package org.teamvoided.createllaneous.mixin.extract;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.xaero_api.api.CustomMapNameRegistry;
import xaero.common.misc.Misc;


@Pseudo
@Mixin(Misc.class)
public class XaeroMiscMixin {
    @ModifyReturnValue(method = "getFixedDisplayName", at = @At("RETURN"), require = 0)
    private static Component customTrainNames(Component original, @Local(argsOnly = true) Entity entity) {
        for (CustomMapNameRegistry.MapNameProvider provider : CustomMapNameRegistry.MAP_NAME_PROVIDERS.values()) {
            var name = provider.getMapName(original, entity);
            if (name != original) {
                return name;
            }
        }
        return original;
    }
}
