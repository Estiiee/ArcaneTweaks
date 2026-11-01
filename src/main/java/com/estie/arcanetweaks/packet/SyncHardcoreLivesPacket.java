package com.estie.arcanetweaks.packet;

import com.estie.arcanetweaks.handlers.HardcoreHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncHardcoreLivesPacket {
    
    private final int lives;
    
    public SyncHardcoreLivesPacket(int lives) {
        this.lives = lives;
    }
    
    public static void encode(SyncHardcoreLivesPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.lives);
    }
    
    public static SyncHardcoreLivesPacket decode(FriendlyByteBuf buf) {
        return new SyncHardcoreLivesPacket(buf.readInt());
    }
    
    public static void handle(SyncHardcoreLivesPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            
            mc.player.getPersistentData().putInt(HardcoreHandler.HARDCORE_TAG, msg.lives);
        });
        
        ctx.get().setPacketHandled(true);
    }
}