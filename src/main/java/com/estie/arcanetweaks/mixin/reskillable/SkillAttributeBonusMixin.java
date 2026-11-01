package com.estie.arcanetweaks.mixin.reskillable;

import com.estie.arcanetweaks.Config;
import net.bandit.reskillable.common.commands.skills.SkillAttributeBonus;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(SkillAttributeBonus.class)
public class SkillAttributeBonusMixin {
    
    @Inject(method = "getAttribute", at = @At("HEAD"), cancellable = true, remap = false)
    private void overrideAttribute(CallbackInfoReturnable<Attribute> cir) {
        SkillAttributeBonus self = (SkillAttributeBonus) (Object) this;
        
        Supplier<Attribute> override = Config.reskillableAttributeBonuses.get(self);
        if (override != null) cir.setReturnValue(override.get());
    }
}