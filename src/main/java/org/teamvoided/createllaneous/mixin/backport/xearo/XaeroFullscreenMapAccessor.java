package org.teamvoided.createllaneous.mixin.backport.xearo;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import xaero.map.MapProcessor;
import xaero.map.gui.GuiMap;

@Mixin(GuiMap.class)
public interface XaeroFullscreenMapAccessor {
    @Accessor(value = "cameraX", remap = false)
    double getCameraX();

    @Accessor(value = "cameraZ", remap = false)
    double getCameraZ();

    @Accessor(value = "scale", remap = false)
    double getScale();

    @Accessor(value = "mapProcessor", remap = false)
    MapProcessor cm_getMapProcessor();
}
