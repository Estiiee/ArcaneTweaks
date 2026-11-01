package com.estie.arcanetweaks.mixin.dldungeonsjbg;

import com.estie.arcanetweaks.handlers.DLDungeonsHandler;
import jaredbgreat.dldungeons.themes.Theme;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Theme.class)
public class ThemeMixin {
    
    @Inject(method = "getOverworldTheme", at = @At("RETURN"), cancellable = true, remap = false)
    private static void chooseThemeForBiome(RandomSource random, Holder<Biome> biome, CallbackInfoReturnable<Theme> cir) {
       
        if (biome.is(BiomeTags.IS_NETHER) || biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN)) return;
        
        Theme filtered = DLDungeonsHandler.pickTheme(biome, random);
        if(filtered == null) return;
        cir.setReturnValue(filtered);
    }
}