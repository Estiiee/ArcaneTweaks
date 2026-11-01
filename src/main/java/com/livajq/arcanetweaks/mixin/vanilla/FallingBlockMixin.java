package com.livajq.arcanetweaks.mixin.vanilla;

import com.livajq.arcanetweaks.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {
    
    //not the most ideal way to do it but at least as of now it works for all needed blocks
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void arcane$noFall(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (Config.noGravityBlocks.contains(ForgeRegistries.BLOCKS.getKey(state.getBlock()))) ci.cancel();
    }
}