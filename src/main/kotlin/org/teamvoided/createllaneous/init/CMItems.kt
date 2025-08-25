package org.teamvoided.createllaneous.init

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlockItem

object CMItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(Createllaneous.MODID)

    val EMPTY_BREEZE_BREATHER = register("empty_breeze_breather") {
        EmptyBreezeBreatherBlockItem(CMBlocks.EMPTY_BREEZE_BREATHER.get(), Item.Properties())
    }

    fun <T : Item> register(name: String, item: () -> T): DeferredItem<T> {
        return ITEMS.register(name, item)
    }
}