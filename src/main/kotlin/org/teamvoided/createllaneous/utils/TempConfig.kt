package org.teamvoided.createllaneous.utils

import java.awt.Color

class CMConfig {
    // -- Client --
    // train map
    @JvmField
    var enableCustomTrainMapColors = false

    @JvmField
    val mainColor: Color = Color.GREEN

    @JvmField
    val darkerColor: Color = Color.ORANGE

    @JvmField
    val darkerColorShadow: Color = Color.GRAY

    // R_G_B_A
    @JvmField
    var outlineColor = 0xFF_00_00_00.toInt()

    // compat
    var enableXaerosIntegration = true
}
