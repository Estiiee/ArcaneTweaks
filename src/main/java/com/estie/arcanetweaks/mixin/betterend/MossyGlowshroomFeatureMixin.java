package com.estie.arcanetweaks.mixin.betterend;

import com.estie.arcanetweaks.util.BetterEndUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.features.trees.MossyGlowshroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MossyGlowshroomFeature.class)
public abstract class MossyGlowshroomFeatureMixin {
    
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
            )
    )
    private boolean arcane$allowMoreGroundsBlock(BlockState state, Block block) {
        return BetterEndUtils.isProperSurfaceOrBlock(EndBlocks.MOSSY_GLOWSHROOM_SAPLING, state, block);
    }
    
}