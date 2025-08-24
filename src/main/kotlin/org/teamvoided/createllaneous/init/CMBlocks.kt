package org.teamvoided.createllaneous.init

import com.simibubi.create.content.decoration.TrainTrapdoorBlock
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

object CMBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(Createllaneous.MODID)
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

    /** ask ender what in the world do i do in [model].json for cutout */
    val BRASS_GRATE = register("brass_grate") {
        WaterloggedTransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_GRATE))
    }
    val BRASS_TRAPDOOR = register("brass_trapdoor") {
        TrainTrapdoorBlock(BlockBehaviour.Properties.ofFullCopy(CUT_BRASS.get()).sound(SoundType.NETHERITE_BLOCK))
        //TrapDoorBlock(SlidingDoorBlock.TRAIN_SET_TYPE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_GRATE))
    }

    /** what is a visual? */
    val BREEZE_BREATHER = register("breeze_breather") {
        BreezeBreatherBlock(BlockBehaviour.Properties.ofFullCopy(SharedProperties.softMetal()))
    }

    init {
        //MovingInteractionBehaviour.interactionBehaviour<BreezeBreatherBlock>(BreezeBreatherBlock.BreezeBreatherConductor())
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