package org.teamvoided.createllaneous.compat

import net.createmod.catnip.registry.RegisteredObjectsHelper
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.fml.ModList
import java.util.*
import java.util.function.Supplier

object CMMods {
    val CURIOS = OtherMod("curios")
    val JEI = OtherMod("JEI")
    @JvmField
    val XAEROS_WORLD_MAP = OtherMod("xaeroworldmap")
    val CREATE_ENCHANTMENT_INDUSTRY = OtherMod("create_enchantment_industry")

    @Suppress("unused")
    data class OtherMod(val id: String) {
        val isLoaded: Boolean = ModList.get().isLoaded(id)
        fun rl(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(id, path)
        fun getBlock(id: String): Block = BuiltInRegistries.BLOCK.get(rl(id))
        fun getItem(id: String): Item = BuiltInRegistries.ITEM.get(rl(id))
        fun contains(entry: ItemLike): Boolean {
            if (!this.isLoaded) return false
            val asItem = entry.asItem()
            return asItem != null && (RegisteredObjectsHelper.getKeyOrThrow(asItem).namespace == id)
        }

        fun <T : Any> runIfInstalled(toRun: Supplier<Supplier<T>>): Optional<T> =
            if (this.isLoaded) Optional.of<T>(toRun.get().get()) else Optional.empty<T>()

        fun executeIfInstalled(toExecute: Supplier<Runnable?>) = if (this.isLoaded) toExecute.get()!!.run() else Unit
    }
}
