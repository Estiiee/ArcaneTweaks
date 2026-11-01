package com.estie.arcanetweaks.mixin.betterend;

import com.estie.arcanetweaks.util.BetterEndUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.features.trees.PythadendronTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PythadendronTreeFeature.class)
public abstract class PythadendronTreeFeatureMixin {
    
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
            )
    )
    private Block arcane$allowMoreGrounds(BlockState state) {
        Block real = state.getBlock();
        if (BetterEndUtils.isProperSurfaceOrBlock(EndBlocks.PYTHADENDRON_SAPLING, state, real)) return EndBlocks.CHORUS_NYLIUM;
        return real;
    }
    //lmao
}