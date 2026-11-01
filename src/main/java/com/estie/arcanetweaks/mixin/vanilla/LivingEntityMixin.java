package com.estie.arcanetweaks.mixin.vanilla;

import com.estie.arcanetweaks.Config;
import com.estie.arcanetweaks.interfaces.TickSpeedAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TickSpeedAccessor {
    
    @Unique
    private double arcanetweaks$tickSpeedAccumulator = 0;
    
    @Override
    public double arcanetweaks$getTickSpeedAccumulator() {
        return arcanetweaks$tickSpeedAccumulator;
    }
    
    @Override
    public void arcanetweaks$setTickSpeedAccumulator(double value) {
        arcanetweaks$tickSpeedAccumulator = value;
    }
    
    /*
    @Redirect(
            method = "getDamageAfterMagicAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(FF)F"
            )
    )
    private float redirectResCalc(float a, float b, DamageSource src, float pDamageAmount) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!self.hasEffect(MobEffects.DAMAGE_RESISTANCE)) return pDamageAmount;
        
        final double resPerLvl = Config.resistanceAmount; //% reduction per resistance level
        pDamageAmount -= (float) (pDamageAmount * (self.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * resPerLvl);
        return Math.max(pDamageAmount, 0.0F);
    }
     */
    
    /*
    //gl trying to find what breaks Math redirect in the pack
    @Redirect(
            method = "getDamageAfterMagicAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"
            )
    )
    private boolean disableVanillaResistance(LivingEntity self, MobEffect effect) {
        if (effect == MobEffects.DAMAGE_RESISTANCE) return false;
        return self.hasEffect(effect);
    }
   
   this too?? what the fuck did they do to that method
     */
    
    //2 can play at that game
    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("HEAD"), cancellable = true)
    private void cancelVanillaResistance(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        if (pDamageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            cir.setReturnValue(pDamageAmount);
            return;
        }
        
        LivingEntity self = (LivingEntity)(Object)this;
        if (!pDamageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            int k = EnchantmentHelper.getDamageProtection(self.getArmorSlots(), pDamageSource);
            if (k > 0) pDamageAmount = CombatRules.getDamageAfterMagicAbsorb(pDamageAmount, (float) k);
        }
        cir.setReturnValue(Math.max(pDamageAmount, 0.0F));
    }
    
    @Redirect(
            method = "hurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean redirectFireCheck(DamageSource source, TagKey<DamageType> tag) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (tag.equals(DamageTypeTags.IS_FIRE) && self.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            MobEffectInstance fireResistance = self.getEffect(MobEffects.FIRE_RESISTANCE);
            if (fireResistance != null) {
                float fireResAmount = (float) ((fireResistance.getAmplifier() + 1) * Config.fireResistanceAmount);
                if (fireResAmount >= 1.0F) return source.is(tag);
            }
            
            return false;
        }
        return source.is(tag);
    }
}