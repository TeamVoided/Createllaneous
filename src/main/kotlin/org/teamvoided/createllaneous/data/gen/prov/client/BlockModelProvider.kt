package org.teamvoided.createllaneous.data.gen.prov.client

import com.simibubi.create.AllBlocks
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
import net.neoforged.neoforge.client.model.generators.*
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.Createllaneous.id
import org.teamvoided.createllaneous.Createllaneous.mc
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.utils.blockKey


class BlockModelProvider(o: PackOutput) : BlockStateProvider(o, MODID, FH) {

    override fun registerStatesAndModels() {

        CMBlocks.DOOR_BLOCKS.forEach {
            slidingDoorBlock(it.get(), doorSideParticleFromBlockName(it.get()), mc(RenderType.CUTOUT_MIPPED.name))
        }
    }

    private fun doorSideParticleFromBlockName(block: Block): Pair<String, String> {
        val str: String = BuiltInRegistries.ITEM.getKey(block.asItem()).path
        val side = when (str.first().toString()) {
            "a" -> doorSide(AllBlocks.ANDESITE_DOOR.get()) to block(AllBlocks.ANDESITE_CASING.get()).toString()
            "b" -> doorSide(AllBlocks.BRASS_DOOR.get()) to block(AllBlocks.BRASS_CASING.get()).toString()
            "c" -> id("block/copper_casing_door_side").toString() to block(AllBlocks.COPPER_CASING.get()).toString()
            "t" -> doorSide(AllBlocks.TRAIN_DOOR.get()) to block(AllBlocks.RAILWAY_CASING.get()).toString()
            else -> str to str
        }
        return side
    }

    private fun block(block: Block): ResourceLocation = blockKey(block).withPrefix("block/")
    private fun doorSide(block: Block): String =
        block(block).withSuffix("_side").toString()


    fun slidingDoorBlock(block: Block, pair: Pair<String, String>, renderType: ResourceLocation) {
        val texturePath = BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/")

        val top = createDoor(texturePath.toString(), pair, true).renderType(renderType)
        val bottom = createDoor(texturePath.toString(), pair, false).renderType(renderType)

        getVariantBuilder(block).forAllStatesExcept({ state: BlockState ->

            val model: ModelFile = if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) top else bottom
            val yRot = ((state.getValue(DoorBlock.FACING)).toYRot().toInt() + 90) % 360

            ConfiguredModel.builder().modelFile(model).rotationY(yRot).build()

        }, *arrayOf<Property<*>>(DoorBlock.POWERED, DoorBlock.OPEN, DoorBlock.HINGE, SlidingDoorBlock.VISIBLE))
    }

    private fun createDoor(name: String,  pair: Pair<String, String>, top: Boolean): BlockModelBuilder {
        val suffix = if (top) "top" else "bottom"
        val string = name + "_" + suffix

        return models()
            .withExistingParent(string, id("block/parent/create_door_$suffix"))
            .texture("particle", pair.second)
            .texture(suffix, string)
            .texture("side", pair.first)
    }

    fun BlockModelProvider.simpleBlock(block: Block): BlockModelProvider {
        return this.simpleBlock(block)

    }
}