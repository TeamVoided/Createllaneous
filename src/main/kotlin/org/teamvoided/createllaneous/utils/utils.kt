package org.teamvoided.createllaneous.utils

import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.items.IItemHandler
import org.teamvoided.createllaneous.mixin.compat.BlazeForgerBlockEntityAccessor
import plus.dragons.createenchantmentindustry.common.processing.forger.BlazeForgerBlockEntity
import plus.dragons.createenchantmentindustry.common.registry.CEIBlockEntities.BLAZE_FORGER

fun registerCapabilities(event: RegisterCapabilitiesEvent) {
    event.registerBlockEntity(ItemHandler.BLOCK, BLAZE_FORGER.get()) { forger, side ->
        when (side) {
            Direction.EAST -> BlazeForgerItemHandler(forger, 0)
            Direction.WEST -> BlazeForgerItemHandler(forger, 1)
            else -> null
        }
    }
}

class BlazeForgerItemHandler(val blockEntity: BlazeForgerBlockEntity, val sideSlot: Int) : IItemHandler {
    private fun inv() = (blockEntity as BlazeForgerBlockEntityAccessor).cm_getInventory()
    override fun getSlots(): Int = 2
    override fun getStackInSlot(slot: Int): ItemStack = inv().getStackInSlot(slot)
    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (slot > 1) return stack
        if (slot != sideSlot) return stack

        return inv().insertItem(slot, stack, simulate)
    }
    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY
    override fun getSlotLimit(slot: Int): Int = inv().getSlotLimit(slot)
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true
}