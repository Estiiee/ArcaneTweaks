package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.handlers.HardcoreHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    
    @Redirect(
            method = "handleClientCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;setGameMode(Lnet/minecraft/world/level/GameType;)Z"
            )
    )
    private boolean preventSpectatorHardcore(ServerPlayer player, GameType gameType) {
        if (gameType == GameType.SPECTATOR) {
            
            int lives = player.getPersistentData().getInt(HardcoreHandler.HARDCORE_TAG);
            
            if (lives > 0) return false;
        }

        return player.setGameMode(gameType);
    }
}