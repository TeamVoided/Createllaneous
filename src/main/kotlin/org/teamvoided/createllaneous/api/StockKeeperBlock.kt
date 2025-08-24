package org.teamvoided.createllaneous.api

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics

interface StockKeeperBlock {
    fun isValid(): Boolean
    fun render(
        keeperBlock: StockKeeperBlock,
        graphics: GuiGraphics,
        matrix: PoseStack,
        x: Int,
        y: Int,
        windowHeight: Int,
    )
}