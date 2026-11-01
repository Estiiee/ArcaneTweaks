package com.estie.arcanetweaks.handlers;

import com.estie.arcanetweaks.Config;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber
public class VillagerHandler {
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onVillagerTrades(VillagerTradesEvent event) {
        
        /*  Moved to EnchantBookForEmeraldsMixin because this is scuffed, causes weird bugs, leftover trades and whatever else
        
        if (event.getType() != VillagerProfession.LIBRARIAN) return;
        
        //replace all enchanted book trades with a tier system e.g. tier 3 sharpness can only appear at level 3 librarian and above
        //and also add custom item to the cost instead of books (by default diamond)
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        
        for (int level = 1; level <= 4; level++) {
            int villagerLevel = level;
            
            List<VillagerTrades.ItemListing> list = trades.get(level);
            if (list == null) continue;
            
            list.replaceAll(original -> (trader, random) -> {
                MerchantOffer base = original.getOffer(trader, random);
                if (base == null) return null;
                
                ItemStack result = base.getResult();
                if (!(result.getItem() instanceof EnchantedBookItem)) return base;
                
                Map<Enchantment, Integer> enchMap = EnchantmentHelper.getEnchantments(result);
                
                if (enchMap.isEmpty()) return base;
                
                Enchantment ench = enchMap.keySet().iterator().next();
                ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(ench);
                
                int enchTier = Config.enchantmentTiers.getOrDefault(id, -1);
                
                if (enchTier > 0 && villagerLevel < enchTier) return null; //safe? who knows
                
                int emeralds = base.getBaseCostA().getCount();
                
                if (enchTier > 0) {
                    int diff = villagerLevel - enchTier;
                    float discount = 1.0f - (diff * 0.20f);
                    discount = Math.max(0.20f, discount);
                    emeralds = Math.max(1, Math.round(emeralds * discount));
                }
                
                int min = Math.max(1, emeralds - 5);
                int max = emeralds + 5;
                int diamonds = Mth.nextInt(random, min, max);
                diamonds = Mth.clamp(diamonds, 1, 64);
                
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, emeralds),
                        new ItemStack(Items.DIAMOND, diamonds),
                        result.copy(),
                        base.getMaxUses(),
                        base.getXp(),
                        base.getPriceMultiplier()
                );
            });
        }
        
         */
        
        VillagerProfession prof = event.getType();
        if (prof != VillagerProfession.ARMORER &&
                prof != VillagerProfession.TOOLSMITH &&
                prof != VillagerProfession.WEAPONSMITH) {
            return;
        }
        
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        
        for (int level = 1; level <= 5; level++) {
            List<VillagerTrades.ItemListing> list = trades.get(level);
            if (list == null) continue;
            
            list.replaceAll(original -> (trader, random) -> {
                MerchantOffer base = original.getOffer(trader, random);
                if (base == null) return null;
                
                ItemStack result = base.getResult();
                Item item = result.getItem();
                
                Item requiredItem = ForgeRegistries.ITEMS.getValue(Config.tradingCostItem);
                if (requiredItem == null || requiredItem == Items.AIR) requiredItem = Items.DIAMOND;
                
                int requiredCost = getRequiredCost(item);
                if (requiredCost < 0) return base;
                
                return new MerchantOffer(
                        new ItemStack(requiredItem, requiredCost),
                        ItemStack.EMPTY,
                        result.copy(),
                        base.getMaxUses(),
                        base.getXp(),
                        base.getPriceMultiplier()
                );
            });
        }
    }
    
    private static int getRequiredCost(Item item) {
        String id = item.builtInRegistryHolder().key().location().getPath();
        
        return switch (id) {
            case "diamond_helmet" -> 5;
            case "diamond_chestplate" -> 8;
            case "diamond_leggings" -> 7;
            case "diamond_boots" -> 4;
            
            case "diamond_sword" -> 2;
            case "diamond_pickaxe" -> 3;
            case "diamond_axe" -> 3;
            case "diamond_shovel" -> 1;
            case "diamond_hoe" -> 2;
            
            default -> -1;
        };
    }
}