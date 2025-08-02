package example.examplemod.client

import example.examplemod.Createllaneous
import example.examplemod.Createllaneous.LOGGER
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.Level
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = Createllaneous.MODID, dist = [Dist.CLIENT])
object CreatellaneousClient {
    init {
        MOD_BUS.addListener(::onClientSetup)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }
}
