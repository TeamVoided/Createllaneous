package org.teamvoided.createllaneous.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.utils.Lang

object CMTabs {
    val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID)

    val TAB = register(MODID) {
        CreativeModeTab.builder()
            .title(Component.translatable(Lang.TAB))
            .icon { CMBlocks.CUT_BRASS.asItem().defaultInstance }
            .displayItems { _, output -> output.acceptAll(CMItems.ITEMS.entries.map { it.get().defaultInstance }) }
            .build()
    }

    fun <T : CreativeModeTab> register(name: String, item: () -> T): DeferredHolder<CreativeModeTab, T> {
        return TABS.register(name, item)
    }
}