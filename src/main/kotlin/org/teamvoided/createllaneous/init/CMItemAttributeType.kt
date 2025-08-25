package org.teamvoided.createllaneous.init

import com.simibubi.create.AllDataComponents.SEQUENCED_ASSEMBLY
import com.simibubi.create.api.registry.CreateBuiltInRegistries
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType
import com.simibubi.create.content.logistics.item.filter.attribute.SingletonItemAttribute
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous.MODID
import java.util.function.BiPredicate
import java.util.function.Function

object CMItemAttributeType {
    val ITEM_ATTRIBUTE_TYPES: DeferredRegister<ItemAttributeType> =
        DeferredRegister.create(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE, MODID)

    val INCOMPLETE =
        singleton("incomplete") { item, level -> item.get(SEQUENCED_ASSEMBLY) != null || item.item is SequencedAssemblyItem }


    private fun singleton(id: String, predicate: BiPredicate<ItemStack, Level>) = register(id) { loc ->
        SingletonItemAttribute.Type { SingletonItemAttribute(it, predicate, "${loc.namespace}.${loc.path}") }
    }


    internal fun register(id: String, type: Function<ResourceLocation, ItemAttributeType>) =
        ITEM_ATTRIBUTE_TYPES.register(id, type)
}