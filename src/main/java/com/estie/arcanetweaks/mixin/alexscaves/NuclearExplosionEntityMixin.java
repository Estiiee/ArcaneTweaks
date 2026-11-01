package com.estie.arcanetweaks.mixin.alexscaves;

import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NuclearExplosionEntity.class)
public abstract class NuclearExplosionEntityMixin {
    
    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            cancellable = true
    )
    private void cancelEntityEffects(CallbackInfo ci) {
        NuclearExplosionEntity self = (NuclearExplosionEntity)(Object)this;
        if (self.level().isClientSide || self.getPersistentData().getBoolean("ArcaneTweaks_DragonNuke")) ci.cancel();
    }
}