package org.teamvoided.createllaneous.client

import com.tterrag.registrate.util.OneTimeEventReceiver
import com.tterrag.registrate.util.nullness.NonNullSupplier
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.Level
import org.teamvoided.createllaneous.Createllaneous
import org.teamvoided.createllaneous.Createllaneous.LOGGER
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.function.Predicate

@Mod(value = Createllaneous.MODID, dist = [Dist.CLIENT])
object CreatellaneousClient {
    init {
        MOD_BUS.addListener(::onClientSetup)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }
}
