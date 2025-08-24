package org.teamvoided.createllaneous.data.gen.prov.tag


import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import org.teamvoided.createllaneous.Createllaneous.MODID
import java.util.concurrent.CompletableFuture

class CMItemTagProvider(
    output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>,
    bt: CompletableFuture<TagLookup<Block>>, fh: ExistingFileHelper,
) : ItemTagsProvider(output, lookup, bt, MODID, fh) {
    override fun addTags(provider: HolderLookup.Provider) {
//        tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag).add()
    }
}
