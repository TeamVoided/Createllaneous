package org.teamvoided.createllaneous.data.gen.prov


import com.simibubi.create.AllBlocks
import com.simibubi.create.AllItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Blocks
import org.teamvoided.createllaneous.Createllaneous.id
import org.teamvoided.createllaneous.data.gen.Lookup
import org.teamvoided.createllaneous.init.CMBlocks

class CMCraftingRecipeProvider(o: PackOutput, l: Lookup) : RecipeProvider(o, l) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBlocks.CUT_BRASS.asItem(), 4)
            .pattern("##")
            .pattern("##")
            .define('#', AllBlocks.BRASS_BLOCK.asItem())
            .unlockedBy("has_brass_block", has(AllBlocks.BRASS_BLOCK.asItem()))
            .save(recipeOutput)
        recipeOutput.stair(CMBlocks.CUT_BRASS.asItem(), CMBlocks.CUT_BRASS_STAIRS.asItem())
        recipeOutput.slab(CMBlocks.CUT_BRASS.asItem(), CMBlocks.CUT_BRASS_SLAB.asItem())
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBlocks.BRASS_GRATE.asItem(), 4)
            .pattern(" # ")
            .pattern("# #")
            .pattern(" # ")
            .define('#', AllBlocks.BRASS_BLOCK.asItem())
            .unlockedBy("has_brass_block", has(AllBlocks.BRASS_BLOCK.asItem()))
            .save(recipeOutput)

        recipeOutput.createStonecuttedSet(
            listOf(
                AllBlocks.BRASS_BLOCK.asItem() to 4,
                CMBlocks.CUT_BRASS.asItem() to 1
            ),
            CMBlocks.CUT_BRASS.asItem(),
            CMBlocks.CUT_BRASS_STAIRS.asItem(),
            CMBlocks.CUT_BRASS_SLAB.asItem(),
            null,
            CMBlocks.BRASS_GRATE.asItem()
        )

        recipeOutput.simpleCasingTrapdoor(AllBlocks.BRASS_CASING.asItem(),CMBlocks.BRASS_TRAPDOOR.asItem())
        recipeOutput.simpleCasingTrapdoor(AllBlocks.ANDESITE_CASING.asItem(),CMBlocks.ANDESITE_TRAPDOOR.asItem())
        recipeOutput.simpleCasingTrapdoor(AllBlocks.COPPER_CASING.asItem(),CMBlocks.COPPER_CASING_TRAPDOOR.asItem())


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBlocks.EMPTY_BREEZE_BREATHER.asItem(), 1)
            .pattern(" # ")
            .pattern("#0#")
            .pattern(" # ")
            .define('0', Blocks.TUFF.asItem())
            .define('#', AllItems.IRON_SHEET.asItem())
            .unlockedBy("has_breeze_rod", has(Items.BREEZE_ROD))
            .save(recipeOutput)
    }

    private fun RecipeOutput.simpleCasingTrapdoor(input: Item, output: Item) {
        val inpStr = BuiltInRegistries.ITEM.getKey(input).path
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output)
            .requires(ItemTags.WOODEN_TRAPDOORS)
            .requires(input)
            .unlockedBy("has_$inpStr", has(input))
            .save(this)
    }

    private fun RecipeOutput.stonecuttingRecipe(input: Item, output: Item, count: Int = 1) {
        val inpStr = BuiltInRegistries.ITEM.getKey(input).path
        val outStr = BuiltInRegistries.ITEM.getKey(output).path
        return SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, count)
            .unlockedBy("has_$inpStr", has(input))
            .save(this, id(outStr + "_from_" + inpStr + "_stonecutting"))
    }

    fun RecipeOutput.createStonecuttedSet(
        input: List<Pair<Item, Int>>,
        polish: Item? = null,
        stair: Item? = null,
        slab: Item? = null,
        wall: Item? = null,
        vararg extra: Item
    ) {
        input.forEach { (inp, count) ->
            if (polish != null && inp != polish) this.stonecuttingRecipe(inp, polish, count)
            if (stair != null) this.stonecuttingRecipe(inp, stair, count)
            if (slab != null) this.stonecuttingRecipe(inp, slab, 2 * count)
            if (wall != null) this.stonecuttingRecipe(inp, wall, count)
            extra.forEach { special ->
                if (special != inp) {
                    this.stonecuttingRecipe(inp, special, count)
                }
            }
        }
    }

    private fun RecipeOutput.stair(input: Item, output: Item, count: Int = 1) {
        val inpStr = BuiltInRegistries.ITEM.getKey(input).path
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
            .pattern("#  ")
            .pattern("## ")
            .pattern("###")
            .define('#', AllBlocks.BRASS_BLOCK.asItem())
            .unlockedBy("has_$inpStr", has(input))
            .save(this)
    }

    private fun RecipeOutput.slab(input: Item, output: Item, count: Int = 2) {
        val inpStr = BuiltInRegistries.ITEM.getKey(input).path
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
            .pattern("###")
            .define('#', AllBlocks.BRASS_BLOCK.asItem())
            .unlockedBy("has_$inpStr", has(input))
            .save(this)
    }
}