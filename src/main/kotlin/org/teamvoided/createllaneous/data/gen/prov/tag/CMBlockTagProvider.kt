package org.teamvoided.createllaneous.data.gen.prov.tag


import com.simibubi.create.AllTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.BlockTagsProvider
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.data.gen.Lookup
import org.teamvoided.createllaneous.init.CMBlocks

class CMBlockTagProvider(o: PackOutput, l: Lookup) : BlockTagsProvider(o, l, MODID, FH) {
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
            CMBlocks.ANDESITE_TRAPDOOR.get(),
        )

        tag(BlockTags.MINEABLE_WITH_AXE).add(
            CMBlocks.BRASS_TRAPDOOR.get(),
            CMBlocks.ANDESITE_TRAPDOOR.get(),
        )

        tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag).add(
            CMBlocks.BRASS_GRATE.get(),
            CMBlocks.BRASS_TRAPDOOR.get(),
            CMBlocks.ANDESITE_TRAPDOOR.get(),

            Blocks.LIGHTNING_ROD,

            Blocks.COPPER_BLOCK,
            Blocks.EXPOSED_COPPER,
            Blocks.WEATHERED_COPPER,
            Blocks.OXIDIZED_COPPER,

            Blocks.CUT_COPPER,
            Blocks.CUT_COPPER_STAIRS,
            Blocks.CUT_COPPER_SLAB,
            Blocks.EXPOSED_CUT_COPPER,
            Blocks.EXPOSED_CUT_COPPER_STAIRS,
            Blocks.EXPOSED_CUT_COPPER_SLAB,
            Blocks.WEATHERED_CUT_COPPER,
            Blocks.WEATHERED_CUT_COPPER_STAIRS,
            Blocks.WEATHERED_CUT_COPPER_SLAB,
            Blocks.OXIDIZED_CUT_COPPER,
            Blocks.OXIDIZED_CUT_COPPER_STAIRS,
            Blocks.OXIDIZED_CUT_COPPER_SLAB,

            Blocks.CHISELED_COPPER,
            Blocks.EXPOSED_CHISELED_COPPER,
            Blocks.WEATHERED_CHISELED_COPPER,
            Blocks.OXIDIZED_CHISELED_COPPER,

            Blocks.COPPER_GRATE,
            Blocks.EXPOSED_COPPER_GRATE,
            Blocks.WEATHERED_COPPER_GRATE,
            Blocks.OXIDIZED_COPPER_GRATE,

            Blocks.COPPER_BULB,
            Blocks.EXPOSED_COPPER_BULB,
            Blocks.WEATHERED_COPPER_BULB,
            Blocks.OXIDIZED_COPPER_BULB,

            Blocks.COPPER_DOOR,
            Blocks.EXPOSED_COPPER_DOOR,
            Blocks.WEATHERED_COPPER_DOOR,
            Blocks.OXIDIZED_COPPER_DOOR,

            Blocks.COPPER_TRAPDOOR,
            Blocks.EXPOSED_COPPER_TRAPDOOR,
            Blocks.WEATHERED_COPPER_TRAPDOOR,
            Blocks.OXIDIZED_COPPER_TRAPDOOR,

            Blocks.WAXED_COPPER_BLOCK,
            Blocks.WAXED_EXPOSED_COPPER,
            Blocks.WAXED_WEATHERED_COPPER,
            Blocks.WAXED_OXIDIZED_COPPER,

            Blocks.WAXED_CUT_COPPER,
            Blocks.WAXED_CUT_COPPER_STAIRS,
            Blocks.WAXED_CUT_COPPER_SLAB,
            Blocks.WAXED_EXPOSED_CUT_COPPER,
            Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
            Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
            Blocks.WAXED_WEATHERED_CUT_COPPER,
            Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
            Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
            Blocks.WAXED_OXIDIZED_CUT_COPPER,
            Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
            Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,

            Blocks.WAXED_CHISELED_COPPER,
            Blocks.WAXED_EXPOSED_CHISELED_COPPER,
            Blocks.WAXED_WEATHERED_CHISELED_COPPER,
            Blocks.WAXED_OXIDIZED_CHISELED_COPPER,

            Blocks.WAXED_COPPER_GRATE,
            Blocks.WAXED_EXPOSED_COPPER_GRATE,
            Blocks.WAXED_WEATHERED_COPPER_GRATE,
            Blocks.WAXED_OXIDIZED_COPPER_GRATE,

            Blocks.WAXED_COPPER_BULB,
            Blocks.WAXED_EXPOSED_COPPER_BULB,
            Blocks.WAXED_WEATHERED_COPPER_BULB,
            Blocks.WAXED_OXIDIZED_COPPER_BULB,

            Blocks.WAXED_COPPER_DOOR,
            Blocks.WAXED_EXPOSED_COPPER_DOOR,
            Blocks.WAXED_WEATHERED_COPPER_DOOR,
            Blocks.WAXED_OXIDIZED_COPPER_DOOR,

            Blocks.WAXED_COPPER_TRAPDOOR,
            Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
            Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
            Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,

            // (ender) add the rest of them at some point
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