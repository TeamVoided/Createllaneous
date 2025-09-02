package org.teamvoided.createllaneous.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xaero.common.misc.Misc;


@Mixin(Misc.class)
public class XaeroMiscMixin {
    @ModifyReturnValue(method = "getFixedDisplayName", at = @At("RETURN"), require = 0)
    private static Component customTrainNames(Component original, @Local(argsOnly = true) Entity e) {
        if (e instanceof CarriageContraptionEntity entity) {
            var train = Create.RAILWAYS.sided(e.level()).trains.get(entity.trainId);
            return train.name;
        }
        return original;
    }
}
