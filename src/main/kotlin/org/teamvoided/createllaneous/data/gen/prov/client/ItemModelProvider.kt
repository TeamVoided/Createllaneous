package org.teamvoided.createllaneous.data.gen.prov.client

import com.simibubi.create.AllBlocks
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.Createllaneous.id
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock
import org.teamvoided.createllaneous.content.breather.EmptyBreezeBreatherBlock
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.init.CMItems
import org.teamvoided.createllaneous.utils.blockKey
import org.teamvoided.createllaneous.utils.itemKey

class ItemModelProvider(o: PackOutput) : ItemModelProvider(o, MODID, FH) {
    override fun registerModels() {
        CMItems.ITEMS.entries.forEach {
            when (val item = it.get()) {
                is BlockItem -> when (val blockItem = item.block) {
                    is DoorBlock -> {
                        if (blockItem == CMBlocks.ANDESITE_SLIDING_DOOR.get()) {
                            getBuilder(item.toString())
                                .parent(UncheckedModelFile("item/generated"))
                                .texture("layer0", itemKey(AllBlocks.ANDESITE_DOOR.asItem()).withPrefix("item/"))
                            getBuilder(AllBlocks.ANDESITE_DOOR.asItem().toString())
                                .parent(UncheckedModelFile("item/generated"))
                                .texture("layer0", id("item/andesite_folding_door"))

                        } else this.basicItem(item)
                    }

                    is TrapDoorBlock -> this.withExistingParent(
                        blockKey(blockItem).toString(),
                        blockKey(blockItem).withPrefix("block/").withSuffix("_bottom")
                    )

                    is EmptyBreezeBreatherBlock, is BreezeBreatherBlock -> {
                        this.withExistingParent(
                            blockKey(blockItem).toString(),
                            blockKey(CMBlocks.BREEZE_BREATHER.get()).withPrefix("block/")
                        )
                    }

                    else -> this.simpleBlockItem(blockItem)
                }

                else -> this.basicItem(item)
            }
        }
    }
}