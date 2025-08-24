package org.teamvoided.createllaneous

import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.init.CMItems
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.teamvoided.createllaneous.data.gen.gatherData
import org.teamvoided.createllaneous.init.CMBlockEntityTypes
import org.teamvoided.createllaneous.init.CMTabs
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Createllaneous.MODID)
object Createllaneous {
    const val MODID = "createllaneous"
    val LOGGER: Logger = LogManager.getLogger(MODID)

    init {
        CMBlocks.BLOCKS.register(MOD_BUS)
        CMBlockEntityTypes.BLOCK_ENTITY_TYPES.register(MOD_BUS)
        CMItems.ITEMS.register(MOD_BUS)
        CMTabs.TABS.register(MOD_BUS)

        MOD_BUS.addListener(::onCommonSetup)
        MOD_BUS.addListener(::onServerSetup)
        MOD_BUS.addListener(::gatherData)

    }

    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.log(Level.INFO, "Hello! This is working!")
    }
}