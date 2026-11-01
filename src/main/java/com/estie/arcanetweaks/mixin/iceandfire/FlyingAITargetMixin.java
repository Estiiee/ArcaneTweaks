package com.estie.arcanetweaks.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.ai.FlyingAITarget;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlyingAITarget.class)
public class FlyingAITargetMixin {
    
    @Redirect(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"
            )
    )
    private boolean spoofWater(Mob mob) {
        return true;
    }
}