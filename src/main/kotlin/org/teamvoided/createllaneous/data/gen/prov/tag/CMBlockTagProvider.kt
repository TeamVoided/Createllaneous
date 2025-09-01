package org.teamvoided.createllaneous.data.gen.prov.tag


import com.simibubi.create.AllBlocks
import com.simibubi.create.AllTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.BlockTagsProvider
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.data.gen.Lookup
import org.teamvoided.createllaneous.data.tags.CMBlockTags
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.utils.registry.AXE_ABLE
import org.teamvoided.createllaneous.utils.registry.DOOR_BLOCKS
import org.teamvoided.createllaneous.utils.registry.IRON_TOOL
import org.teamvoided.createllaneous.utils.registry.PICKAXE_ABLE
import org.teamvoided.createllaneous.utils.registry.SLABS
import org.teamvoided.createllaneous.utils.registry.STAIRS
import org.teamvoided.createllaneous.utils.registry.TRAPDOORS

class CMBlockTagProvider(o: PackOutput, l: Lookup) : BlockTagsProvider(o, l, MODID, FH) {
    override fun addTags(provider: HolderLookup.Provider) {
        shapes()
        mining()
        createllaneous()
        create()
    }

    fun shapes() {
        SLABS.forEach { tag(BlockTags.SLABS).add(it.get()) }
        STAIRS.forEach { tag(BlockTags.STAIRS).add(it.get()) }
        TRAPDOORS.forEach { tag(BlockTags.TRAPDOORS).add(it.get()) }
        DOOR_BLOCKS.forEach {
            tag(BlockTags.DOORS).add(it.get())
            tag(BlockTags.MOB_INTERACTABLE_DOORS).add(it.get())
        }
    }

    fun mining() {
        // Automatic
        PICKAXE_ABLE.forEach { tag(BlockTags.MINEABLE_WITH_PICKAXE).add(it.get()) }
        AXE_ABLE.forEach { tag(BlockTags.MINEABLE_WITH_AXE).add(it.get()) }
        IRON_TOOL.forEach { tag(BlockTags.NEEDS_IRON_TOOL).add(it.get()) }

        tag(BlockTags.MINEABLE_WITH_AXE).add(
            // (ender) Why create?
            AllBlocks.TRAIN_TRAPDOOR.get()
        )
    }

    fun createllaneous(){
//        TRAPDOORS.forEach { tag(CMBlockTags.INTERACTABLE_TRAPDOORS).add(it.get()) }
//        tag(CMBlockTags.INTERACTABLE_TRAPDOORS).add(it.get())
    }

    fun create() {
        // Automatic
        DOOR_BLOCKS.forEach { tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag).add(it.get()) }

        tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag).add(
            CMBlocks.BRASS_GRATE.get()
        )
//        tag(AllTags.AllBlockTags.BRITTLE.tag).add()
        wrenchPickup()
    }

    fun wrenchPickup() {
        tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag).add(
            CMBlocks.BRASS_GRATE.get(),

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
        )
    }
}