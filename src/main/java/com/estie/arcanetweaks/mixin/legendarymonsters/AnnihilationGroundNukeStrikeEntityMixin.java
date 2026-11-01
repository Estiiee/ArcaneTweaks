package com.estie.arcanetweaks.mixin.legendarymonsters;

import com.estie.arcanetweaks.Config;
import net.miauczel.legendary_monsters.entity.AnimatedMonster.Projectile.AnnihilationGroundNukeStrikeEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnnihilationGroundNukeStrikeEntity.class)
public abstract class AnnihilationGroundNukeStrikeEntityMixin {
    
    @Redirect(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            remap = false
    )
    private boolean redirectHurt(LivingEntity target, DamageSource source, float amount) {
        float modified = (float) (amount + Config.obliteratorGroundNukeDamageFlat + (target.getMaxHealth() * Config.obliteratorGroundNukeDamagePercent));
        return target.hurt(source, modified);
    }
}