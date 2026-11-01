package com.estie.arcanetweaks.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.estie.arcanetweaks.Config;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySeaSerpent.class)
public class EntitySeaSerpentMixin {
    
    @Inject(method = "isTouchingMob", at = @At("HEAD"), cancellable = true, remap = false)
    private void extendTouchingMob(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        EntitySeaSerpent self = (EntitySeaSerpent)(Object)this;
        double bonus = Config.seaSerpentReach;
        
        double serpentRadius = (self.getBbWidth() / 2.0) * (1.0 + bonus);
        double targetRadius = entity.getBbWidth() / 2.0;
        
        double maxDist = serpentRadius + targetRadius;
        double maxDistSqr = maxDist * maxDist;
        
        if (self.distanceToSqr(entity) <= maxDistSqr) cir.setReturnValue(true);
    }
}