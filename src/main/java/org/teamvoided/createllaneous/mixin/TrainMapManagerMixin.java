package org.teamvoided.createllaneous.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.simibubi.create.compat.trainmap.TrainMapManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.createllaneous.utils.TempConfigKt;

import static org.teamvoided.createllaneous.utils.TempConfigKt.*;

@Mixin(TrainMapManager.class)
public class TrainMapManagerMixin {
    @Inject(method = "redrawAll", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;<init>()V"))
    private static void xaerosMapTick(CallbackInfo ci, @Local(ordinal = 0) LocalIntRef mainColor, @Local(ordinal = 1) LocalIntRef darkerColor, @Local(ordinal = 2) LocalIntRef darkerColorShadow) {
        if (TempConfigKt.enableColorOverride) {
            mainColor.set(MAIN_COLOR.getRGB());
            darkerColor.set(DARKER_COLOR.getRGB());
            darkerColorShadow.set(DARKER_COLOR_SHADOW.getRGB());
        }
    }

    @ModifyConstant(method = "renderPhase", constant = @Constant(intValue = 0xFF_000000))
    private static int xaerosMapTick(int bgColor) {
        return OUTLINE_COLOR;
    }
}
