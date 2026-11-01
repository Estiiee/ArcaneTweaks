package com.estie.arcanetweaks.mixin.legendarymonsters;

import com.estie.arcanetweaks.Config;
import net.miauczel.legendary_monsters.entity.AnimatedMonster.IAnimatedBoss.TheObliterator.TheObliteratorEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TheObliteratorEntity.class)
public abstract class TheObliteratorEntityMixin {
    
    @Inject(method = "damageCap", at = @At("HEAD"), cancellable = true, remap = false)
    private void changeDamageCap(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(Config.obliteratorDamageCap);
    }
}