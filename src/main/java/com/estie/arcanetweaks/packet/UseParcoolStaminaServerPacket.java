package com.estie.arcanetweaks.packet;

import com.alrex.parcool.common.capability.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UseParcoolStaminaServerPacket {
    private final int amount;
    
    public UseParcoolStaminaServerPacket(int amount) {
        this.amount = amount;
    }
   
    public static void encode(UseParcoolStaminaServerPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.amount);
    }
    
    public static UseParcoolStaminaServerPacket decode(FriendlyByteBuf buf) {
        return new UseParcoolStaminaServerPacket(buf.readInt());
    }
    
    public static void handle(UseParcoolStaminaServerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            
            mc.player.getCapability(Capabilities.STAMINA_CAPABILITY).ifPresent(stamina -> stamina.consume(msg.amount));
        });
        
        ctx.get().setPacketHandled(true);
    }
}
