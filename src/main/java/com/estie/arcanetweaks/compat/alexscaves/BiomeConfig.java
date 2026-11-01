package com.estie.arcanetweaks.compat.alexscaves;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class BiomeConfig {
    public final String id;
    public final boolean isTag;
    public final ResourceLocation key;
    
    public final float ambientLight;
    public final float fogNearness;
    public final float waterFogFarness;
    public final float skyOverride;
    public final Vec3 lightColor;
    public final int tabletColor;
    
    public BiomeConfig(String id, boolean isTag, float ambientLight, float fogNearness, float waterFogFarness, float skyOverride, Vec3 lightColor, int tabletColor) {
        this.id = id;
        this.isTag = isTag;
        this.key = new ResourceLocation(id);
        
        this.ambientLight = ambientLight;
        this.fogNearness = fogNearness;
        this.waterFogFarness = waterFogFarness;
        this.skyOverride = skyOverride;
        this.lightColor = lightColor;
        this.tabletColor = tabletColor;
    }
    
    public boolean matches(Holder<Biome> biome) {
        if (isTag) {
            TagKey<Biome> tag = TagKey.create(Registries.BIOME, key);
            return biome.is(tag);
        } else {
            ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, key);
            return biome.is(biomeKey);
        }
    }
}
