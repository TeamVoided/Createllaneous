package org.teamvoided.createllaneous.init

import com.simibubi.create.AllBlockEntityTypes
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import org.teamvoided.createllaneous.compat.CMMods
import org.teamvoided.createllaneous.compat.EnchantmentIndustry
import org.teamvoided.createllaneous.utils.registry.DOOR_BLOCKS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CMEvents {
    fun init() {
        MOD_BUS.addListener(::registerCapabilities)
        MOD_BUS.addListener(::addBlockEntities)
    }

    // region Register
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        if (CMMods.CREATE_ENCHANTMENT_INDUSTRY.isLoaded)
            EnchantmentIndustry.register(event)
    }
    // endregion

    fun addBlockEntities(event: BlockEntityTypeAddBlocksEvent) {
        event.modify(AllBlockEntityTypes.SLIDING_DOOR.get(), *DOOR_BLOCKS.map { it.get() }.toTypedArray())
    }
}

