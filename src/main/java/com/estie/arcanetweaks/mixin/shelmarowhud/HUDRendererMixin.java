package com.estie.arcanetweaks.mixin.shelmarowhud;

//obsolete now but piece of history

/*
@OnlyIn(Dist.CLIENT)
@Mixin(HUDRenderer.class)
public class HUDRendererMixin {

    @Shadow
    @Final
    private static Minecraft mc;

    @ModifyVariable(
            method = "renderSimpleType",
            at = @At(value = "STORE", ordinal = 2),
            name = "staminaPercent",
            remap = false
    )
    private static float overrideStaminaPercent(float original) {
        if (!ModList.get().isLoaded("parcool")) return original;

        IStamina s = mc.player.getCapability(Capabilities.STAMINA_CAPABILITY).orElse(null);
        if (s != null) return (float) s.get() / s.getActualMaxStamina();

        return original;
    }

    @ModifyVariable(
            method = "renderSimpleType",
            at = @At(value = "STORE", ordinal = 2),
            name = "totalStaminaWidth",
            remap = false
    )
    private static int overrideTotalStaminaWidth(int original) {
        if (!ModList.get().isLoaded("parcool")) return original;

        IStamina s = mc.player.getCapability(Capabilities.STAMINA_CAPABILITY).orElse(null);
        if (s != null) return Mth.clamp(Math.round((HUDConfig.PIX_PER_STAMINA.get()).floatValue() * s.getActualMaxStamina()), 6, HUDConfig.MAX_STAMINA_WIDTH.get());

        return original;
    }

    @ModifyVariable(
            method = "renderSoulLikeType",
            at = @At(value = "STORE", ordinal = 2),
            name = "staminaRatio",
            remap = false
    )
    private static float overrideStaminaRatio(float original) {
        if (!ModList.get().isLoaded("parcool")) return original;

        IStamina s = mc.player.getCapability(Capabilities.STAMINA_CAPABILITY).orElse(null);
        if (s != null) return (float) s.get() / s.getActualMaxStamina();

        return original;
    }

    @ModifyVariable(
            method = "renderSoulLikeType",
            at = @At(value = "STORE", ordinal = 2),
            name = "totalMaxStaminaPix",
            remap = false
    )
    private static int overrideTotalMaxStaminaPix(int original) {
        if (!ModList.get().isLoaded("parcool")) return original;

        IStamina s = mc.player.getCapability(Capabilities.STAMINA_CAPABILITY).orElse(null);
        if (s != null) return Mth.clamp(Math.round((HUDConfig.PIX_PER_STAMINA.get()).floatValue() * s.getActualMaxStamina()), 5, HUDConfig.MAX_STAMINA_WIDTH.get());

        return original;
    }
    
}

 */