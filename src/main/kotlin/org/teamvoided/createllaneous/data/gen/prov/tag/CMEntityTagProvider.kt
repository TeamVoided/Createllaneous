package org.teamvoided.createllaneous.data.gen.prov.tag


import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.world.entity.EntityType
import org.teamvoided.createllaneous.Createllaneous.MODID
import org.teamvoided.createllaneous.data.gen.FH
import org.teamvoided.createllaneous.data.gen.Lookup
import org.teamvoided.createllaneous.data.tags.CMEntityTags

class CMEntityTagProvider(o: PackOutput, l: Lookup) : EntityTypeTagsProvider(o, l, MODID, FH) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(CMEntityTags.BREEZE_BURNER_CAPTURABLE).add(
            EntityType.BREEZE
        )
    }
}