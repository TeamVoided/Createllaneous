package org.teamvoided.createllaneous.data.gen.prov.client

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DoorHingeSide
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.Createllaneous.mc
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.init.CMBlocks
import java.util.function.Supplier


class BlockModelProvider(o: PackOutput) : BlockStateProvider(o, MODID, FH) {

    override fun registerStatesAndModels() {
        //(ender) do model stuff I guess :shrug:


        CMBlocks.DOOR_BLOCKS.forEach {
            slidingDoorBlock(it.get(), mc(RenderType.CUTOUT_MIPPED.name))
        }
    }


    fun slidingDoorBlock(block: Block, renderType: ResourceLocation) {
        val baseName = BuiltInRegistries.ITEM.getKey(block.asItem()).path
        val texturePath = BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/")
        val top: ResourceLocation = texturePath.withSuffix("_top")
        val bottom: ResourceLocation = texturePath.withSuffix("_bottom")

        val bottomLeft: ModelFile = (models()
            .doorBottomLeft(baseName + "_bottom_left", bottom, top)).renderType(renderType)
        val bottomRight: ModelFile =
            (models().doorBottomRight(baseName + "_bottom_right", bottom, top)).renderType(renderType)
        val topLeft: ModelFile =
            (models().doorTopLeft(baseName + "_top_left", bottom, top)).renderType(renderType)
        val topRight: ModelFile =
            (models().doorTopRight(baseName + "_top_right", bottom, top)).renderType(renderType)


        getVariantBuilder(block).forAllStatesExcept({ state: BlockState ->
            var yRot = (state.getValue(DoorBlock.FACING)).toYRot().toInt() + 90
            val right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT
            val lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER

            yRot %= 360
            val model: ModelFile = if (lower) {
                if (right) {
                    bottomRight
                } else {
                    bottomLeft
                }
            } else {
                if (right) {
                    topRight
                } else {
                    topLeft
                }
            }
            ConfiguredModel.builder().modelFile(model).rotationY(yRot).build()
        }, *arrayOf<Property<*>>(DoorBlock.POWERED, DoorBlock.OPEN, SlidingDoorBlock.VISIBLE))
    }

    fun BlockModelProvider.simpleBlock(block: Block): BlockModelProvider {
        return this.simpleBlock(block)

    }
}