package org.teamvoided.createllaneous.client

import me.fzzyhmstrs.fzzy_config.annotations.NonSync
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor
import org.teamvoided.createllaneous.Createllaneous
import java.awt.Color

class CMClientConfig : Config(Createllaneous.id(Createllaneous.MODID)) {
    // train map
    @JvmField
    @NonSync
    var enableCustomTrainMapColors = false

    @JvmField
    var mainColor = ValidatedColor(Color.GREEN, true)

    @JvmField
    var darkerColor = ValidatedColor(Color.ORANGE, true)

    @JvmField
    var darkerColorShadow = ValidatedColor(Color.GRAY, true)

    // R_G_B_A
    @JvmField
    var outlineColor = ValidatedColor(Color.BLACK, true)

    // compat
    var enableXaerosIntegration = true
}