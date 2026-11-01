package com.livajq.arcanetweaks.bossbehavior;

import com.estie.arcanecontent.init.ArcaneSounds;
import com.gametechbc.traveloptics.network.ImpactFramePacket;
import com.gametechbc.traveloptics.util.TravelopticsImpactFramePresets;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.livajq.arcanetweaks.Config;
import net.minecraft.sounds.SoundEvent;

import java.awt.*;
import java.util.List;

public class DragonLightningBehavior extends DragonBehavior<EntityLightningDragon> {
    private static final Color LIGHTNING_EXPLOSION_COLOR = new Color(Config.dragonNukeColorLightning);
    
    public DragonLightningBehavior() {
        super(2);
    }
    
    @Override
    public Color getExplosionColor() {
        return LIGHTNING_EXPLOSION_COLOR;
    }
    
    @Override
    public SoundEvent getExplosionChargeSound() {
        return ArcaneSounds.DRAGON_NUKE_CHARGE_LIGHTNING.get();
    }
    
    @Override
    public List<ImpactFramePacket.Frame> getImpactFramePreset() {
        return TravelopticsImpactFramePresets.sequence(TravelopticsImpactFramePresets.ENDER_SHADOWS, TravelopticsImpactFramePresets.ENDER_TONES);
    }
}