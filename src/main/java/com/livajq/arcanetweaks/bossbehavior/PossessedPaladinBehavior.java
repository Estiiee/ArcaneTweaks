package com.livajq.arcanetweaks.bossbehavior;

import com.gametechbc.traveloptics.init.TravelopticsMessages;
import com.gametechbc.traveloptics.util.TravelopticsImpactFramePresets;
import net.miauczel.legendary_monsters.entity.AnimatedMonster.IAnimatedBoss.PossessedPaladin.PossessedPaladinEntity;

public class PossessedPaladinBehavior extends BossBehavior<PossessedPaladinEntity> {
    
    public PossessedPaladinBehavior() {
        super(1);
    }
    
    @Override
    public void onPhaseTick(PossessedPaladinEntity boss, int phase) {
        if (boss.getAttackState() == 26 && boss.attackTicks == 49)
            TravelopticsMessages.sendImpactFrameToRange(boss, 64, TravelopticsImpactFramePresets.sequence(TravelopticsImpactFramePresets.MONOCHROME_SHADOWS, TravelopticsImpactFramePresets.MONOCHROME_TONES));
    }
    
}