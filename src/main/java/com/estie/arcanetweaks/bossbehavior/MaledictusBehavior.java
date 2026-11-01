package com.estie.arcanetweaks.bossbehavior;

import com.gametechbc.traveloptics.init.TravelopticsEffects;
import com.gametechbc.traveloptics.spells.eldritch.BlackoutSpell;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.world.effect.MobEffectInstance;

public class MaledictusBehavior extends BossBehavior<Maledictus_Entity> {
    
    public MaledictusBehavior() {
        super(2);
    }
    
    @Override
    public void onPhaseTick(Maledictus_Entity boss, int phase) {
        if (boss.tickCount % 400 != 0) return;
        
        BlackoutSpell blackout = new BlackoutSpell();
        blackout.onCast(boss.level(), 2, boss, CastSource.MOB, MagicData.getPlayerMagicData(boss));
    }
    
    @Override
    public void onPhaseChange(Maledictus_Entity boss, int newPhase, int oldPhase, boolean firstTime) {
        if (!firstTime || newPhase != 2) return;
        
        boss.addEffect(new MobEffectInstance(TravelopticsEffects.PHANTOM_RAGE.get(), Integer.MAX_VALUE, 0));
    }
}