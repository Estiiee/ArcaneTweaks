package com.estie.arcanetweaks.mixin.betterend;

import com.estie.arcanetweaks.util.BetterEndUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.features.trees.HelixTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HelixTreeFeature.class)
public abstract class HelixTreeFeatureMixin {
    
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean arcane$allowMoreGrounds(BlockState state, TagKey<Block> tag) {
        return BetterEndUtils.isProperSurfaceOrTag(EndBlocks.HELIX_TREE_SAPLING, state, tag);
    }
}