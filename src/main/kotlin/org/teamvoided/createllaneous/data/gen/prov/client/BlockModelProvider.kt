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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.neoforged.neoforge.client.model.generators.*
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.Createllaneous.id
import org.teamvoided.createllaneous.Createllaneous.mc
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.utils.blockKey
import org.teamvoided.createllaneous.utils.registry.DOOR_BLOCKS


class BlockModelProvider(o: PackOutput) : BlockStateProvider(o, MODID, FH) {

    override fun registerStatesAndModels() {
        DOOR_BLOCKS.forEach {
            if (it.get() !is SlidingDoorBlock) throw Error("${it.registeredName} is not a sliding door or folding door")
            slidingDoorBlock(it.get(), doorSideParticleFromBlockName(it.get()))
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


    fun slidingDoorBlock(block: Block, pair: Pair<String, String>) {
        val texturePath = BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/")
        val renderType = mc(RenderType.CUTOUT_MIPPED.name)

        val top = createDoor(texturePath.toString(), pair, true).renderType(renderType)
        val bottom = createDoor(texturePath.toString(), pair, false).renderType(renderType)
        if ((block as SlidingDoorBlock).isFoldingDoor) {
            createFoldedDoor(texturePath, pair, true)
            createFoldedDoor(texturePath, pair, false)

        }

        getVariantBuilder(block).forAllStatesExcept({ state: BlockState ->
            val model: ModelFile = if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) top else bottom
            val yRot = ((state.getValue(DoorBlock.FACING)).toYRot().toInt() + 90) % 360

            ConfiguredModel.builder().modelFile(model).rotationY(yRot).build()

        }, *arrayOf<Property<*>>(DoorBlock.POWERED, DoorBlock.OPEN, DoorBlock.HINGE, SlidingDoorBlock.VISIBLE))
    }


    private fun createDoor(name: String, pair: Pair<String, String>, top: Boolean): BlockModelBuilder {
        val suffix = if (top) "top" else "bottom"
        val string = name + "_" + suffix

        return models()
            .withExistingParent(string, id("block/parent/create_door_$suffix"))
            .texture("particle", pair.second)
            .texture(suffix, string)
            .texture("side", pair.first)
    }

    private fun createFoldedDoor(
        texturePath: ResourceLocation,
        pair: Pair<String, String>,
        left: Boolean
    ): BlockModelBuilder {
        val side = if (left) "left" else "right"
        val name = texturePath.toString()
        val modelName = texturePath.withSuffix("_$side") //MAKE SURE YOU MATCH THIS WITH WHAT IS IN YOUR BLOCK INIT INIT

        return models()
            .withExistingParent(modelName.toString(), id("block/parent/folding_door/fold_$side"))
            .texture("particle", pair.second)
            .texture("top", name + "_top")
            .texture("bottom", name + "_bottom")
            .texture("side", pair.first)
            .renderType(mc(RenderType.CUTOUT_MIPPED.name))
    }

    fun BlockModelProvider.simpleBlock(block: Block): BlockModelProvider {
        return this.simpleBlock(block)

    }
}