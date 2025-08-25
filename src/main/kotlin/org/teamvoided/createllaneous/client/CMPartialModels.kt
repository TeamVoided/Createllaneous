package org.teamvoided.createllaneous.client

import com.simibubi.create.AllPartialModels
import com.simibubi.create.Create
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import org.teamvoided.createllaneous.Createllaneous.id

object CMPartialModels {
    fun init() {}

    val BREEZE_CAGE: PartialModel = block("breeze_breather/block")
    val BREEZE_TINY: PartialModel = block("breeze_breather/breeze/tiny")
    val BREEZE_INERT: PartialModel = block("breeze_breather/breeze/inert")
    val BREEZE_IDLE: PartialModel = block("breeze_breather/breeze/idle")
    val BREEZE_SUPER: PartialModel = block("breeze_breather/breeze/super")

    val BREEZE_BURNER_RODS: PartialModel = block("breeze_breather/rods_small")
    val BREEZE_BURNER_RODS_2: PartialModel = block("breeze_breather/rods_large")
    val BREEZE_BURNER_SUPER_RODS: PartialModel = block("breeze_breather/superheated_rods_small")
    val BREEZE_BURNER_SUPER_RODS_2: PartialModel = block("breeze_breather/superheated_rods_large")


    //val BREEZE_GOGGLES: PartialModel = block("breeze_breather/goggles")
    //val BREEZE_GOGGLES_SMALL: PartialModel = block("breeze_breather/goggles_small")
    //val BREEZE_BURNER_FLAME: PartialModel = block("breeze_breather/flame")

    private fun block(path: String): PartialModel = PartialModel.of(id("block/$path"))
}