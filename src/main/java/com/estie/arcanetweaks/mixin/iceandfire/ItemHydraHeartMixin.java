package com.estie.arcanetweaks.mixin.iceandfire;

import com.github.alexthe666.iceandfire.item.ItemHydraHeart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemHydraHeart.class)
public abstract class ItemHydraHeartMixin {
    
    @ModifyVariable(
            method = "inventoryTick",
            at = @At(value = "STORE"),
            name = "level"
    )
    private int adjustRegenLevel(int original, @NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (!(entity instanceof Player player)) return original;
        double healthPercentage = player.getHealth() / Math.max(1.0F, player.getMaxHealth());
        
        if (healthPercentage < 0.5) return 1;
        else if (healthPercentage < 1.0) return 0;
        else return original;
    }
}