package com.estie.arcanetweaks.mixin.goety;

import com.Polarice3.Goety.common.events.ItemEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemEvents.class)
public class ItemEventsMixin {
    
    @ModifyVariable(
            method = "PlayerTick(Lnet/minecraftforge/event/TickEvent$PlayerTickEvent;)V",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0,
            remap = false
    )
    private static boolean broadScytheCheck(boolean original, TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        ItemStack stack = player.getMainHandItem();
        
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id != null && id.getPath().toLowerCase().contains("scythe")) return true;
        return original;
    }
    
    @ModifyVariable(
            method = "PlayerTick(Lnet/minecraftforge/event/TickEvent$PlayerTickEvent;)V",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 3,
            remap = false
    )
    private static boolean broadHammerCheck(boolean original, TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        ItemStack stack = player.getMainHandItem();
        
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id != null && id.getPath().toLowerCase().contains("hammer")) return true;
        return original;
    }
}