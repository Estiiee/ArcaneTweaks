package com.estie.arcanetweaks.handlers;

import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.estie.arcanetweaks.ArcaneTweaks;
import com.estie.arcanetweaks.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class IceAndFireHandler {
    
    //stop dread mobs from targeting allies
    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity attacker = event.getEntity();
        LivingEntity newTarget = event.getNewTarget();
        if (newTarget == null || !(attacker instanceof IDreadMob)) return;
        
        if (isDreadMobAlly(newTarget)) event.setCanceled(true);
    }
    
    //cancel dread mob and allied mob friendly fire
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        
        Entity attacker = event.getSource().getEntity();
        LivingEntity victim = event.getEntity();
        
        if (attacker instanceof Projectile projectile) {
            Entity owner = projectile.getOwner();;
            if (owner != null) attacker = owner;
        }
        
        if (attacker == null) return;
        
        boolean attackerIsDread = attacker instanceof IDreadMob;
        boolean victimIsDread = victim instanceof IDreadMob;
        
        if (attackerIsDread == victimIsDread) return;
        
        if (attackerIsDread && isDreadMobAlly(victim)) event.setCanceled(true);
        else if (isDreadMobAlly(attacker)) event.setCanceled(true);
    }
    
    private static boolean isDreadMobAlly(Entity entity) {
        if (entity == null) return false;
        
        EntityType<?> type = entity.getType();
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
        if (id == null) return false;
        
        return Config.extraAlliesSet.contains(id.toString());
    }
}