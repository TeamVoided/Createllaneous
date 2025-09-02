package org.teamvoided.createllaneous.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlockEntity
import org.teamvoided.createllaneous.content.large_bell.LargeBellBlockEntity

object CMBlockEntityTypes {
    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Createllaneous.MODID)

    val BREEZE_BREATHER_BLOCK_ENTITY = register("breeze_breather_block_entity") {
        BlockEntityType(::BreezeBreatherBlockEntity, setOf(CMBlocks.BREEZE_BREATHER.get()), null)
    }
    val LARGE_BELL_BLOCK_ENTITY = register("large_bell_block_entity") {
        BlockEntityType(::LargeBellBlockEntity, setOf(CMBlocks.LARGE_BELL.get()), null)
    }

    fun <T : BlockEntity> register(name: String, be: () -> BlockEntityType<T>)
            : DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = BLOCK_ENTITY_TYPES.register(name, be)
}