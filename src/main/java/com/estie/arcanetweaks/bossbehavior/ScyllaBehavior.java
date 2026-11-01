package com.estie.arcanetweaks.bossbehavior;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.lightning.ThunderstormSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ScyllaBehavior extends BossBehavior<Scylla_Entity> {
    
    public ScyllaBehavior() {
        super(1);
    }
    
    @Override
    public void onPhaseTick(Scylla_Entity boss, int phase) {
        if (boss.tickCount % 20 != 0) return;
        //atp I'm starting to question why I even added onPhaseChange()
        CompoundTag data = boss.getPersistentData();
        int p = boss.isPhase();
        
        if (p == 1 && !data.getBoolean("ArcaneTweaks_P1Flag")) {
            heal(boss);
            data.putBoolean("ArcaneTweaks_P1Flag", true);
        }
        
        if (p == 2 && !data.getBoolean("ArcaneTweaks_P2Flag")) {
            heal(boss);
            castThunderstorm(boss);
            data.putBoolean("ArcaneTweaks_P2Flag", true);
        }
    }
    
    private void heal(Scylla_Entity boss) {
        boss.addEffect(new MobEffectInstance(MobEffects.HEAL, 100, 9));
    }
    
    private void castThunderstorm(Scylla_Entity boss) {
        ThunderstormSpell thunderstorm = new ThunderstormSpell();
        thunderstorm.onCast(boss.level(), 20, boss, CastSource.MOB, MagicData.getPlayerMagicData(boss));
        
        MobEffect thunderstormEffect = MobEffectRegistry.THUNDERSTORM.get();
        boss.removeEffect(thunderstormEffect);
        boss.addEffect(new MobEffectInstance(thunderstormEffect, Integer.MAX_VALUE, 92, false, true, true));
    }
}