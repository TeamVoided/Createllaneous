package org.teamvoided.createllaneous

import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.teamvoided.createllaneous.data.gen.datagen
import org.teamvoided.createllaneous.init.*
import org.teamvoided.createllaneous.utils.CMConfig
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Createllaneous.MODID)
object Createllaneous {
    const val MODID = "createllaneous"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmField
    val config = CMConfig()

    init {
        CMBlocks.BLOCKS.register(MOD_BUS)
        CMBlockEntityTypes.BLOCK_ENTITY_TYPES.register(MOD_BUS)
        CMItems.ITEMS.register(MOD_BUS)
        CMTabs.TABS.register(MOD_BUS)
        CMItemAttributeType.ITEM_ATTRIBUTE_TYPES.register(MOD_BUS)

        MOD_BUS.addListener(::onCommonSetup)
        CMEvents.init()

        // Datagen
        MOD_BUS.addListener(::datagen)
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {
        CMBlocks.init()
        CMBehaviours.init()
    }

    fun mc(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)
    fun id(path: String): ResourceLocation = id(MODID, path)
    fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)
}