package org.teamvoided.createllaneous.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import org.teamvoided.createllaneous.Createllaneous.id

object CMEntityTags {
    val BREEZE_BURNER_CAPTURABLE = tag("breeze_burner_capturable")


    fun tag(id: String): TagKey<EntityType<*>> = TagKey.create(Registries.ENTITY_TYPE, id(id))

}