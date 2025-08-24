package org.teamvoided.createllaneous.client

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.Block
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.registries.DeferredBlock
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.init.CMBlocks
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = Createllaneous.MODID, dist = [Dist.CLIENT])
object CreatellaneousClient {
    init {
        MOD_BUS.addListener(::onClientSetup)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        setLayer(CMBlocks.BRASS_GRATE, RenderType.CUTOUT)
    }

    @Suppress("DEPRECATION")
    fun <T: Block> setLayer(block: DeferredBlock<T>, type: RenderType) =
        ItemBlockRenderTypes.setRenderLayer(block.get(), type)
}
