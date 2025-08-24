package org.teamvoided.createllaneous.mixin.backport;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.trains.station.StationScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.createllaneous.compat.CMMods;

@Mixin(StationScreen.class)
public class StationScreenMixin {

    @ModifyReturnValue(method = "mapModsPresent", at = @At("RETURN"))
    boolean checkIfXaero(boolean original) {
        return original || CMMods.XAEROS_WORLD_MAP.isLoaded();
    }
}
