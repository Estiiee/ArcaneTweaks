package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import com.estie.arcanetweaks.interfaces.TickSpeedAccessor;
import com.estie.arcanetweaks.mobs.MobStats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//idea shamelessly copied from Enchant With Mob by baguchi. sowwy
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    
    @Shadow
    private void tickPassenger(Entity vehicle, Entity passenger) {}
    
    @Inject(
            method = "tickNonPassenger",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;tickCount:I",
                    opcode = Opcodes.PUTFIELD
            ),
            cancellable = true
    )
    private void modifyTickSpeed(Entity entity, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity living)) return;
        MobStats stats = Config.mobAttributeModifiers.get(living.getType());
        if (stats == null) return;
        
        double multiplier = stats.tick();
        if (multiplier == 1.0) return;
        
        TickSpeedAccessor accessor = (TickSpeedAccessor) living;
        double acc = accessor.arcanetweaks$getTickSpeedAccumulator();
        
        acc += multiplier;
        while (acc >= 1.0f) {
            entity.tickCount++;
            entity.tick();
            acc -= 1.0f;
        }
        
        accessor.arcanetweaks$setTickSpeedAccumulator(acc);
        
        for (Entity passenger : entity.getPassengers()) {
            this.tickPassenger(entity, passenger);
        }
        ci.cancel();
    }
}