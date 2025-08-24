package org.teamvoided.createllaneous.mixin.backport;

import com.simibubi.create.compat.trainmap.TrainMapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.createllaneous.backport.XaeroTrainMap;
import org.teamvoided.createllaneous.compat.CMMods;

import static com.simibubi.create.compat.trainmap.TrainMapManager.tick;

@Mixin(TrainMapManager.class)
public class TrainMapManagerMixin {
    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    private static void xaerosMapTick(CallbackInfo ci) {
        ResourceKey<Level> playerDimension = Minecraft.getInstance().level.dimension();
        if (CMMods.XAEROS_WORLD_MAP.isLoaded() && XaeroTrainMap.isMapOpen(Minecraft.getInstance().screen)) {
            ResourceKey<Level> renderedDimension = XaeroTrainMap.getRenderedDimension();
            tick(renderedDimension != null ? renderedDimension : playerDimension);
            ci.cancel();
        }
    }
}
