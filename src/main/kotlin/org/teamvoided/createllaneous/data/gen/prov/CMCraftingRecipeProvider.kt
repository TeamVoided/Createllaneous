package org.teamvoided.createllaneous.data.gen.prov


import com.simibubi.create.AllBlocks
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
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
    }
}
