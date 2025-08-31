package org.teamvoided.createllaneous.utils

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.*

fun EntityType<*>.isIn(tag: TagKey<EntityType<*>>) = `is`(tag)
fun Entity.isIn(tag: TagKey<EntityType<*>>) = type.isIn(tag)

fun blockKey(block: Block): ResourceLocation =
    Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block))
fun itemKey(item: Item): ResourceLocation =
    Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))