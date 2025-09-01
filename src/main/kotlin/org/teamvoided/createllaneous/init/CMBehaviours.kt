package org.teamvoided.createllaneous.init

import com.simibubi.create.AllPartialModels
import com.simibubi.create.api.registry.SimpleRegistry.Provider.forBlockTag
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import net.createmod.catnip.data.Couple
import net.minecraft.core.registries.BuiltInRegistries
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock
import org.teamvoided.createllaneous.content.breather.BreezeBreatherMovementBehavior
import org.teamvoided.createllaneous.content.contraptions.behaviour.CustomDoorMovingInteraction
import org.teamvoided.createllaneous.content.contraptions.behaviour.CustomTrapdoorMovingInteraction
import org.teamvoided.createllaneous.data.tags.CMBlockTags
import org.teamvoided.createllaneous.init.CMBlocks.BREEZE_BREATHER
import org.teamvoided.createllaneous.utils.registry.DOOR_BLOCKS
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.REGISTRY as MOVING_INTERACTION
import com.simibubi.create.api.behaviour.movement.MovementBehaviour.REGISTRY as MOVEMENT

object CMBehaviours {
    fun init() {
        // Vanilla addon
        MOVING_INTERACTION.registerProvider(forBlockTag(CMBlockTags.COPPER_DOORS, CustomDoorMovingInteraction.COPPER))
        MOVING_INTERACTION.registerProvider(
            forBlockTag(CMBlockTags.COPPER_TRAPDOORS, CustomTrapdoorMovingInteraction.COPPER)
        )
       /* MOVING_INTERACTION.registerProvider(
            forBlockTag(CMBlockTags.INTERACTABLE, InteractableMovingInteraction())
        )*/

        // Mod
        MOVING_INTERACTION.registerProvider(
            forBlockTag(CMBlockTags.TRAIN_TRAPDOORS, CustomTrapdoorMovingInteraction.IRON)
        )

        MOVEMENT.register(BREEZE_BREATHER.get(), BreezeBreatherMovementBehavior())
        MOVING_INTERACTION.register(BREEZE_BREATHER.get(), BreezeBreatherBlock.BreezeBreatherConductor())

        DOOR_BLOCKS.forEach {
            val block = it.get()

            MOVING_INTERACTION.register(block, DoorMovingInteraction())
            MOVEMENT.register(block, SlidingDoorMovementBehaviour())

            if (block.isFoldingDoor) {
                val model = BuiltInRegistries.BLOCK.getKey(block)
                //MAKE SURE YOU MATCH THIS WITH WHAT IS IN YOUR BLOCK MODEL DATAGEN (no block/ prefix)
                AllPartialModels.FOLDING_DOORS[model] = Couple.create(
                    PartialModel.of(model.withPrefix("block/").withSuffix("_left")),
                    PartialModel.of(model.withPrefix("block/").withSuffix("_right"))
                )
            }
        }
    }
}