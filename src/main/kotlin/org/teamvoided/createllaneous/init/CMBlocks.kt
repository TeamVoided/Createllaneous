package org.teamvoided.createllaneous.init

import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour
import com.simibubi.create.api.behaviour.movement.MovementBehaviour
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction
import com.simibubi.create.content.decoration.TrainTrapdoorBlock
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour
import com.simibubi.create.foundation.data.SharedProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock
import org.teamvoided.createllaneous.content.breather.BreezeBreatherMovementBehavior
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlock

object CMBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(Createllaneous.MODID)
    val DOOR_BLOCKS = mutableSetOf<DeferredBlock<out Block>>()


    val EMPTY_BREEZE_BREATHER = registerNoItem("empty_breeze_breather") {
        EmptyBreezeBreatherBlock(BlockBehaviour.Properties.ofFullCopy(SharedProperties.softMetal()))
    }
    val BREEZE_BREATHER = register("breeze_breather") {
        BreezeBreatherBlock(BlockBehaviour.Properties.ofFullCopy(EMPTY_BREEZE_BREATHER.get()))
    }

    val CUT_BRASS = register("cut_brass") {
        Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                .mapColor(MapColor.TERRACOTTA_YELLOW)
                .requiresCorrectToolForDrops()
        )
    }
    val CUT_BRASS_STAIRS = register("cut_brass_stairs") {
        StairBlock(CUT_BRASS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CUT_BRASS.get()))
    }
    val CUT_BRASS_SLAB = register("cut_brass_slab") {
        SlabBlock(BlockBehaviour.Properties.ofFullCopy(CUT_BRASS.get()))
    }

    val BRASS_GRATE = register("brass_grate") {
        WaterloggedTransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_GRATE))
    }
    val BRASS_TRAPDOOR = register("brass_trapdoor") {
        TrainTrapdoorBlock(BlockBehaviour.Properties.ofFullCopy(CUT_BRASS.get()).sound(SoundType.NETHERITE_BLOCK))
        //TrapDoorBlock(SlidingDoorBlock.TRAIN_SET_TYPE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_GRATE))
    }
    val ANDESITE_TRAPDOOR = register("andesite_trapdoor") {
        TrainTrapdoorBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                .requiresCorrectToolForDrops()
        )
    }
    val ANDESITE_SLIDING_DOOR = register("andesite_sliding_door") {
        SlidingDoorBlock.metal(
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 6.0F),
            false
        )
    }.doorBlock()
    val COPPER_CASING_TRAPDOOR = register("copper_casing_trapdoor") {
        TrainTrapdoorBlock(
            BlockBehaviour.Properties.ofFullCopy(BRASS_TRAPDOOR.get())
                .mapColor(Blocks.COPPER_TRAPDOOR.defaultMapColor())
        )
    }
    val COPPER_CASING_SLIDING_DOOR = register("copper_casing_sliding_door") {
        SlidingDoorBlock.metal(
            BlockBehaviour.Properties.ofFullCopy(ANDESITE_SLIDING_DOOR.get())
                .mapColor(Blocks.COPPER_DOOR.defaultMapColor()),
            false
        )
    }.doorBlock()

    fun init() {
        MovementBehaviour.REGISTRY.register(BREEZE_BREATHER.get(), BreezeBreatherMovementBehavior())
        MovingInteractionBehaviour.REGISTRY.register(
            BREEZE_BREATHER.get(),
            BreezeBreatherBlock.BreezeBreatherConductor()
        )

        DOOR_BLOCKS.forEach {
            MovingInteractionBehaviour.REGISTRY.register(it.get(), DoorMovingInteraction())
            MovementBehaviour.REGISTRY.register(it.get(), SlidingDoorMovementBehaviour())
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

    fun <T : Block> DeferredBlock<T>.doorBlock(): DeferredBlock<T> {
        DOOR_BLOCKS.add(this)
        return this
    }
}