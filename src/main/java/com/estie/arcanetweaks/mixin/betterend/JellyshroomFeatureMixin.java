package com.estie.arcanetweaks.mixin.betterend;

import com.estie.arcanetweaks.util.BetterEndUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.features.trees.JellyshroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JellyshroomFeature.class)
public abstract class JellyshroomFeatureMixin {
    
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean arcane$allowMoreGrounds(BlockState state, TagKey<Block> tag) {
        return BetterEndUtils.isProperSurfaceOrTag(EndBlocks.SMALL_JELLYSHROOM, state, tag);
    }
}