package org.teamvoided.createllaneous.backport

import com.simibubi.create.Create
import com.simibubi.create.compat.trainmap.TrainMapManager
import com.simibubi.create.compat.trainmap.TrainMapSyncClient
import com.simibubi.create.foundation.gui.RemovedGuiUtils
import com.simibubi.create.foundation.utility.CreateLang
import com.simibubi.create.infrastructure.config.AllConfigs
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.Rect2i
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.neoforged.neoforge.client.event.InputEvent.MouseButton
import org.teamvoided.createllaneous.mixin.backport.xearo.XaeroFullscreenMapAccessor
import xaero.map.gui.GuiMap
import xaero.map.gui.ScreenBase

object XaeroTrainMap {
    private var requesting = false
    @JvmStatic
    var renderedDimension: ResourceKey<Level?>? = null
        private set

    @JvmStatic
    private var encounteredException = false

    @JvmStatic
    fun tick() {
        if (!AllConfigs.client().showTrainMapOverlay.get() || !isMapOpen(Minecraft.getInstance().screen)) {
            if (requesting) TrainMapSyncClient.stopRequesting()
            requesting = false
            return
        }
        TrainMapManager.tick()
        requesting = true
        TrainMapSyncClient.requestData()
    }

    @JvmStatic
    fun mouseClick(event: MouseButton.Pre) {
        if (encounteredException) return

        val mc = Minecraft.getInstance()
        try {
            if (mc.screen !is GuiMap) return
        } catch (e: Exception) {
            Create.LOGGER.error("Failed to handle mouseClick for Xaero's World Map train map integration:", e)
            encounteredException = true
            return
        }

        val window = mc.window
        val mX = mc.mouseHandler.xpos() * window.guiScaledWidth / window.screenWidth
        val mY = mc.mouseHandler.ypos() * window.guiScaledHeight / window.screenHeight

        if (TrainMapManager.handleToggleWidgetClick(Mth.floor(mX), Mth.floor(mY), 3, 30)) event.setCanceled(true)
    }

    // Called by XaeroFullscreenMapMixin, guarded by try-catch
    @JvmStatic
    fun onRender(graphics: GuiGraphics, screen: GuiMap, mX: Int, mY: Int, pt: Float) {
        val x = (screen as XaeroFullscreenMapAccessor).getCameraX()
        val z = (screen as XaeroFullscreenMapAccessor).getCameraZ()
        val mapScale = (screen as XaeroFullscreenMapAccessor).getScale()
        renderedDimension =
            (screen as XaeroFullscreenMapAccessor).cm_getMapProcessor().mapWorld.currentDimension.dimId

        if (!AllConfigs.client().showTrainMapOverlay.get()) {
            renderToggleWidgetAndTooltip(graphics, screen, mX, mY)
            return
        }

        val mc = Minecraft.getInstance()
        val window = mc.window

        val guiScale = window.screenWidth.toDouble() / window.guiScaledWidth
        val scale = mapScale / guiScale

        val pose = graphics.pose()
        pose.pushPose()

        pose.translate(screen.width / 2.0f, screen.height / 2.0f, 0f)
        pose.scale(scale.toFloat(), scale.toFloat(), 1f)
        pose.translate(-x, -z, 0.0)

        var mouseX = mX - screen.width / 2.0f
        var mouseY = mY - screen.height / 2.0f
        mouseX /= scale.toFloat()
        mouseY /= scale.toFloat()
        mouseX += x.toFloat()
        mouseY += z.toFloat()

        val bounds =
            Rect2i(
                Mth.floor(-screen.width / 2.0f / scale + x), Mth.floor(-screen.height / 2.0f / scale + z),
                Mth.floor(screen.width / scale), Mth.floor(screen.height / scale)
            )

        val tooltip =
            TrainMapManager.renderAndPick(graphics, Mth.floor(mouseX), Mth.floor(mouseY), false, bounds)

        pose.popPose()

        if (!renderToggleWidgetAndTooltip(
                graphics,
                screen,
                mX,
                mY
            ) && tooltip != null
        ) RemovedGuiUtils.drawHoveringText(graphics, tooltip, mX, mY, screen.width, screen.height, 256, mc.font)
    }

    private fun renderToggleWidgetAndTooltip(
        graphics: GuiGraphics?, screen: GuiMap, mouseX: Int,
        mouseY: Int,
    ): Boolean {
        TrainMapManager.renderToggleWidget(graphics, 3, 30)
        if (!TrainMapManager.isToggleWidgetHovered(mouseX, mouseY, 3, 30)) return false

        RemovedGuiUtils.drawHoveringText(
            graphics,
            listOf(CreateLang.translate("train_map.toggle").component()),
            mouseX,
            mouseY + 20,
            screen.width,
            screen.height,
            256,
            Minecraft.getInstance().font
        )
        return true
    }

    @JvmStatic
    fun isMapOpen(screen: Screen?): Boolean {
        if (encounteredException) return false

        try {
            return screen is ScreenBase &&
                    (screen is GuiMap || screen.parent is GuiMap)
        } catch (e: Exception) {
            Create.LOGGER.error("Failed to check if Xaero's World Map was open for train map integration:", e)
            encounteredException = true
            return false
        }
    }
}