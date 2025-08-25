package org.teamvoided.createllaneous.client

import com.simibubi.create.AllBlockEntityTypes
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.registries.DeferredBlock
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.BreezeBreatherRenderer
import org.teamvoided.createllaneous.content.breather.BreezeBreatherVisual
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
        listOf(
            CMBlocks.BRASS_GRATE,
            CMBlocks.EMPTY_BREEZE_BREATHER,
            CMBlocks.BREEZE_BREATHER,
        ).forEach { setLayer(it, RenderType.CUTOUT) }

        registerVisualizer(CMBlockEntityTypes.BREEZE_BREATHER_BLOCK_ENTITY.get(), ::BreezeBreatherVisual)


        CMPartialModels.init()
    }

    private fun registerRender(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(CMBlockEntityTypes.BREEZE_BREATHER_BLOCK_ENTITY.get()) { BreezeBreatherRenderer() }
    }

    fun <T : BlockEntity> registerVisualizer(
        type: BlockEntityType<T>, factory: SimpleBlockEntityVisualizer.Factory<T>,
    ): SimpleBlockEntityVisualizer<T> {
        return SimpleBlockEntityVisualizer.builder<T>(type)
            .factory(factory)
            .skipVanillaRender { true }
            .apply()
    }

    @Suppress("DEPRECATION")
    fun <T : Block> setLayer(block: DeferredBlock<T>, type: RenderType) =
        ItemBlockRenderTypes.setRenderLayer(block.get(), type)
}
