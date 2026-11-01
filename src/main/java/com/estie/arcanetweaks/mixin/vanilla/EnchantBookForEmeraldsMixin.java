package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(targets = "net.minecraft.world.entity.npc.VillagerTrades$EnchantBookForEmeralds")
public class EnchantBookForEmeraldsMixin {
    
    @Redirect(
            method = "getOffer",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
            )
    )
    private Stream<Enchantment> redirectFilter(Stream<Enchantment> stream, Predicate<Enchantment> predicate, Entity trader, RandomSource random) {
         Stream<Enchantment> vanillaFiltered = stream.filter(predicate);
        
         int villagerLevel;
         if (trader instanceof Villager v) villagerLevel = v.getVillagerData().getLevel();
         else villagerLevel = 1;
         
        return vanillaFiltered.filter(e -> {
            ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(e);
            int tier = Config.enchantmentTiers.getOrDefault(id, 1);
            return villagerLevel >= tier;
        });
    }
    
    @Redirect(
            method = "getOffer",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/world/item/trading/MerchantOffer"
            )
    )
    private MerchantOffer redirectOffer(ItemStack emeralds, ItemStack book, ItemStack result, int maxUses, int xp, float priceMult, Entity trader, RandomSource random) {
        int vanillaEmeraldCost = emeralds.getCount();
        
        Config.Range r = Config.enchantmentSecondaryCost;
        Item item = ForgeRegistries.ITEMS.getValue(Config.enchantmentSecondaryCostItem);
        if (item == null || item == Items.AIR) item = Items.DIAMOND;
        ItemStack secondaryItem = new ItemStack(item, 1);
        
        float multiplier = Mth.nextFloat(random, r.min(), r.max());
        int secondaryAmount = Math.round(vanillaEmeraldCost * multiplier);
        int max = secondaryItem.getMaxStackSize();
        
        secondaryAmount = Mth.clamp(secondaryAmount, 1, max);
        secondaryItem.setCount(secondaryAmount);
        
        return new MerchantOffer(emeralds, secondaryItem, result, maxUses, xp, priceMult);
    }
}