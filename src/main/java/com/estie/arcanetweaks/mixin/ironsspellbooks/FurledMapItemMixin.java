package com.estie.arcanetweaks.mixin.ironsspellbooks;

import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FurledMapItem.class)
public abstract class FurledMapItemMixin {
    
    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceKey;equals(Ljava/lang/Object;)Z"
            )
    )
    private boolean redirectDimensionCheck(ResourceKey<Level> currentDim, Object restrictionObj, Level level, Player player, InteractionHand hand) {
        if (!(restrictionObj instanceof ResourceKey<?> restrictionRaw)) return currentDim.equals(restrictionObj);
        
        @SuppressWarnings("unchecked")
        ResourceKey<Level> restriction = (ResourceKey<Level>) restrictionRaw;
        
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() == ItemRegistry.CITADEL_FURLED_MAP.get()) {
            if (currentDim.equals(FurledMapItem.OVERWORLD)) return true;
            else return false;
        }

        return currentDim.equals(restriction);
    }
}