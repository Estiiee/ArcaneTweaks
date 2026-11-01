package com.estie.arcanetweaks.handlers;

import com.estie.arcanetweaks.ArcaneTweaks;
import com.estie.arcanetweaks.Config;
import com.estie.arcanetweaks.packet.SyncHardcoreLivesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class HardcoreHandler {
    public static final String HARDCORE_TAG = "Arcane_HardcoreLives";
    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
    
    @SubscribeEvent
    public static void onPlayerLogin(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player) || event.phase == TickEvent.Phase.END) return;
        if (player.tickCount != 1) return;
        if (!player.level().getLevelData().isHardcore()) return;
        
        CompoundTag data = player.getPersistentData();
        if (!data.contains(HARDCORE_TAG)) data.putInt(HARDCORE_TAG, Config.hardcoreLivesCount);
        int lives = data.getInt(HARDCORE_TAG);
        
        PacketHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncHardcoreLivesPacket(lives)
        );
    }
    
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
       LivingEntity entity = event.getEntity();
       if (!(entity instanceof ServerPlayer player)) return;
       if (!(player.level() instanceof ServerLevel level) || !level.getLevelData().isHardcore()) return;
       
       CompoundTag data = player.getPersistentData();
       if (!data.contains(HARDCORE_TAG)) data.putInt(HARDCORE_TAG, Config.hardcoreLivesCount);
       int lives = data.getInt(HARDCORE_TAG);
        
        if (lives > 0) {
            lives--;
            data.putInt(HARDCORE_TAG, lives);
            
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SyncHardcoreLivesPacket(lives));
        }
    }
    
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer newPlayer)) return;
        if (!(event.getOriginal() instanceof ServerPlayer oldPlayer)) return;
        
        CompoundTag oldData = oldPlayer.getPersistentData();
        CompoundTag newData = newPlayer.getPersistentData();
        
        if (oldData.contains(HARDCORE_TAG))
            newData.putInt(HARDCORE_TAG, oldData.getInt(HARDCORE_TAG));
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (!Config.hardcoreIconVisible) return;
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.level() == null) return;
        if (!mc.player.level().getLevelData().isHardcore()) return;
        
        GuiGraphics gfx = event.getGuiGraphics();
        int lives = mc.player.getPersistentData().getInt(HardcoreHandler.HARDCORE_TAG);
        if (lives < 0) return;
        
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        double xPercent = Config.hardcoreIconPosX / 100.0;
        double yPercent = Config.hardcoreIconPosY / 100.0;
        
        int x = (int) (screenWidth * xPercent);
        int y = (int) (screenHeight * yPercent);
        
        int size = Config.hardcoreIconSize;
        int maxLives = Config.hardcoreLivesCount;
        
        double percent = (lives / (double) maxLives) * 100.0;
        int iconTier;
        
        if (lives == 1) iconTier = 1;
        else {
            if (percent <= 20) iconTier = 1;
            else if (percent <= 40) iconTier = 2;
            else if (percent <= 60) iconTier = 3;
            else if (percent <= 80) iconTier = 4;
            else iconTier = 5;
        }
        
        int u, v;
        switch (iconTier) {
            case 5 -> { u = 34; v = 9; }
            case 4 -> { u = 16; v = 0; }
            case 3 -> { u = 34; v = 0; }
            case 2 -> { u = 52; v = 0; }
            case 1 -> { u = 34; v = 9; }
            default -> { u = 16; v = 0; }
        }

        gfx.blit(ICONS, x, y, size, size, u, v, 9, 9, 256, 256);
    }
}