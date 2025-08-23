package org.teamvoided.createllaneous.init

import org.teamvoided.createllaneous.Createllaneous
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object CMBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(Createllaneous.MODID)

    val EXAMPLE_BLOCK = register("example_block") {
        Block(BlockBehaviour.Properties.of().lightLevel { 15 }.strength(3.0f))
    }

    fun <T : Block> register(name: String, block: () -> T): DeferredBlock<T> {
        return BLOCKS.register(name, block)
    }
}