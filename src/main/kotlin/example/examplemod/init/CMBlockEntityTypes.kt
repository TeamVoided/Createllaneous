package example.examplemod.init

import example.examplemod.Createllaneous
import example.examplemod.init.CMBlocks.EXAMPLE_BLOCK
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.FurnaceBlockEntity
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object CMBlockEntityTypes {
    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Createllaneous.MODID)

    val EXAMPLE_BLOCK_ENTITY = register("example_block_entity") {
        BlockEntityType(::FurnaceBlockEntity, setOf(EXAMPLE_BLOCK.get()), null)
    }

    fun <T : BlockEntity> register(
        name: String, be: () -> BlockEntityType<T>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
        return BLOCK_ENTITY_TYPES.register(name, be)
    }
}