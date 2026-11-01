package com.estie.arcanetweaks.mixin.bclib;

import com.estie.arcanetweaks.util.BetterEndUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.betterx.bclib.blocks.UnderwaterPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UnderwaterPlantBlock.class)
public abstract class UnderwaterPlantBlockMixin {
    
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true, remap = false)
    private void surviveonMoreSurfaces(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Block plant = state.getBlock();
        BlockState down = world.getBlockState(pos.below());
        
        if (BetterEndUtils.isProperSurface(plant, down, true) && state.getFluidState().is(Fluids.WATER)) cir.setReturnValue(true);
    }
}