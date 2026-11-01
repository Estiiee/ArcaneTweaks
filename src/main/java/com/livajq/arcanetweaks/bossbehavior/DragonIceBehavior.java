package com.livajq.arcanetweaks.bossbehavior;

import com.estie.arcanecontent.init.ArcaneSounds;
import com.gametechbc.traveloptics.network.ImpactFramePacket;
import com.gametechbc.traveloptics.util.TravelopticsImpactFramePresets;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.livajq.arcanetweaks.Config;
import net.minecraft.sounds.SoundEvent;

import java.awt.*;
import java.util.List;

public class DragonIceBehavior extends DragonBehavior<EntityIceDragon> {
    private static final Color ICE_EXPLOSION_COLOR = new Color(Config.dragonNukeColorIce);
    
    public DragonIceBehavior() {
        super(2);
    }
    
    @Override
    public Color getExplosionColor() {
        return ICE_EXPLOSION_COLOR;
    }
    
    @Override
    public SoundEvent getExplosionChargeSound() {
        return ArcaneSounds.DRAGON_NUKE_CHARGE_ICE.get();
    }
    
    @Override
    public List<ImpactFramePacket.Frame> getImpactFramePreset() {
        return TravelopticsImpactFramePresets.sequence(TravelopticsImpactFramePresets.AQUA_SHADOWS, TravelopticsImpactFramePresets.AQUA_TONES);
    }
}