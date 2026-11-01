package com.estie.arcanetweaks.mixin.vanilla;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {
    
    //so supposedly the crash happens when Dungeon Now Loading spawns a mob on worker thread via Feature and then spartan mixin tries to randomize mob equipment?
    //it doesn't help that it happened once in 1000 hours and I cannot replicate the crash but maybeee this helps
    //attempt 2 at fixing the crash I cannot even test
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void skipOnWorldgenThread(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        Mob self = (Mob)(Object)this;
        Level level = self.level();
    
        if (!self.isAddedToWorld()) {
            ci.cancel();
            return;
        }

        String threadName = Thread.currentThread().getName();
        if (threadName.contains("Worker")
                || threadName.contains("ForkJoin")
                || threadName.contains("Chunk")
                || threadName.contains("WorldGen")
                || threadName.contains("pool")) {
            ci.cancel();
            return;
        }
        
        if (!level.isClientSide && level.getServer() != null && !level.getServer().isSameThread()) ci.cancel();
    }
}