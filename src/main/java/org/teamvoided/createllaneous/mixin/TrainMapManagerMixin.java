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

import static org.teamvoided.createllaneous.client.CreatellaneousClient.clientConfig;

@Mixin(TrainMapManager.class)
public class TrainMapManagerMixin {
    @Inject(method = "redrawAll", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;<init>()V"))
    private static void xaerosMapTick(CallbackInfo ci, @Local(name = "mainColor") LocalIntRef mainColor, @Local(name = "darkerColor") LocalIntRef darkerColor, @Local(name = "darkerColorShadow") LocalIntRef darkerColorShadow) {
        if (clientConfig.enableCustomTrainMapColors) {
            mainColor.set(clientConfig.mainColor.toInt());
            darkerColor.set(clientConfig.darkerColor.toInt());
            darkerColorShadow.set(clientConfig.darkerColorShadow.toInt());
        }
    }

    @ModifyConstant(method = "renderPhase", constant = @Constant(intValue = 0xFF_000000))
    private static int xaerosMapTick(int bgColor) {
        return (clientConfig.enableCustomTrainMapColors) ? clientConfig.outlineColor.toInt() : bgColor;
    }
}
