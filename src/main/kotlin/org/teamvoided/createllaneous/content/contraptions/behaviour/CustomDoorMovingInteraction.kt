package org.teamvoided.createllaneous.content.contraptions.behaviour

import com.simibubi.create.content.contraptions.Contraption
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents.*
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DoorHingeSide
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo

class CustomDoorMovingInteraction(val close: SoundEvent?, val open: SoundEvent?) : SimpleBlockMovingInteraction() {
    override fun updateColliders(): Boolean = true
    override fun handle(
        player: Player?, contraption: Contraption, pos: BlockPos, currentState: BlockState,
    ): BlockState {
        var currentState = currentState
        if (currentState.block !is DoorBlock) return currentState

        val sound = if (currentState.getValue(DoorBlock.OPEN)) close else open

        val otherPos = if (currentState.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) pos.above() else pos.below()
        val info = contraption.getBlocks()[otherPos]
        if (info != null && info.state().hasProperty(DoorBlock.OPEN)) {
            val newState = info.state().cycle(DoorBlock.OPEN)
            setContraptionBlockData(
                contraption.entity,
                otherPos,
                StructureBlockInfo(info.pos(), newState, info.nbt())
            )
        }

        currentState = currentState.cycle(DoorBlock.OPEN)

        if (player != null) {
            if (currentState.block is SlidingDoorBlock) {
                val hinge = currentState.getValue(SlidingDoorBlock.HINGE)
                val facing = currentState.getValue(SlidingDoorBlock.FACING)
                val doublePos =
                    pos.relative(if (hinge == DoorHingeSide.LEFT) facing.clockWise else facing.counterClockWise)
                val doubleInfo = contraption.getBlocks()[doublePos]
                if (doubleInfo != null && SlidingDoorBlock.isDoubleDoor(
                        currentState,
                        hinge,
                        facing,
                        doubleInfo.state()
                    )
                ) handlePlayerInteraction(null, InteractionHand.MAIN_HAND, doublePos, contraption.entity)
            }

            val pitch = player.level().random.nextFloat() * 0.1f + 0.9f
            if (sound != null) playSound(player, sound, pitch)
        }

        return currentState
    }

    companion object {
        val WOOD = CustomDoorMovingInteraction(WOODEN_DOOR_CLOSE, WOODEN_DOOR_OPEN)
        val COPPER = CustomDoorMovingInteraction(COPPER_DOOR_CLOSE, COPPER_DOOR_OPEN)
        val IRON = CustomDoorMovingInteraction(IRON_DOOR_OPEN, IRON_DOOR_CLOSE)
    }
}
