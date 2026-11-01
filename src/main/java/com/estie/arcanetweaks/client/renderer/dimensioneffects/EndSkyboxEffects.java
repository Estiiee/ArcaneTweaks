package com.estie.arcanetweaks.client.renderer.dimensioneffects;

import com.estie.arcanetweaks.ArcaneTweaks;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class EndSkyboxEffects extends DimensionSpecialEffects.EndEffects {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ArcaneTweaks.MODID, "textures/environment/end_sky_swap.png");
    
    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack pPoseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        
        Matrix4f mat = pPoseStack.last().pose();
        
        int slices = 48;
        int stacks = 48;
        float radius = 100f;
        
        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR);
        
        for (int i = 0; i < stacks; i++) {
            float v1 = (float) i / stacks;
            float v2 = (float) (i + 1) / stacks;
            
            float phi1 = v1 * (float) Math.PI;
            float phi2 = v2 * (float) Math.PI;
            
            for (int j = 0; j <= slices; j++) {
                float u = (float) j / slices;
                float theta = u * (float) Math.PI * 2f;
                
                float x1 = radius * Mth.cos(theta) * Mth.sin(phi1);
                float y1 = radius * Mth.cos(phi1);
                float z1 = radius * Mth.sin(theta) * Mth.sin(phi1);
                
                float x2 = radius * Mth.cos(theta) * Mth.sin(phi2);
                float y2 = radius * Mth.cos(phi2);
                float z2 = radius * Mth.sin(theta) * Mth.sin(phi2);
                
                buf.vertex(mat, x1, y1, z1).uv(u, v1).color(255, 255, 255, 255).endVertex();
                buf.vertex(mat, x2, y2, z2).uv(u, v2).color(255, 255, 255, 255).endVertex();
            }
        }
        
        tess.end();
        
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        
        return true;
    }
}