package com.estie.arcanetweaks.compat.alexscaves;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.estie.arcanetweaks.ArcaneTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeConfigLoader {
    
    private static final String FILE_NAME = "biome_visual_overrides.toml";
    private static List<BiomeConfig> biomeConfigs = new ArrayList<>();
    private static Map<ResourceKey<Biome>, BiomeConfig> idOverrides = new HashMap<>();
    private static Map<TagKey<Biome>, BiomeConfig> tagOverrides = new HashMap<>();
    public static Map<ResourceKey<Biome>, BiomeConfig> lookupOverrides = new HashMap<>();
    
    public static void init() {
        File modConfigDir = FMLPaths.CONFIGDIR.get().resolve(ArcaneTweaks.MODID).toFile();
        File file = new File(modConfigDir, FILE_NAME);
        
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeDefaults(file);
            } catch (IOException e) {
                ArcaneTweaks.LOGGER.error("Could not create config file!", e);
                return;
            }
        }
        
        loadConfig(file);
    }
    
    private static void writeDefaults(File file) {
        String defaultToml = """
        # This file controls biome visual overrides using Alex's Caves system (thank you for hardcoding everything)
        # Supports both biomes and biome tags (use isTag)
        
        [[biomes]]
        id = "minecraft:plains"
        isTag = false
        ambient_light = 0.3
        fog_nearness = 0.0
        water_fog_farness = 1.0
        sky_override = 1.0
        light_color = [1.0, 1.0, 1.0]
        tablet_color = "0x393F77"
        """;
        
        try {
            Files.writeString(file.toPath(), defaultToml, StandardOpenOption.APPEND);
        } catch (IOException e) {
            ArcaneTweaks.LOGGER.error("Could not write default biome config!", e);
        }
    }
    
    private static void loadConfig(File file) {
        try (FileConfig cfg = FileConfig.of(file, TomlFormat.instance())) {
            cfg.load();
            biomeConfigs.clear();
            idOverrides.clear();
            tagOverrides.clear();
            
            List<UnmodifiableConfig> biomes = cfg.get("biomes");
            if (biomes == null) return;
            
            for (UnmodifiableConfig section : biomes) {
                String id = section.get("id");
                boolean isTag = section.getOrElse("isTag", false);
                
                float ambientLight = section.getOrElse("ambient_light", 0.0).floatValue();
                float fogNear = section.getOrElse("fog_nearness", 0.0).floatValue();
                float waterFogFar = section.getOrElse("water_fog_farness", 1.0).floatValue();
                float sky = section.getOrElse("sky_override", 0.0).floatValue();
                
                List<?> colorList = section.getOrElse("light_color", List.of(1.0F, 1.0F, 1.0F));
                Vec3 color = new Vec3(
                        ((Number) colorList.get(0)).doubleValue(),
                        ((Number) colorList.get(1)).doubleValue(),
                        ((Number) colorList.get(2)).doubleValue()
                );
                
                String hex = section.getOrElse("tablet_color", "0xFFFFFF");
                int tabletColor = Integer.decode(hex);
                
                biomeConfigs.add(new BiomeConfig(id, isTag, ambientLight, fogNear, waterFogFar, sky, color, tabletColor));
            }
        }
        
        for (BiomeConfig bcfg : biomeConfigs) {
            if (bcfg.isTag) tagOverrides.put(TagKey.create(Registries.BIOME, bcfg.key), bcfg);
            else idOverrides.put(ResourceKey.create(Registries.BIOME, bcfg.key), bcfg);
        }
        
        lookupOverrides.clear();
        
        lookupOverrides.putAll(idOverrides);
        
        var registry = ForgeRegistries.BIOMES;
        for (var entry : tagOverrides.entrySet()) {
            TagKey<Biome> tag = entry.getKey();
            BiomeConfig config = entry.getValue();
            
            for (ResourceLocation rl : registry.getKeys()) {
                var holder = registry.getHolder(rl).orElse(null);
                if (holder == null) continue;
                
                if (holder.is(tag)) {
                    ResourceKey<Biome> key = holder.unwrapKey().orElse(null);
                    if (holder.unwrapKey().orElse(null) != null) lookupOverrides.put(key, config);
                }
            }
        }
    }
}