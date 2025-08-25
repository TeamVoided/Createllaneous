package org.teamvoided.createllaneous.data.gen.prov.loot

import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import org.teamvoided.createllaneous.init.CMBlocks

class BlockLootProvider(l: HolderLookup.Provider) : BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS, l) {
    override fun getKnownBlocks(): Iterable<Block> = CMBlocks.BLOCKS.entries.map { it.value() }.toList()

    override fun generate() {
        dropSelf(CMBlocks.CUT_BRASS.get())
        add(CMBlocks.BRASS_TRAPDOOR.get(), createSilkTouchOnlyTable(CMBlocks.ANDESITE_TRAPDOOR.get()))
    }

    companion object {
        fun blockLoot(): SubProviderEntry = SubProviderEntry(::BlockLootProvider, LootContextParamSets.BLOCK)
    }
}