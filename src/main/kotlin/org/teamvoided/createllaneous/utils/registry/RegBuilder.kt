package org.teamvoided.createllaneous.utils.registry

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.neoforged.neoforge.registries.DeferredBlock


val CUTOUT = mutableSetOf<DeferredBlock<out Block>>()

fun <T : Block> DeferredBlock<T>.cutout(): DeferredBlock<T> {
    CUTOUT.add(this)
    return this
}


// region Mineables
val PICKAXE_ABLE = mutableSetOf<DeferredBlock<out Block>>()
val AXE_ABLE = mutableSetOf<DeferredBlock<out Block>>()

fun <T : Block> DeferredBlock<T>.axe(): DeferredBlock<T> {
    AXE_ABLE.add(this)
    return this
}

fun <T : Block> DeferredBlock<T>.pickaxe(): DeferredBlock<T> {
    PICKAXE_ABLE.add(this)
    return this
}

fun <T : Block> DeferredBlock<T>.mineable(): DeferredBlock<T> = this.pickaxe().axe()

// endregion

// region Levels
val IRON_TOOL = mutableSetOf<DeferredBlock<out Block>>()
fun <T : Block> DeferredBlock<T>.ironTool(): DeferredBlock<T> {
    IRON_TOOL.add(this)
    return this
}
// endregion

// region Shapes
val STAIRS = mutableSetOf<DeferredBlock<out StairBlock>>()
val SLABS = mutableSetOf<DeferredBlock<out SlabBlock>>()
val TRAPDOORS = mutableSetOf<DeferredBlock<out Block>>()

fun <T : StairBlock> DeferredBlock<T>.stair(): DeferredBlock<T> {
    STAIRS.add(this)
    return this
}

fun <T : SlabBlock> DeferredBlock<T>.slab(): DeferredBlock<T> {
    SLABS.add(this)
    return this
}

fun <T : Block> DeferredBlock<T>.trapdoor(): DeferredBlock<T> {
    TRAPDOORS.add(this)
    return this
}
// endregion

// region Special
val DOOR_BLOCKS = mutableSetOf<DeferredBlock<out SlidingDoorBlock>>()

fun <T : SlidingDoorBlock> DeferredBlock<T>.door(): DeferredBlock<T> {
    DOOR_BLOCKS.add(this)
    return this
}

// endregion
