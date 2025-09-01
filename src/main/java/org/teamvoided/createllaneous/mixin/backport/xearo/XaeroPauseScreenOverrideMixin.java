package org.teamvoided.createllaneous.mixin.backport.xearo;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.createllaneous.backport.XaeroTrainMap;
import org.teamvoided.createllaneous.compat.CMMods;

@Mixin(Screen.class)
public class XaeroPauseScreenOverrideMixin {

    @Inject(method = "isPauseScreen", at = @At("HEAD"), cancellable = true)
    public void create$xaeroPauseScreenOverride(CallbackInfoReturnable<Boolean> cir) {
        if (CMMods.XAEROS_WORLD_MAP.isLoaded()) {
            if (XaeroTrainMap.isMapOpen((Screen) (Object) this))
                cir.setReturnValue(false);
        }
    }
}
