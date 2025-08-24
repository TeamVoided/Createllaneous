package org.teamvoided.createllaneous.data.gen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.prov.CMCraftingRecipeProvider
import org.teamvoided.createllaneous.data.gen.prov.client.BlockModelProvider
import org.teamvoided.createllaneous.data.gen.prov.client.ItemModelProvider
import org.teamvoided.createllaneous.data.gen.prov.client.ENLangProvider
import org.teamvoided.createllaneous.data.gen.prov.proc.CMCrushingRecipeGen
import org.teamvoided.createllaneous.data.gen.prov.tag.CMBlockTagProvider
import org.teamvoided.createllaneous.data.gen.prov.tag.CMItemTagProvider
import java.util.concurrent.CompletableFuture

typealias Lookup = CompletableFuture<HolderLookup.Provider>

fun gatherData(event: GatherDataEvent) {
    val generator = event.generator
    val output = generator.packOutput
    val lookup = event.lookupProvider
    val fh = turnOffFH(event.existingFileHelper)
    val server = event.includeServer()

    val blockTags: BlockTagsProvider = CMBlockTagProvider(output, lookup, fh)
    generator.addProvider(server, blockTags)
    generator.addProvider(server, CMItemTagProvider(output, lookup, blockTags.contentsGetter(), fh))
    generator.addProvider(server, CMCraftingRecipeProvider(output, lookup))
    generator.addProvider(server, CMCrushingRecipeGen(output, lookup))

    generator.addProvider(server, ENLangProvider(output))
    generator.addProvider(server, ItemModelProvider(output, fh))
    generator.addProvider(server, BlockModelProvider(output, fh))

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

fun turnOffFH(fh: ExistingFileHelper): ExistingFileHelper {
    try {
        val enabledField = fh.javaClass.getDeclaredField("enable")
        enabledField.isAccessible = true
        enabledField.setBoolean(fh, false)
    } catch (e: Exception) {
        println("Error setting enable to false: ${e.message}")
    }
    return fh
}