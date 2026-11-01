package com.estie.arcanetweaks.handlers;

import com.estie.arcanetweaks.ArcaneTweaks;
import com.estie.arcanetweaks.Config;
import com.estie.arcanetweaks.mobs.MobStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class MobAttributeHandler {
    
    public static final UUID ATTACK_UUID = UUID.fromString("c1b8f4c0-8c3e-4c1a-9f0c-1e4b7a2d9f11");
    public static final UUID ARMOR_UUID  = UUID.fromString("7e2d1a44-5b8f-4e6a-9c3d-2f8a1c7b4e22");
    public static final UUID HEALTH_UUID = UUID.fromString("a4d3c9b2-1f6e-4b8a-8c1d-3e7f2a9b5c33");
    public static final UUID SPEED_UUID  = UUID.fromString("d9f2a7c1-3b4e-4c8a-9d1f-5a6b7c8d9e44");
    public static final UUID FOLLOW_UUID = UUID.fromString("e3c7b1d4-2a5f-4e9c-8b3d-6f7a8c9b0d55");
    
    @SubscribeEvent
    public static void onJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity living)) return;
        
        MobStats stats = Config.mobAttributeModifiers.get(living.getType());
        if (stats == null) return;
        
        apply(living, Attributes.ATTACK_DAMAGE, stats.attack(), ATTACK_UUID);
        apply(living, Attributes.ARMOR, stats.armor(), ARMOR_UUID);
        apply(living, Attributes.MAX_HEALTH, stats.health(), HEALTH_UUID);
        apply(living, Attributes.MOVEMENT_SPEED, stats.speed(), SPEED_UUID);
        apply(living, Attributes.FOLLOW_RANGE, stats.follow(), FOLLOW_UUID);
    }
    
    private static void apply(LivingEntity e, Attribute attr, double mult, UUID id) {
        AttributeInstance inst = e.getAttribute(attr);
        if (inst == null) return;
        
        inst.removeModifier(id);
        inst.addPermanentModifier(new AttributeModifier(id, "arcanetweaks:" + attr.getDescriptionId(), mult - 1.0, AttributeModifier.Operation.MULTIPLY_BASE));
        
        if (attr == Attributes.MAX_HEALTH) e.setHealth((float) inst.getValue());
    }
}