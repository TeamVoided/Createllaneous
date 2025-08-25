package org.teamvoided.createllaneous.data.gen.prov.client

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlock
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.init.CMItems
import org.teamvoided.createllaneous.utils.blockKey
import java.util.*

class ItemModelProvider(o: PackOutput) : ItemModelProvider(o, MODID, FH) {
    override fun registerModels() {
        CMItems.ITEMS.entries.forEach {
            when (val item = it.get()) {
                is BlockItem -> when (val blockItem = item.block) {
                    is DoorBlock -> this.basicItem(item)
                    is TrapDoorBlock -> this.simpleBlockItem(blockKey(item.block).withSuffix("_bottom"))
                    is EmptyBreezeBreatherBlock, is BreezeBreatherBlock -> {
                        this.withExistingParent(
                            blockKey(blockItem).toString(),
                            ResourceLocation.fromNamespaceAndPath(
                                MODID,
                                "block/" + blockKey(CMBlocks.BREEZE_BREATHER.get()).path + "/block"
                            )
                        )
                    }

                    else -> this.simpleBlockItem(item.block)
                }

                else -> this.basicItem(item)
            }
        }
    }

}