package org.teamvoided.createllaneous.data.gen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.prov.CMCraftingRecipeProvider
import org.teamvoided.createllaneous.data.gen.prov.client.BlockModelProvider
import org.teamvoided.createllaneous.data.gen.prov.client.ENLangProvider
import org.teamvoided.createllaneous.data.gen.prov.client.ItemModelProvider
import org.teamvoided.createllaneous.data.gen.prov.proc.CMCrushingRecipeGen
import org.teamvoided.createllaneous.data.gen.prov.tag.CMBlockTagProvider
import org.teamvoided.createllaneous.data.gen.prov.tag.CMEntityTagProvider
import org.teamvoided.createllaneous.data.gen.prov.tag.CMItemTagProvider
import java.util.concurrent.CompletableFuture

typealias Lookup = CompletableFuture<HolderLookup.Provider>

var FH: ExistingFileHelper? = null

fun gatherData(event: GatherDataEvent) {
    val generator = event.generator
    val output = generator.packOutput
    val lookup = event.lookupProvider
    FH = turnOffFileHelper(event.existingFileHelper)
    val server = event.includeServer()

    //Data
    generator.addProvider(server, CMCraftingRecipeProvider(output, lookup))
    generator.addProvider(server, CMCrushingRecipeGen(output, lookup))
    // Tags
    val blockTags = generator.addProvider(server, CMBlockTagProvider(output, lookup))
    generator.addProvider(server, CMItemTagProvider(output, lookup, blockTags.contentsGetter()))
    generator.addProvider(server, CMEntityTagProvider(output, lookup))

    // Assets
    generator.addProvider(server, ENLangProvider(output))
    generator.addProvider(server, ItemModelProvider(output))
    generator.addProvider(server, BlockModelProvider(output))

    generator.addProvider(
        server, DatapackBuiltinEntriesProvider(
            output, lookup, RegistrySetBuilder()
//                .add(Registries.DAMAGE_TYPE, CADamageTypesDatagen::bootstrap)
//                .add(CreateRegistries.POTATO_PROJECTILE_TYPE, CAPotatoProjectileTypesDatagen::bootstrap)
            ,
            setOf(MODID)
        )
    )
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