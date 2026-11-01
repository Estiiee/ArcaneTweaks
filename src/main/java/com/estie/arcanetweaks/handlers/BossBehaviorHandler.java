package com.estie.arcanetweaks.handlers;

import com.estie.arcanecontent.common.capability.ArcaneCapabilities;
import com.estie.arcanetweaks.ArcaneTweaks;
import com.estie.arcanetweaks.bossbehavior.BossBehavior;
import com.estie.arcanetweaks.bossbehavior.BossBehaviorRegistry;
import com.estie.arcanetweaks.bossbehavior.HurtResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class BossBehaviorHandler {

    @SubscribeEvent
    public static void onBossTick(LivingEvent.LivingTickEvent event) {
        LivingEntity boss = event.getEntity();
        if (boss.level().isClientSide()) return;
        
        BossBehavior<LivingEntity> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        double hpRatio = boss.getHealth() / boss.getMaxHealth();
        
        int newPhase = 1;
        for (int i = 0; i < behavior.thresholds.length; i++) {
            if (hpRatio <= behavior.thresholds[i]) newPhase = i + 2;
        }
        
        CompoundTag tag = boss.getPersistentData();
        
        int currentPhase = tag.contains("Arcane_BossPhase") ? tag.getInt("Arcane_BossPhase") : 1;
        int visitedMask = tag.getInt("Arcane_BossVisitedMask");
        
        if (newPhase != currentPhase) {
            boolean firstTime = (visitedMask & (1 << (newPhase - 1))) == 0;
            
            visitedMask |= (1 << (newPhase - 1));
            
            tag.putInt("Arcane_BossPhase", newPhase);
            tag.putInt("Arcane_BossVisitedMask", visitedMask);
            
            if (!boss.isDeadOrDying()) behavior.onPhaseChange(boss, newPhase, currentPhase, firstTime);
        }
        
        behavior.onPhaseTick(boss,  currentPhase);
    }
    
    @SubscribeEvent
    public static void onBossOrTargetHurt(LivingHurtEvent event) {
        LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity living ? living : null;
        LivingEntity target = event.getEntity();
        
        BossBehavior<LivingEntity> behaviorAttacker = attacker != null ? BossBehaviorRegistry.get(attacker) : null;
        BossBehavior<LivingEntity> behaviorTarget = BossBehaviorRegistry.get(target);
        
        if (behaviorAttacker != null) {
            HurtResult result = behaviorAttacker.onAttack(attacker, target, event.getSource(), event.getAmount());
            
            if (result.cancel) {
                event.setCanceled(true);
                return;
            }
            if (result.modify) event.setAmount(result.newAmount);
        }
        
        if (behaviorTarget != null) {
            HurtResult result = behaviorTarget.onHurt(target, event.getSource(), event.getAmount());
            
            if (result.cancel) {
                event.setCanceled(true);
                return;
            }
            if (result.modify) event.setAmount(result.newAmount);
        }
    }
    
    @SubscribeEvent
    public static void onBossJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity boss)) return;
        if (boss.level().isClientSide()) return;
        
        BossBehavior<LivingEntity> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        behavior.reconcileMinions(boss);
    }
    
    @SubscribeEvent
    public static void onBossDeath(LivingDeathEvent event) {
        LivingEntity boss = event.getEntity();
        if (boss.level().isClientSide()) return;
        
        BossBehavior<LivingEntity> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        behavior.onBossDied(boss);
    }
    
    @SubscribeEvent
    public static void onMinionDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        
        mob.getCapability(ArcaneCapabilities.BOSS_MINION).ifPresent(cap -> {
            LivingEntity boss = cap.getBoss();
            if (boss == null) return;
            
            BossBehavior<LivingEntity> behavior = BossBehaviorRegistry.get(boss);
            if (behavior == null) return;
            
            Set<UUID> set = BossBehavior.MINIONS.get(boss);
            if (set != null) set.remove(mob.getUUID());
            
            behavior.onMinionDied(boss, mob);
        });
    }
    
    @SubscribeEvent
    public static void onMinionJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        
        mob.getCapability(ArcaneCapabilities.BOSS_MINION).ifPresent(cap -> {
            LivingEntity boss = cap.getBoss();
            if (boss == null) return;
            
            BossBehavior<LivingEntity> behavior = BossBehaviorRegistry.get(boss);
            if (behavior == null) return;
            
            Set<UUID> set = BossBehavior.MINIONS.computeIfAbsent(boss, b -> new HashSet<>());
            
            if (set.add(mob.getUUID())) behavior.onMinionAdded(boss, mob);
        });
    }
}