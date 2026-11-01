package com.estie.arcanetweaks.mixin.lostcities;

import com.estie.arcanetweaks.Config;
import mcjty.lostcities.worldgen.lost.BuildingInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BuildingInfo.class)
public abstract class BuildingInfoMixin {
    
    @Inject(method = "getRandomDoor", at = @At("HEAD"), cancellable = true, remap = false)
    private void getRandomDoorCustom(Random rand, CallbackInfoReturnable<Block> cir) {
        if (!Config.lostCitiesDoors.isEmpty()) {
            String id = Config.lostCitiesDoors.get(rand.nextInt(Config.lostCitiesDoors.size()));
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
            
            if (block != null && block != Blocks.AIR) cir.setReturnValue(block);
        }
    }
}