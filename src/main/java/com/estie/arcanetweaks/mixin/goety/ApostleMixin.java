package com.estie.arcanetweaks.mixin.goety;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.estie.arcanetweaks.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Apostle.class)
public class ApostleMixin {
    
    @Inject(method = "isInNether", at = @At("HEAD"), cancellable = true, remap = false)
    private void isInSuperbossBiome(CallbackInfoReturnable<Boolean> cir) {
        Apostle self = (Apostle) (Object) this;
        cir.setReturnValue(self.level().getBiome(self.blockPosition()).is(Config.apostleSuperbossBiome));
    }
}