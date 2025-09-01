package org.teamvoided.createllaneous.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import org.teamvoided.createllaneous.Createllaneous.id

object CMBlockTags {
    val TRAIN_TRAPDOORS = tag("train_trapdoors")
    val COPPER_TRAPDOORS = tag("copper_trapdoors")
    val COPPER_DOORS = tag("copper_doors")

//    val INTERACTABLE = tag("interactable")

    fun tag(id: String): TagKey<Block> = TagKey.create(Registries.BLOCK, id(id))
}