package com.estie.arcanetweaks.compat.goety.ritualtype;

import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.Polarice3.Goety.common.ritual.type.ExpertNetherRitualType;
import com.estie.arcanetweaks.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ExpertNetherCustomRitualType extends ExpertNetherRitualType {
    
    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        if (pLevel.dimension() != Level.OVERWORLD || !pLevel.getBiome(pPos).is(Config.ritualExpertNetherBiome)) {
            if (pPlayer != null) {
                pPlayer.displayClientMessage(Component.translatable("info.goety.ritual.structure.nether"), true);
            }
            
            return false;
        } else {
            return RitualRequirements.getStructures(this.getName(), pPlayer, pPos, pLevel);
        }
    }
}