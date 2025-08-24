package org.teamvoided.createllaneous.init

import org.teamvoided.createllaneous.Createllaneous
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlockEntity
import org.teamvoided.createllaneous.init.CMBlocks.BREEZE_BREATHER

object CMBlockEntityTypes {
    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Createllaneous.MODID)

    val BREEZE_BREATHER_BLOCK_ENTITY = register("breeze_breather_block_entity") {
        BlockEntityType(::BreezeBreatherBlockEntity, setOf(BREEZE_BREATHER.get()), null)
    }

    fun <T : BlockEntity> register(
        name: String, be: () -> BlockEntityType<T>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
        return BLOCK_ENTITY_TYPES.register(name, be)
    }
}