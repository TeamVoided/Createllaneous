package org.teamvoided.createllaneous.data.gen.prov.client

import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.LanguageProvider
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.init.CMItems
import org.teamvoided.createllaneous.utils.Lang

class ENLangProvider(output: PackOutput) : LanguageProvider(output, MODID, "en_us") {
    override fun addTranslations() {
        CMBlocks.BLOCKS.entries.forEach { ty { addBlock(it, genLang(it.id)) } }
        CMItems.ITEMS.entries.forEach { ty { addItem(it, genLang(it.id)) } }


        add(Lang.TAB, "Createllaneous")
    }

    private fun genLang(identifier: ResourceLocation): String =
        identifier.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    fun ty(fn: () -> Unit) = try {
        fn()
    } catch (e: Exception) {
        LOGGER.error("Error: ", e)
    }
}
