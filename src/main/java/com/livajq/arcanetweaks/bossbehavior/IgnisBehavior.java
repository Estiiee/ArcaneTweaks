package com.livajq.arcanetweaks.bossbehavior;

import com.gametechbc.traveloptics.effects.MeteorStormEffect;
import com.gametechbc.traveloptics.init.TravelopticsEffects;
import com.gametechbc.traveloptics.init.TravelopticsMessages;
import com.gametechbc.traveloptics.spells.fire.MeteorStormSpell;
import com.gametechbc.traveloptics.util.TravelopticsImpactFramePresets;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class IgnisBehavior extends BossBehavior<Ignis_Entity> {
    
    public IgnisBehavior() {
        super(1);
    }
    
    @Override
    public void onPhaseTick(Ignis_Entity boss, int phase) {
        if (boss.getAnimation() == Ignis_Entity.ULTIMATE_ATTACK && boss.getAnimationTick() == 74)
            TravelopticsMessages.sendImpactFrameToRange(boss, 64, TravelopticsImpactFramePresets.sequence(TravelopticsImpactFramePresets.FIRE_SHADOWS, TravelopticsImpactFramePresets.FIRE_TONES));
        
        if (boss.tickCount % 20 != 0) return;
        //same as obliterator
        CompoundTag data = boss.getPersistentData();
        int p = boss.getBossPhase();
        
        if (p == 1 && !data.getBoolean("ArcaneTweaks_P1Flag")) {
            castMeteors(boss);
            data.putBoolean("ArcaneTweaks_P1Flag", true);
        }
    }
    
    private void castMeteors(Ignis_Entity boss) {
        MeteorStormSpell meteorStorm = new MeteorStormSpell();
        meteorStorm.onCast(boss.level(), 20, boss, CastSource.MOB, MagicData.getPlayerMagicData(boss));
        
        MobEffect meteorEffect = TravelopticsEffects.METEOR_STORM.get();
        boss.removeEffect(meteorEffect);
        boss.addEffect(new MobEffectInstance(meteorEffect, 400, 150));
        
        MobEffectInstance effectInstance = boss.getEffect(meteorEffect);
        if (effectInstance != null && effectInstance.getEffect() instanceof MeteorStormEffect stormEffect) stormEffect.setOuterRadius(16);
    }
}