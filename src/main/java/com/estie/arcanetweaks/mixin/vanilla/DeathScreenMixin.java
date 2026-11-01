package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import com.estie.arcanetweaks.handlers.HardcoreHandler;
import com.estie.arcanetweaks.util.SpecialDeathMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin {
    
    @Shadow
    private boolean hardcore;
    
    @Unique
    private String arcanetweaks$selectedDeathMessage;
    
    @ModifyVariable(method = "init", at = @At(value = "STORE"), ordinal = 0)
    private Component replaceSpectateText(Component original) {
        if (!hardcore) return original;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return original;
        
        int lives = mc.player.getPersistentData().getInt(HardcoreHandler.HARDCORE_TAG);
        if (lives <= 0) return original;
        
        Component livesStyled = Component.literal(String.valueOf(lives)).withStyle(style -> style.withColor(0xFF5555));
        return Component.translatable("deathScreen.respawnLives", livesStyled);
    }
    
    @Inject(method = "init", at = @At("TAIL"))
    private void selectDeathMessage(CallbackInfo ci) {
        List<String> messages;
        LocalPlayer player = Minecraft.getInstance().player;
        
        if (player != null && SpecialDeathMessages.DEATH_MESSAGES.containsKey(player.getUUID())) messages = SpecialDeathMessages.DEATH_MESSAGES.get(player.getUUID());
        else messages = Config.deathMessages;
        
        if (!messages.isEmpty()) {
            Random rand = new Random();
            arcanetweaks$selectedDeathMessage = messages.get(rand.nextInt(messages.size()));
        }
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void extraDeathText(GuiGraphics gfx, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (arcanetweaks$selectedDeathMessage == null) return;
        
        Minecraft mc = Minecraft.getInstance();
        DeathScreen deathScreen = (DeathScreen)(Object)this;
        
        gfx.drawCenteredString(
                mc.font,
                Component.literal(arcanetweaks$selectedDeathMessage),
                deathScreen.width / 2,
                115,
                0xFFFFFF
        );
    }
}