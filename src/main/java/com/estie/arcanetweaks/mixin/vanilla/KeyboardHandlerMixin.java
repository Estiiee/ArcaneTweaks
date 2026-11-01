package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyboardHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@OnlyIn(Dist.CLIENT)
@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    
    @Redirect(
            method = "keyPress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/GameNarrator;isActive()Z"
            )
    )
    private boolean redirectNarratorCheck(GameNarrator narrator) {
        if (!Config.narratorKeybind) return false;
        else return narrator.isActive();
    }
}