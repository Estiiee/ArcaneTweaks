package com.estie.arcanetweaks.handlers;

import com.estie.arcanetweaks.packet.StartSillyRainbowEffectPacket;
import com.estie.arcanetweaks.packet.SyncHardcoreLivesPacket;
import com.estie.arcanetweaks.packet.UseParcoolStaminaServerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;
    
    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("arcanetweaks", "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );
        
        int id = 0;
        
        INSTANCE.registerMessage(id++, SyncHardcoreLivesPacket.class, SyncHardcoreLivesPacket::encode, SyncHardcoreLivesPacket::decode, SyncHardcoreLivesPacket::handle);
        INSTANCE.registerMessage(id++, UseParcoolStaminaServerPacket.class, UseParcoolStaminaServerPacket::encode, UseParcoolStaminaServerPacket::decode, UseParcoolStaminaServerPacket::handle);
        INSTANCE.registerMessage(id++, StartSillyRainbowEffectPacket.class, StartSillyRainbowEffectPacket::encode, StartSillyRainbowEffectPacket::decode, StartSillyRainbowEffectPacket::handle);
    }
}