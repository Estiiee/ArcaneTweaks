package com.estie.arcanetweaks.util;

import com.estie.arcanetweaks.ArcaneTweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class ItemObliteratorGenerator {
    
    public static void generateObliteratorBlacklist() {
        ArcaneTweaks.LOGGER.info("=== GENERATING OBLITERATOR BLACKLIST ===");
        
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id == null) continue;
            
            String key = id.toString();
            String path = id.getPath().toLowerCase(Locale.ROOT);
            
            if (isUmbrium(item)) {
                ArcaneTweaks.LOGGER.info("    \"{}\",", key);
                continue;
            }
            
            if (isDivingGear(item)) {
                ArcaneTweaks.LOGGER.info("    \"{}\",", key);
                continue;
            }
            
            if (path.contains("diving")) {
                ArcaneTweaks.LOGGER.info("    \"{}\",", key);
                continue;
            }
      
            if (path.contains("shader_bag")) {
                ArcaneTweaks.LOGGER.info("    \"{}\",", key);
                continue;
            }
            
            if (path.contains("bookwyrm") || path.contains("book_wyrm") || path.contains("wyrm")) {
                ArcaneTweaks.LOGGER.info("    \"{}\",", key);
                continue;
            }
        }
        
        ArcaneTweaks.LOGGER.info("=== DONE ===");
    }
    
    private static boolean isUmbrium(Item item) {
        if (item instanceof ArmorItem armor) {
            String mat = armor.getMaterial().getName().toLowerCase(Locale.ROOT);
            return mat.contains("umbrium");
        }

        if (item instanceof TieredItem tiered) {
            String mat = tiered.getTier().toString().toLowerCase(Locale.ROOT);
            return mat.contains("umbrium");
        }
        
        return false;
    }
    
    private static boolean isDivingGear(Item item) {
        if (!(item instanceof ArmorItem armor)) return false;
        
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) return false;
        String path = id.getPath().toLowerCase(Locale.ROOT);
        
        if (!id.toString().contains("secretsofthevoid")) return false;
        if (path.contains("razor_boots")) return true;

        return !path.contains("helmet")
                && !path.contains("chestplate")
                && !path.contains("leggings")
                && !path.contains("boots");
    }
    
}
