package com.estie.arcanetweaks.mixin.emi;

import com.estie.arcanetweaks.Config;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(EmiRecipes.class)
public class EmiRecipesMixin {

    @Inject(method = "addRecipe", at = @At("HEAD"), cancellable = true, remap = false)
    private static void filterRecipes(EmiRecipe recipe, CallbackInfo ci) {
        ResourceLocation recipeId = recipe.getId();
        if (recipeId == null) return;
        String idString = recipeId.toString();
        
        if (Config.emiRecipeWhitelistSet.stream().anyMatch(idString::contains)) return;
        
        EmiRecipeCategory cat = recipe.getCategory();
        if (cat == null) return;
        ResourceLocation catId = cat.getId();
        if (catId == null) return;
        
        if (Config.emiRecipeCategoryBlacklistSet.contains(catId.toString())) ci.cancel();
    }
}