package org.teamvoided.createllaneous.init

import net.minecraft.world.item.BlockItem
import org.teamvoided.createllaneous.Createllaneous
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object CMItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(Createllaneous.MODID)

    //val EXAMPLE_ITEM = register("example_item") {
    //    Item(Item.Properties())
    //}

    fun <T : Item> register(name: String, item: () -> T): DeferredItem<T> {
        return ITEMS.register(name, item)
    }
}