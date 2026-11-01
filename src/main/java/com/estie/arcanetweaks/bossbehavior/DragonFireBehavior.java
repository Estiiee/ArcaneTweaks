package com.estie.arcanetweaks.bossbehavior;

import com.estie.arcanecontent.init.ArcaneSounds;
import com.gametechbc.traveloptics.network.ImpactFramePacket;
import com.gametechbc.traveloptics.util.TravelopticsImpactFramePresets;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.estie.arcanetweaks.Config;
import net.minecraft.sounds.SoundEvent;

import java.awt.*;
import java.util.List;

public class DragonFireBehavior extends DragonBehavior<EntityFireDragon> {
    private static final Color FIRE_EXPLOSION_COLOR = new Color(Config.dragonNukeColorFire);
    
    public DragonFireBehavior() {
        super(2);
    }
    
    @Override
    public Color getExplosionColor() {
        return FIRE_EXPLOSION_COLOR;
    }
    
    @Override
    public SoundEvent getExplosionChargeSound() {
        return ArcaneSounds.DRAGON_NUKE_CHARGE_FIRE.get();
    }
    
    @Override
    public List<ImpactFramePacket.Frame> getImpactFramePreset() {
        return TravelopticsImpactFramePresets.sequence(TravelopticsImpactFramePresets.FIRE_SHADOWS, TravelopticsImpactFramePresets.FIRE_TONES);
    }
}
