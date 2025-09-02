package org.teamvoided.createllaneous.data.gen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.loot.LootTableProvider
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.prov.CMCraftingRecipeProvider
import org.teamvoided.createllaneous.data.gen.prov.client.BlockModelProvider
import org.teamvoided.createllaneous.data.gen.prov.client.ENLangProvider
import org.teamvoided.createllaneous.data.gen.prov.client.ItemModelProvider
import org.teamvoided.createllaneous.data.gen.prov.loot.BlockLootProvider.Companion.blockLoot
import org.teamvoided.createllaneous.data.gen.prov.proc.CMCrushingRecipeGen
import org.teamvoided.createllaneous.data.gen.prov.tag.CMBlockTagProvider
import org.teamvoided.createllaneous.data.gen.prov.tag.CMEntityTagProvider
import org.teamvoided.createllaneous.data.gen.prov.tag.CMItemTagProvider
import java.util.concurrent.CompletableFuture

typealias Lookup = CompletableFuture<HolderLookup.Provider>

var FH: ExistingFileHelper? = null

fun datagen(event: GatherDataEvent) {
    FH = turnOffFileHelper(event.existingFileHelper)
    // Assets
    event.createProvider(::ENLangProvider)
    event.createProvider(::ItemModelProvider)
    event.createProvider(::BlockModelProvider)

    //Data
    event.createProvider(::CMCraftingRecipeProvider)
    event.createProvider(::CMCrushingRecipeGen)
    // Tags
    val blockTags = event.createProvider(::CMBlockTagProvider)
    event.createProvider { o, l -> CMItemTagProvider(o, l, blockTags.contentsGetter()) }
    event.createProvider(::CMEntityTagProvider)

    event.createProvider { o, l -> LootTableProvider(o, setOf(), listOf(blockLoot()), l) }

    // Dynamic
    event.createProvider { o, l ->
        DatapackBuiltinEntriesProvider(
            o, l, RegistrySetBuilder()
//                .add(Registries.DAMAGE_TYPE, CADamageTypesDatagen::bootstrap)
//                .add(CreateRegistries.POTATO_PROJECTILE_TYPE, CAPotatoProjectileTypesDatagen::bootstrap)
            ,
            setOf(MODID)
        )
    }
}

fun turnOffFileHelper(fh: ExistingFileHelper): ExistingFileHelper {
    try {
        val enabledField = fh.javaClass.getDeclaredField("enable")
        enabledField.isAccessible = true
        enabledField.setBoolean(fh, false)
    } catch (e: Exception) {
        println("Error setting enable to false: ${e.message}")
    }
    return fh
}