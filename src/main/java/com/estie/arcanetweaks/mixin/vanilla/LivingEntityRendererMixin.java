package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.client.renderer.layer.UniversalEntityLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addUniversalLayer(EntityRendererProvider.Context ctx, EntityModel<?> model, float shadowRadius, CallbackInfo ci) {
        LivingEntityRenderer<?, ?> renderer = (LivingEntityRenderer<?, ?>)(Object)this;
        renderer.addLayer(new UniversalEntityLayer(renderer));
    }
}