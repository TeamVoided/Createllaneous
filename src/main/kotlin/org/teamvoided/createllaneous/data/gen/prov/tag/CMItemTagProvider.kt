package org.teamvoided.createllaneous.data.gen.prov.tag


import com.simibubi.create.AllTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Block
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.data.gen.Lookup
import org.teamvoided.createllaneous.utils.registry.DOOR_BLOCKS
import java.util.concurrent.CompletableFuture

class CMItemTagProvider(o: PackOutput, l: Lookup, bt: CompletableFuture<TagLookup<Block>>) :
    ItemTagsProvider(o, l, bt, MODID, FH) {
    override fun addTags(provider: HolderLookup.Provider) {
//        tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag).add()
        create()
        copy()
    }

    fun create() {
        DOOR_BLOCKS.forEach { tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag).add(it.asItem()) }
    }

    fun copy() {
        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS)
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS)
    }
}
