package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void arcane$extraPlantSurfaces(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        
        BlockState state = (BlockState)(Object)this;
        Block plant = state.getBlock();
        
        ResourceLocation plantId = ForgeRegistries.BLOCKS.getKey(plant);
        if (plantId == null) return;
        
        BlockState ground = level.getBlockState(pos.below());
        ResourceLocation groundId = ForgeRegistries.BLOCKS.getKey(ground.getBlock());
        if (groundId == null) return;
        
        boolean inWater = level.getFluidState(pos).is(FluidTags.WATER);
        
        Set<ResourceLocation> allowed;
        
        if (inWater) allowed = Config.extraPlantSurfacesWater.get(plantId);
        else allowed = Config.extraPlantSurfaces.get(plantId);
        
        if (allowed == null) return;
        
        if (allowed.contains(groundId)) cir.setReturnValue(true);
    }
}