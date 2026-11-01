package com.estie.arcanetweaks.client.renderer.layer;

import com.estie.arcanetweaks.ArcaneTweaks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniversalEntityLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation FORCEFIELD = new ResourceLocation("minecraft", "textures/misc/forcefield.png");
    private static final ResourceLocation FORCEFIELD_ALT = new ResourceLocation(ArcaneTweaks.MODID, "textures/misc/forcefield_alt.png");
    
    public UniversalEntityLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        
        if (!entity.hasCustomName()) return;
        poseStack.pushPose();
        
        if (entity.getCustomName().getString().equalsIgnoreCase("estie")) renderRGBSkin(entity, poseStack, buffer, partialTicks, light);
        //if (entity.getCustomName().getString().equalsIgnoreCase("test")) renderForcefield(entity, poseStack, buffer, partialTicks, light);
        //if (entity.getCustomName().getString().equalsIgnoreCase("test_alt")) renderForcefieldAlt(entity, poseStack, buffer, partialTicks, light);
        
        poseStack.popPose();
    }
    
    private void renderRGBSkin(T entity, PoseStack poseStack, MultiBufferSource buffer,  float partialTicks, int light) {
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        
        float time = (entity.tickCount + partialTicks) / 20.0F;
        
        float r = 0.5F + 0.5F * (float)Math.sin(time);
        float g = 0.5F + 0.5F * (float)Math.sin(time + 2.0F);
        float b = 0.5F + 0.5F * (float)Math.sin(time + 4.0F);
        
        this.getParentModel().renderToBuffer(
                poseStack, vc, light,
                LivingEntityRenderer.getOverlayCoords(entity, 0),
                r, g, b, 0.5F
        );
    }
    
    /*
    private void renderForcefield(T entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int light) {
        
        float time = (entity.tickCount + partialTicks);
        
        float r = 0.3F;
        float g = 0;
        float b = 0;
        
        poseStack.pushPose();

        RenderType type = RenderType.energySwirl(FORCEFIELD, time * 0.06F, 0);
        VertexConsumer vc = buffer.getBuffer(type);
        
        this.getParentModel().renderToBuffer(
                poseStack, vc, light,
                LivingEntityRenderer.getOverlayCoords(entity, 0),
                r, g, b, 0.2F
        );
        
        poseStack.popPose();
    }
    
    private void renderForcefieldAlt(T entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int light) {
        
        float time = (entity.tickCount + partialTicks);
        
        float r = 0.3F;
        float g = 0;
        float b = 0;
        
        poseStack.pushPose();
        
        RenderType type = RenderType.entityTranslucent(FORCEFIELD_ALT);
        VertexConsumer vc = buffer.getBuffer(type);
        
        this.getParentModel().renderToBuffer(
                poseStack, vc, light,
                LivingEntityRenderer.getOverlayCoords(entity, 0),
                r, g, b, 1.0F
        );
        
        poseStack.popPose();
    }
     */
}