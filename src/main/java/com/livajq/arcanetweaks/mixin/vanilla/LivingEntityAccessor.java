package com.livajq.arcanetweaks.mixin.vanilla;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("lerpSteps") int arcanetweaks$getLerpSteps();
    @Accessor("lerpSteps") void arcanetweaks$setLerpSteps(int v);
    @Accessor("lerpHeadSteps") int arcanetweaks$getLerpHeadSteps();
    @Accessor("lerpHeadSteps") void arcanetweaks$setLerpHeadSteps(int v);
}