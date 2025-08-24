//package org.teamvoided.createllaneous.data.gen.prov
//
//
//import com.simibubi.create.AllBlocks
//import com.simibubi.create.AllItems
//import net.minecraft.data.PackOutput
//import net.minecraft.data.recipes.*
//import net.minecraft.data.recipes.RecipeProvider.has
//import net.minecraft.resources.ResourceLocation
//import net.minecraft.tags.ItemTags
//import net.minecraft.tags.TagKey
//import net.minecraft.world.item.Item
//import net.minecraft.world.item.Items
//import net.minecraft.world.item.crafting.Ingredient
//import net.minecraft.world.level.ItemLike
//import net.neoforged.neoforge.common.Tags
//import net.neoforged.neoforge.common.conditions.ModLoadedCondition
//import org.teamvoided.createllaneous.Createllaneous.MODID
//import org.teamvoided.createllaneous.data.gen.Lookup
//
//class CACraftingRecipeProvider(output: PackOutput, registries: Lookup) : RecipeProvider(output, registries) {
//    override fun buildRecipes(recipeOutput: RecipeOutput) {
//            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CABlocks.BARBED_WIRE.asItem(), 2)
//                .pattern(" W ")
//                .pattern("W W")
//                .pattern(" W ")
//                .define('W', CATagRegister.Items.IRON_WIRES)
//                .unlockedBy("has_iron_wires", has(CATagRegister.Items.IRON_WIRES))
//                .unlockedBy("has_barbed_wires", has(CABlocks.BARBED_WIRE.asItem()))
//
//
//        SimpleCookingRecipeBuilder.smoking(
//            Ingredient.of(*arrayOf<ItemLike?>(CAItems.CAKE_BASE.asItem())),
//            RecipeCategory.FOOD,
//            CAItems.CAKE_BASE_BAKED,
//            0.0f,
//            100
//        )
//            .unlockedBy("has_cake_base", has(CAItems.CAKE_BASE))
//            .unlockedBy("has_cake_base_baked", has(CAItems.CAKE_BASE_BAKED))
//            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateAddition.MODID, "smoking/cake_base_baked"))
//    }
//}
