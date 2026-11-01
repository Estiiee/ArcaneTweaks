package com.livajq.arcanetweaks.mixin.vanilla;

import com.estie.arcanecontent.world.district.DistrictBiomeSource;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class MixinChunkGenerator {
    
    //flimsy fix for modded biomes disappearing on world reload since terrablender and apparently everyone else only look for vanilla biome source (ofc)
    @Inject(method = "<init>", at = @At("RETURN"))
    private void unwrapDistrictOnConstruct(BiomeSource biomeSource, CallbackInfo ci) {
        if (biomeSource instanceof DistrictBiomeSource district) ((ChunkGeneratorAccessor) this).setBiomeSource(district.getDelegate());
    }
}