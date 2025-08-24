package org.teamvoided.createllaneous.data.gen.prov.tag


import com.simibubi.create.AllTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.init.CMBlocks
import java.util.concurrent.CompletableFuture

class CMBlockTagProvider(
    output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>, fh: ExistingFileHelper,
) : BlockTagsProvider(output, lookup, MODID, fh) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag).add(
            CMBlocks.BRASS_GRATE.get()
        )
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            CMBlocks.CUT_BRASS.get(),
            CMBlocks.CUT_BRASS_STAIRS.get(),
            CMBlocks.CUT_BRASS_SLAB.get(),
            CMBlocks.BRASS_GRATE.get(),
            CMBlocks.BRASS_TRAPDOOR.get(),
        )

        tag(BlockTags.MINEABLE_WITH_AXE).add(
            CMBlocks.BRASS_TRAPDOOR.get(),
        )

        tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag).add(
            Blocks.COPPER_GRATE,
            // (ender) add the rest of them at some point

            CMBlocks.BRASS_GRATE.get(),
            CMBlocks.BRASS_TRAPDOOR.get(),
        )

        tag(BlockTags.NEEDS_IRON_TOOL).add(
            CMBlocks.CUT_BRASS.get(),
            CMBlocks.CUT_BRASS_STAIRS.get(),
            CMBlocks.CUT_BRASS_SLAB.get(),
            CMBlocks.BRASS_GRATE.get(),
        )

//        tag(AllTags.AllBlockTags.BRITTLE.tag).add()
    }
}