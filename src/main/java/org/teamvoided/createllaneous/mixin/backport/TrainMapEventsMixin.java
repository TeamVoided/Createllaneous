package org.teamvoided.createllaneous.mixin.backport;

import com.simibubi.create.compat.trainmap.TrainMapEvents;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.createllaneous.backport.XaeroTrainMap;
import org.teamvoided.createllaneous.compat.CMMods;

@Mixin(value = TrainMapEvents.class, remap = false)
public class TrainMapEventsMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private static void extraTick(ClientTickEvent.Post event, CallbackInfo ci) {
        if (CMMods.XAEROS_WORLD_MAP.isLoaded())
            XaeroTrainMap.tick();
    }

    @Inject(method = "mouseClick", at = @At("TAIL"))
    private static void mouseClick(InputEvent.MouseButton.Pre event, CallbackInfo ci) {
        if (CMMods.XAEROS_WORLD_MAP.isLoaded())
            XaeroTrainMap.mouseClick(event);
    }
}
