package org.teamvoided.createllaneous.data.gen.prov.client

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.TrapDoorBlock
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlock
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.init.CMItems
import java.util.*

class ItemModelProvider(o: PackOutput, fh: ExistingFileHelper) : ItemModelProvider(o, MODID, fh) {
    override fun registerModels() {
        CMItems.ITEMS.entries.forEach {
            when (val item = it.get()) {
                is BlockItem -> when (val blockItem = item.block) {
                    is EmptyBreezeBreatherBlock, is BreezeBreatherBlock -> {
                        this.withExistingParent(
                            key(blockItem).toString(),
                            ResourceLocation.fromNamespaceAndPath(
                                MODID,
                                "block/" + key(CMBlocks.BREEZE_BREATHER.get()).path + "/block"
                            )
                        )
                    }

                    !is TrapDoorBlock -> this.simpleBlockItem(item.block)
                }

                else -> this.basicItem(item)
            }
        }
    }

    private fun key(block: Block): ResourceLocation =
        Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block))
}