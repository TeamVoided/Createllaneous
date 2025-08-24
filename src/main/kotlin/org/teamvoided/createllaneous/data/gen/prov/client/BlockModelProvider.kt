package org.teamvoided.createllaneous.data.gen.prov.client

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import org.teamvoided.createllaneous.Createllaneous.MODID

class BlockModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : BlockModelProvider(output, MODID, existingFileHelper) {
    override fun registerModels() {
        //(ender) do model stuff I guess :shrug:
    }
}