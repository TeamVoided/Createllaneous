package org.teamvoided.createllaneous.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import plus.dragons.createenchantmentindustry.common.processing.forger.BlazeForgerBlockEntity;
import plus.dragons.createenchantmentindustry.common.processing.forger.BlazeForgerInventory;

@Mixin(BlazeForgerBlockEntity.class)
public interface BlazeForgerBlockEntityAccessor {

    @Accessor("inventory")
    BlazeForgerInventory cm_getInventory();

}
