package org.teamvoided.createllaneous.data.gen.prov.client

import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.TrapDoorBlock
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.init.CMItems

class ItemModelProvider(o: PackOutput, fh: ExistingFileHelper) : ItemModelProvider(o, MODID, fh) {
    override fun registerModels() {
        CMItems.ITEMS.entries.forEach {
            val item = it.get()
            when (item) {
                is BlockItem -> if (item.block !is TrapDoorBlock) this.simpleBlockItem(item.block)
                else -> this.basicItem(item)
            }
        }
    }
}