package com.estie.arcanetweaks.handlers;

import com.estie.arcanetweaks.ArcaneTweaks;
import jaredbgreat.dldungeons.themes.Theme;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class DLDungeonsHandler {
    public static final Map<String, String> THEME_TAGS = new HashMap<>();
    private static boolean configLoaded = false;
    
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        if (configLoaded) return;
        
        try {
            Path modConfigDir = FMLPaths.CONFIGDIR.get().resolve(ArcaneTweaks.MODID);
            Path file = modConfigDir.resolve("dld_theme_tags.cfg");
            
            if (Files.notExists(file)) {
                Files.createFile(file);
                
                String header = ""
                        + "# This file determines which biome tags each DLDungeons theme is associated with.\n"
                        + "# For a structure with a given theme to generate, at least one of its tags must\n"
                        + "# match the tags of the biome it is generating in.\n"
                        + "#\n"
                        + "# Themes can have multiple tags separated by commas, e.g.:\n"
                        + "#   dldungeonsjbg:frozen - forge:is_cold, forge:is_snowy\n"
                        + "#\n"
                        + "# Themes tagged with NONE can generate everywhere. Themes tagged with DISABLED cannot generate at all\n"
                        + "# This only affects overworld land generation\n"
                        + "#\n"
                        + "# Useful tags:\n"
                        + "#   forge:is_plateau\n"
                        + "#   forge:is_swamp\n"
                        + "#   forge:is_wet\n"
                        + "#   forge:is_void\n"
                        + "#   forge:is_dry\n"
                        + "#   forge:is_sparse\n"
                        + "#   forge:is_coniferous\n"
                        + "#   forge:is_slope\n"
                        + "#   forge:is_plains\n"
                        + "#   forge:is_sandy\n"
                        + "#   forge:is_wasteland\n"
                        + "#   forge:is_peak\n"
                        + "#   forge:is_mushroom\n"
                        + "#   forge:is_hot\n"
                        + "#   forge:is_lush\n"
                        + "#   forge:is_cold\n"
                        + "#   forge:is_desert\n"
                        + "#   forge:is_rare\n"
                        + "#   forge:is_dense\n"
                        + "#   forge:is_spooky\n"
                        + "#   forge:is_mountain\n"
                        + "#   forge:is_cave\n"
                        + "#   forge:is_snowy\n"
                        + "#   forge:is_underground\n"
                        + "#   forge:is_water\n"
                        + "#\n"
                        + "# Specific biome tags such as minecraft:is_savanna are also valid.\n"
                        + "#\n";
                
                Files.writeString(file, header, StandardOpenOption.APPEND);
            }
            
            List<String> existingLines = Files.readAllLines(file);
            Set<String> existingThemes = new HashSet<>();
            
            for (String line : existingLines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                int idx = line.indexOf(" - ");
                if (idx == -1) continue;
                
                String themeName = line.substring(0, idx).trim();
                existingThemes.add(themeName);
            }
            
            StringBuilder append = new StringBuilder();
            
            for (Theme theme : Theme.themeMap.values()) {
                String key = theme.name;
                if (!existingThemes.contains(key)) {
                    append.append(key)
                            .append(" - ")
                            .append(setBasicTags(theme.name))
                            .append(System.lineSeparator());
                }
            }
            
            if (!append.isEmpty()) {
                Files.writeString(file, System.lineSeparator(), StandardOpenOption.APPEND);
                Files.writeString(file, append.toString(), StandardOpenOption.APPEND);
            }
            parseThemeTagConfig(file);
            configLoaded = true;
        }
        catch (IOException e) {
            ArcaneTweaks.LOGGER.error("Error: failed to load dld_theme_tags.cfg!", e);
        }
    }
    
    public static Theme pickTheme(Holder<Biome> biome, RandomSource random) {
        Set<TagKey<Biome>> biomeTags = biome.tags().collect(Collectors.toSet());
        List<Theme> valid = new ArrayList<>();
        
        for (Theme theme : Theme.themeMap.values()) {
            String tagString = THEME_TAGS.get(theme.name);
            if (tagString == null) continue;

            if (tagString.equals("DISABLED")) continue;
            
            if (tagString.equals("NONE")) {
                valid.add(theme);
                continue;
            }
            
            String[] parts = tagString.split(",");
            for (String part : parts) {
                ResourceLocation rl = new ResourceLocation(part.trim());
                TagKey<Biome> tag = TagKey.create(Registries.BIOME, rl);
                
                if (biomeTags.contains(tag)) {
                    valid.add(theme);
                    break;
                }
            }
        }
        
        return valid.isEmpty() ? null : valid.get(random.nextInt(valid.size()));
    }
    
    public static void parseThemeTagConfig(Path file) {
        try {
            THEME_TAGS.clear();
            List<String> lines = Files.readAllLines(file);
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                int idx = line.indexOf(" - ");
                if (idx == -1) continue;
                
                String theme = line.substring(0, idx).trim();
                String tags = line.substring(idx + 3).trim();
                
                THEME_TAGS.put(theme, tags);
            }
        }
        catch (IOException e) {
            ArcaneTweaks.LOGGER.error("Error: could not parse config file!", e);
        }
    }
    
    private static String setBasicTags(String themeName) {
        themeName = themeName.toLowerCase();
        
        String tags;
        if (themeName.contains("frozen")) tags = "forge:is_cold, forge:is_snowy";
        else if (themeName.contains("volcanic")) tags = "forge:is_hot, forge:is_sandy";
        else if (themeName.contains("desert")) tags = "forge:is_desert";
        else if (themeName.contains("jungle")) tags = "forge:is_lush";
        else if (themeName.contains("mesa")) tags = "forge:is_plateau";
        else if (themeName.contains("dank")) tags = "forge:is_swamp";
        else if (themeName.contains("oceanic")) tags = "DISABLED";
        else if (themeName.contains("nether")) tags = "DISABLED";
        else tags = "NONE";
        
        return tags;
    }
}