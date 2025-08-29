package org.teamvoided.createllaneous.init

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlockItem
import org.teamvoided.createllaneous.content.large_bell.LargeBellBlockItem

object CMItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(Createllaneous.MODID)

    val EMPTY_BREEZE_BREATHER = register("empty_breeze_breather") {
        EmptyBreezeBreatherBlockItem(CMBlocks.EMPTY_BREEZE_BREATHER.get(), Item.Properties())
    }
    val LARGE_BELL = register("large_bell") {
        LargeBellBlockItem(CMBlocks.LARGE_BELL.get(), Item.Properties())
    }

    fun <T : Item> register(name: String, item: () -> T): DeferredItem<T> {
        return ITEMS.register(name, item)
    }
}