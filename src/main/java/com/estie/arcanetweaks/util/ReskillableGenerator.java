package com.estie.arcanetweaks.util;

import com.estie.arcanetweaks.ArcaneTweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Map.entry;

public class ReskillableGenerator {
    
    private static final Map<String, Integer> MATERIAL_LEVELS = Map.ofEntries(
            entry("copper", 2),
            entry("chainmail", 4),
            entry("iron", 8),
            entry("blackiron", 10),
            entry("steel", 12),
            entry("diamond", 16),
            entry("shimmersteel", 16),
            entry("myrmex", 16),
            entry("troll", 16),
            entry("terrible", 16),
            entry("mutantskeleton", 16),
            entry("cursedpaladin", 16),
            entry("deathworm", 16),
            entry("netherite", 20),
            entry("dragonscale", 24),
            entry("tide", 24),
            entry("blindingabyss", 24),
            entry("mutantwither", 24),
            entry("primordial", 24),
            entry("dark", 24),
            entry("scoria", 32),
            entry("skrythe", 36),
            entry("leive", 36),
            entry("golem", 36),
            entry("rakoth", 36),
            entry("rhyza", 36),
            entry("tectonic", 36),
            entry("forlorn", 36),
            entry("dragonsteel", 40),
            entry("ignitium", 50),
            entry("living", 50),
            entry("ghostwarrior", 50),
            entry("cursedwraithguard", 50),
            entry("abyssalhide", 50),
            entry("cursium", 50),
            entry("exoskeleton", 50),
            entry("sovereign", 50),
            entry("annihilator", 50)
    );
    
    public static void generateReskillableEntries() {
        ArcaneTweaks.LOGGER.info("=== GENERATING RESKILLABLE ENTRIES ===");
        
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id == null) continue;
            
            String key = id.toString();
            String name = id.getPath().toLowerCase(Locale.ROOT).replace("_", "").replace(" ", "");
            
            String skill = null;
            
            // --- CLASSIFY SKILL TYPE ---
            if (item instanceof ArmorItem) skill = "defense";
            else if (item instanceof SwordItem) skill = "attack";
            else if (item instanceof HoeItem) skill = "farming";
            else if (item instanceof PickaxeItem) skill = "mining";
            else if (item instanceof ShovelItem || item instanceof AxeItem || item instanceof DiggerItem) skill = "gathering";
            else continue;
       
            int level = detectMaterialLevel(item, name);
            ArcaneTweaks.LOGGER.info("    \"{}\": [\"{}:{}\"],", key, skill, level);
        }
        
        ArcaneTweaks.LOGGER.info("=== DONE ===");
    }
    
    private static int detectMaterialLevel(Item item, String normalizedName) {
        
        // 1. Mat check
        if (item instanceof ArmorItem armor) {
            String mat = armor.getMaterial().getName().toLowerCase(Locale.ROOT)
                    .replace("_", "")
                    .replace(" ", "");
            Integer lvl = MATERIAL_LEVELS.get(mat);
            if (lvl != null) return lvl;
        }
        
        if (item instanceof TieredItem tiered) {
            String mat = tiered.getTier().toString().toLowerCase(Locale.ROOT)
                    .replace("_", "")
                    .replace(" ", "");
            Integer lvl = MATERIAL_LEVELS.get(mat);
            if (lvl != null) return lvl;
        }
        
        // 2. Sort keys by descending length so "dragonsteel" beats "steel" and whatever
        List<String> keys = new ArrayList<>(MATERIAL_LEVELS.keySet());
        keys.sort((a, b) -> Integer.compare(b.length(), a.length()));
        
        for (String mat : keys) {
            if (normalizedName.contains(mat)) return MATERIAL_LEVELS.get(mat);
        }
        
        // 3. Dragon moment
        if (normalizedName.contains("dragon")) return 24;
        
        return 10;
    }
}