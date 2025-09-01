package org.teamvoided.createllaneous.init.misc

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

object CMProperties {
    val BRASS = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
        .mapColor(MapColor.TERRACOTTA_YELLOW)
        .requiresCorrectToolForDrops()
}