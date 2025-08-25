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

class CMCraftingRecipeProvider(output: PackOutput, registries: Lookup) : RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBlocks.CUT_BRASS.asItem(), 4)
            .pattern("##")
            .pattern("##")
            .define('#', AllBlocks.BRASS_BLOCK.asItem())
            .unlockedBy("has_brass_block", has(AllBlocks.BRASS_BLOCK.asItem()))
            .save(recipeOutput)
        recipeOutput.stonecuttingRecipe(AllBlocks.BRASS_BLOCK.asItem(), CMBlocks.CUT_BRASS.asItem(), 4)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBlocks.BRASS_GRATE.asItem(), 4)
            .pattern(" # ")
            .pattern("# #")
            .pattern(" # ")
            .define('#', AllBlocks.BRASS_BLOCK.asItem())
            .unlockedBy("has_brass_block", has(AllBlocks.BRASS_BLOCK.asItem()))
            .save(recipeOutput)
        recipeOutput.stonecuttingRecipe(AllBlocks.BRASS_BLOCK.asItem(), CMBlocks.BRASS_GRATE.asItem(), 4)

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CMBlocks.BRASS_TRAPDOOR.asItem())
            .requires(ItemTags.WOODEN_TRAPDOORS)
            .requires(AllBlocks.BRASS_CASING.asItem())
            .unlockedBy("has_brass_casing", has(AllBlocks.BRASS_CASING.asItem()))
            .save(recipeOutput)


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBlocks.EMPTY_BREEZE_BREATHER.asItem(), 1)
            .pattern(" # ")
            .pattern("#0#")
            .pattern(" # ")
            .define('0', Blocks.TUFF.asItem())
            .define('#', AllItems.IRON_SHEET.asItem())
            .unlockedBy("has_breeze_rod", has(Items.BREEZE_ROD))
            .save(recipeOutput)
    }

    private fun RecipeOutput.stonecuttingRecipe(input: Item, output: Item, count: Int = 1) {
        val inpStr = BuiltInRegistries.ITEM.getKey(input).path
        val outStr = BuiltInRegistries.ITEM.getKey(output).path
        return SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, count)
            .unlockedBy("has_$inpStr", has(input))
            .save(this, id(outStr + "_from_" + inpStr + "_stonecutting"))
    }
}