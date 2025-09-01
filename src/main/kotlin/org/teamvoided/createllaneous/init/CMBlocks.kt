package org.teamvoided.createllaneous.init

import com.simibubi.create.AllPartialModels
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour
import com.simibubi.create.api.behaviour.movement.MovementBehaviour
import com.simibubi.create.api.registry.SimpleRegistry
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction
import com.simibubi.create.content.decoration.TrainTrapdoorBlock
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour
import com.simibubi.create.foundation.data.SharedProperties
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import net.createmod.catnip.data.Couple
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock
import org.teamvoided.createllaneous.content.breather.BreezeBreatherMovementBehavior
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlock
import org.teamvoided.createllaneous.content.large_bell.LargeBellBlock
import org.teamvoided.createllaneous.content.large_bell.LargeBellStructuralBlock
import org.teamvoided.createllaneous.data.tags.CMBlockTags
import org.teamvoided.createllaneous.init.misc.CMProperties
import org.teamvoided.createllaneous.utils.registry.*

object CMBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(Createllaneous.MODID)


    val EMPTY_BREEZE_BREATHER = registerNoItem("empty_breeze_breather") {
        EmptyBreezeBreatherBlock(ofFullCopy(SharedProperties.softMetal()))
    }.cutout().pickaxe()
    val BREEZE_BREATHER = registerNoItem("breeze_breather") {
        BreezeBreatherBlock(ofFullCopy(EMPTY_BREEZE_BREATHER.get()))
    }.cutout().pickaxe()
    val LARGE_BELL = registerNoItem("large_bell") {
        LargeBellBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).forceSolidOn().requiresCorrectToolForDrops()
                .strength(10f).sound(SoundType.ANVIL).pushReaction(PushReaction.BLOCK)
        )
    }.pickaxe()
    val LARGE_BELL_STRUCTURAL = registerNoItem("large_bell_structural") {
        LargeBellStructuralBlock(ofFullCopy(LARGE_BELL.get()))
    }.pickaxe()

    // region Brass
    val CUT_BRASS = register("cut_brass") { Block(CMProperties.BRASS) }.pickaxe().ironTool()
    val CUT_BRASS_STAIRS = register("cut_brass_stairs") {
        StairBlock(CUT_BRASS.get().defaultBlockState(), CMProperties.BRASS)
    }.pickaxe().ironTool().stair()
    val CUT_BRASS_SLAB = register("cut_brass_slab") { SlabBlock(CMProperties.BRASS) }.pickaxe().ironTool().slab()
    val BRASS_GRATE = register("brass_grate") {
        WaterloggedTransparentBlock(ofFullCopy(Blocks.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_YELLOW))
    }.cutout().pickaxe().ironTool()
    // endregion

    // region Trapdoors
    val BRASS_TRAPDOOR = register("brass_trapdoor") {
        TrainTrapdoorBlock(CMProperties.BRASS.sound(SoundType.NETHERITE_BLOCK))
    }.mineable().trapdoor()
    val ANDESITE_TRAPDOOR = register("andesite_trapdoor") {
        TrainTrapdoorBlock(ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops())
    }.mineable().trapdoor()
    val COPPER_CASING_TRAPDOOR = register("copper_casing_trapdoor") {
        TrainTrapdoorBlock(ofFullCopy(BRASS_TRAPDOOR.get()).mapColor(MapColor.COLOR_ORANGE))
    }.mineable().trapdoor()
    // endregion

    // region Doors
    val ANDESITE_SLIDING_DOOR = register("andesite_sliding_door") {
        SlidingDoorBlock.metal(
            ofFullCopy(Blocks.IRON_DOOR)
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 6.0F),
            false
        )
    }.door().mineable()

    val COPPER_CASING_SLIDING_DOOR = register("copper_casing_sliding_door") {
        SlidingDoorBlock.metal(
            ofFullCopy(ANDESITE_SLIDING_DOOR.get())
                .mapColor(Blocks.COPPER_DOOR.defaultMapColor()),
            false
        )
    }.door().mineable()
    val COPPER_CASING_FOLDING_DOOR = register("copper_casing_folding_door") {
        SlidingDoorBlock.metal(ofFullCopy(COPPER_CASING_SLIDING_DOOR.get()), true)
    }.door().mineable()
    // endregion


    fun init() {
        MovementBehaviour.REGISTRY.register(BREEZE_BREATHER.get(), BreezeBreatherMovementBehavior())
        MovingInteractionBehaviour.REGISTRY.register(
            BREEZE_BREATHER.get(),
            BreezeBreatherBlock.BreezeBreatherConductor()
        )
       /* MovingInteractionBehaviour.REGISTRY.registerProvider(
            SimpleRegistry.Provider.forBlockTag(CMBlockTags.INTERACTABLE_TRAPDOORS, TrapdoorMovingInteraction())
        )*/

        DOOR_BLOCKS.forEach {
            val block = it.get()

            MovingInteractionBehaviour.REGISTRY.register(block, DoorMovingInteraction())
            MovementBehaviour.REGISTRY.register(block, SlidingDoorMovementBehaviour())

            if (block.isFoldingDoor) {
                val model = BuiltInRegistries.BLOCK.getKey(block)
                //MAKE SURE YOU MATCH THIS WITH WHAT IS IN YOUR BLOCK MODEL DATAGEN (no block/ prefix)
                AllPartialModels.FOLDING_DOORS[model] = Couple.create(
                    PartialModel.of(model.withPrefix("block/").withSuffix("_left")),
                    PartialModel.of(model.withPrefix("block/").withSuffix("_right"))
                )
            }
        }
    }

    fun <T : Block> register(name: String, blockSupplier: () -> T): DeferredBlock<T> {
        val block = registerNoItem(name, blockSupplier)
        CMItems.register(name) { BlockItem(block.get(), Item.Properties()) }
        return block
    }

    fun <T : Block> registerNoItem(name: String, blockSupplier: () -> T): DeferredBlock<T> {
        return BLOCKS.register(name, blockSupplier)
    }

}