package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    
    @Inject(method = "isTradeable", at = @At("HEAD"), cancellable = true)
    private void blacklistTradeable(CallbackInfoReturnable<Boolean> cir) {
        Enchantment self = (Enchantment)(Object)this;
        ResourceLocation id = BuiltInRegistries.ENCHANTMENT.getKey(self);
        if (id == null) return;
        
        if (Config.villagerBookBlacklistSet.contains(id.toString())) cir.setReturnValue(false);
    }
}
