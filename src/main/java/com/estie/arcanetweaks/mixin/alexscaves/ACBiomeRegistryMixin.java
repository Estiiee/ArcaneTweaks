package com.estie.arcanetweaks.mixin.alexscaves;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.estie.arcanetweaks.util.AlexsCavesUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ACBiomeRegistry.class)
public class ACBiomeRegistryMixin {
    
    @Inject(method = "getBiomeAmbientLight", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectAmbientLight(Holder<Biome> biome, CallbackInfoReturnable<Float> cir) {
        Float override = AlexsCavesUtils.getBiomeOverride(biome, cfg -> cfg.ambientLight);
        if (override != null) cir.setReturnValue(override);
    }
    
    @Inject(method = "getBiomeFogNearness", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectFogNearness(Holder<Biome> biome, CallbackInfoReturnable<Float> cir) {
        Float override = AlexsCavesUtils.getBiomeOverride(biome, cfg -> cfg.fogNearness);
        if (override != null) cir.setReturnValue(override);
    }
    
    @Inject(method = "getBiomeWaterFogFarness", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectWaterFogFarness(Holder<Biome> biome, CallbackInfoReturnable<Float> cir) {
        Float override = AlexsCavesUtils.getBiomeOverride(biome, cfg -> cfg.waterFogFarness);
        if (override != null) cir.setReturnValue(override);
    }
    
    @Inject(method = "getBiomeSkyOverride", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectSkyOverride(Holder<Biome> biome, CallbackInfoReturnable<Float> cir) {
        Float override = AlexsCavesUtils.getBiomeOverride(biome, cfg -> cfg.skyOverride);
        if (override != null) cir.setReturnValue(override);
    }
    
    @Inject(method = "getBiomeLightColorOverride", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectLightColor(Holder<Biome> biome, CallbackInfoReturnable<Vec3> cir) {
        Vec3 override = AlexsCavesUtils.getBiomeOverride(biome, cfg -> cfg.lightColor);
        if (override != null) cir.setReturnValue(override);
    }
    
    @Inject(method = "getBiomeTabletColor", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectTabletColor(ResourceKey<Biome> biomeKey, CallbackInfoReturnable<Integer> cir) {
        Integer override = AlexsCavesUtils.getBiomeOverride(biomeKey, cfg -> cfg.tabletColor);
        if (override != null) cir.setReturnValue(override);
    }
}