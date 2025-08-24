package org.teamvoided.createllaneous.client

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.Block
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.registries.DeferredBlock
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlockEntity
import org.teamvoided.createllaneous.content.breather.BreezeBreatherRenderer
import org.teamvoided.createllaneous.init.CMBlockEntityTypes
import org.teamvoided.createllaneous.init.CMBlocks
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = Createllaneous.MODID, dist = [Dist.CLIENT])
object CreatellaneousClient {
    init {
        MOD_BUS.addListener(::onClientSetup)
        MOD_BUS.addListener(::registerRender)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        setLayer(CMBlocks.BRASS_GRATE, RenderType.CUTOUT)
    }

    private fun registerRender(event: EntityRenderersEvent.RegisterRenderers){
        event.registerBlockEntityRenderer(CMBlockEntityTypes.BREEZE_BREATHER_BLOCK_ENTITY.get()) { BreezeBreatherRenderer() }
    }

    @Suppress("DEPRECATION")
    fun <T: Block> setLayer(block: DeferredBlock<T>, type: RenderType) =
        ItemBlockRenderTypes.setRenderLayer(block.get(), type)
}
