package org.teamvoided.createllaneous.utils

import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

fun EntityType<*>.isIn(tag: TagKey<EntityType<*>>) = `is`(tag)
fun Entity.isIn(tag: TagKey<EntityType<*>>) = type.isIn(tag)
