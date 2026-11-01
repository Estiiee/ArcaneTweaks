package com.livajq.arcanetweaks.mixin.iceandfire;

import com.estie.arcanecontent.ArcaneContent;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.MushroomCloudModel;
import com.github.alexmodguy.alexscaves.client.particle.MushroomCloudParticle;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.livajq.arcanetweaks.bossbehavior.BossBehaviorRegistry;
import com.livajq.arcanetweaks.bossbehavior.DragonBehavior;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(MushroomCloudParticle.class)
public abstract class MushroomCloudParticleMixin {
    
    @Unique
    private static final ResourceLocation arcanetweaks$texture = new ResourceLocation(ArcaneContent.MODID, "textures/particle/mushroom_cloud_dragons.png");
    
    @Unique
    private Color arcanetweaks$color = null;
    
    @Unique
    private boolean arcanetweaks$dragonNuke = false;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/github/alexmodguy/alexscaves/client/model/MushroomCloudModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
            )
    )
    private void tintModel(MushroomCloudModel model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a) {
        if (arcanetweaks$color == null) {
            EntityDragonBase dragon = getFirstValidDragon(128);
            
            if (dragon != null) {
                DragonBehavior<EntityDragonBase> behavior = (DragonBehavior<EntityDragonBase>) BossBehaviorRegistry.get(dragon);
                if (behavior != null) {
                    arcanetweaks$color = behavior.getExplosionColor();
                    arcanetweaks$dragonNuke = true;
                }
                
            }
        }
        
        if (!arcanetweaks$dragonNuke || arcanetweaks$color == null) {
            model.renderToBuffer(poseStack, consumer, light, overlay, r, g, b, a);
            return;
        }
        
        float newR = arcanetweaks$color.getRed() / 255.0F;
        float newG = arcanetweaks$color.getGreen() / 255.0F;
        float newB = arcanetweaks$color.getBlue() / 255.0F;
        
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer newConsumer = buffers.getBuffer(RenderType.entityTranslucent(arcanetweaks$texture));
        
        model.renderToBuffer(poseStack, newConsumer, light, overlay, newR, newG, newB, a);
    }
    
    @Redirect(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/github/alexmodguy/alexscaves/client/ClientProxy;renderNukeFlashFor:I",
                    opcode = Opcodes.PUTSTATIC,
                    ordinal = 0
            )
    )
    private void arcanetweaks$disableFlashForDreg(int frames) {
        EntityDragonBase dragon = getFirstValidDragon(128);
        ClientProxy.renderNukeFlashFor = dragon != null ? 0 : frames;
    }
    
    @Unique
    private EntityDragonBase getFirstValidDragon(double radius) {
        MushroomCloudParticle self = (MushroomCloudParticle) (Object) this;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return null;
        
        List<EntityDragonBase> dragons =
                level.getEntitiesOfClass(EntityDragonBase.class, self.getBoundingBox().inflate(radius), d -> d.isAlive()
                        && d.getHealth() > 0
                        && d.getDragonStage() == 5
                        && !d.isTame()
                        && d.getHealth() <= d.getMaxHealth() * 0.6
                );
        
        return dragons.stream().findFirst().orElse(null);
    }
}