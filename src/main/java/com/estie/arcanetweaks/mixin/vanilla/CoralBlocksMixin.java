package com.estie.arcanetweaks.mixin.vanilla;

import cn.leolezury.eternalstarlight.common.data.ESDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.CoralPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin({CoralPlantBlock.class, CoralBlock.class})
public class CoralBlocksMixin {
    
    @Unique
    private static final Set<ResourceLocation> IMMORTAL_CORAL_BLOCKS = Set.of(
            new ResourceLocation("minecraft:brain_coral"),
            new ResourceLocation("minecraft:brain_coral_block"),
            new ResourceLocation("minecraft:tube_coral"),
            new ResourceLocation("minecraft:tube_coral_block"),
            new ResourceLocation("minecraft:bubble_coral"),
            new ResourceLocation("minecraft:bubble_coral_block"),
            new ResourceLocation("minecraft:fire_coral"),
            new ResourceLocation("minecraft:fire_coral_block"),
            new ResourceLocation("minecraft:horn_coral"),
            new ResourceLocation("minecraft:horn_coral_block"),
            new ResourceLocation("eternal_starlight:tentacles_coral"),
            new ResourceLocation("eternal_starlight:tentacles_coral_block"),
            new ResourceLocation("eternal_starlight:golden_coral"),
            new ResourceLocation("eternal_starlight:golden_coral_block"),
            new ResourceLocation("eternal_starlight:crystallum_coral"),
            new ResourceLocation("eternal_starlight:crystallum_coral_block")
    );
    
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
       if (pLevel.dimension() == ESDimensions.STARLIGHT_KEY && IMMORTAL_CORAL_BLOCKS.contains(ForgeRegistries.BLOCKS.getKey(pState.getBlock()))) ci.cancel();
    }
}