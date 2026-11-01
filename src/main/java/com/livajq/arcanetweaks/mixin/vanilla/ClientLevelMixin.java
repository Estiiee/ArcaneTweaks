package com.livajq.arcanetweaks.mixin.vanilla;

import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.interfaces.TickSpeedAccessor;
import com.livajq.arcanetweaks.mobs.MobStats;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Shadow
    private void tickPassenger(Entity vehicle, Entity passenger) {}
    
    @Unique
    private static final ResourceKey<Biome> DEFILED_PLAINS = ResourceKey.create(Registries.BIOME, new ResourceLocation("defiled_lands_preborn", "defiled_plains"));
    
    @Unique
    private static float arcanetweaks$darkness;
    
    @Inject(
            method = "tickNonPassenger",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;tickCount:I",
                    opcode = Opcodes.PUTFIELD
            ),
            cancellable = true
    )
    private void modifyClientTickSpeed(Entity entity, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity living)) return;
        MobStats stats = Config.mobAttributeModifiers.get(living.getType());
        if (stats == null) return;
        
        double multiplier = stats.tick();
        if (multiplier == 1.0) return;
        
        TickSpeedAccessor accessor = (TickSpeedAccessor) living;
        double acc = accessor.arcanetweaks$getTickSpeedAccumulator();
        
        //changing the client tick the same way as server causes stuttering in movement in animations as the game is
        //burning through lerp steps faster than anticipated so it needs to be ensured it happens only during the base ticks
        acc += multiplier;
        boolean first = true;
        int steps = 0, headSteps = 0;
        LivingEntityAccessor lerp = (LivingEntityAccessor) living;
        
        while (acc >= 1.0) {
            entity.tickCount++;
            //extra ticks: no position easing
            if (!first) {
                lerp.arcanetweaks$setLerpSteps(0);
                lerp.arcanetweaks$setLerpHeadSteps(0);
            }
            entity.tick();
           
            //remember what the first tick left
            if (first) {
                steps = lerp.arcanetweaks$getLerpSteps();
                headSteps = lerp.arcanetweaks$getLerpHeadSteps();
            } else {
                //put it back after each extra tick
                lerp.arcanetweaks$setLerpSteps(steps);
                lerp.arcanetweaks$setLerpHeadSteps(headSteps);
            }
            
            first = false;
            acc -= 1.0;
        }
        
        accessor.arcanetweaks$setTickSpeedAccumulator(acc);
        
        for (Entity passenger : entity.getPassengers()) {
            this.tickPassenger(entity, passenger);
        }

        ci.cancel();
    }
    
    @Inject(method = "getCloudColor", at = @At("RETURN"), cancellable = true)
    private void biomeClouds(float partialTick, CallbackInfoReturnable<Vec3> cir) {
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (!cam.isInitialized()) return;
      
        ClientLevel self = (ClientLevel) (Object) this;
        boolean inBiome = self.getBiome(cam.getBlockPosition()).is(DEFILED_PLAINS);
        
        arcanetweaks$darkness = Mth.lerp(0.02F, arcanetweaks$darkness, inBiome ? 1.0F : 0.0F);
        if (Math.abs((inBiome ? 1.0F : 0.0F) - arcanetweaks$darkness) < 0.001F) arcanetweaks$darkness = inBiome ? 1.0F : 0.0F;
        
        if (arcanetweaks$darkness > 0.001F) cir.setReturnValue(cir.getReturnValue().scale(1.0 - arcanetweaks$darkness));
    }
}