package org.teamvoided.createllaneous.content.contraptions.behaviour

import com.simibubi.create.content.contraptions.Contraption
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.state.BlockState

class CustomTrapdoorMovingInteraction(val close: SoundEvent, val open: SoundEvent) : SimpleBlockMovingInteraction() {
    override fun updateColliders(): Boolean = true
    override fun handle(
        player: Player?, contraption: Contraption, pos: BlockPos, currentState: BlockState,
    ): BlockState {
        if (player != null) {
            val sound = if (currentState.getValue(TrapDoorBlock.OPEN)) close else open
            val pitch = player.level().random.nextFloat() * 0.1f + 0.9f
            playSound(player, sound, pitch)
        }
        return currentState.cycle(TrapDoorBlock.OPEN)
    }

    companion object {
        val WOOD = CustomTrapdoorMovingInteraction(WOODEN_TRAPDOOR_CLOSE, WOODEN_TRAPDOOR_OPEN)
        val COPPER = CustomTrapdoorMovingInteraction(COPPER_TRAPDOOR_CLOSE, COPPER_TRAPDOOR_OPEN)
        val IRON = CustomTrapdoorMovingInteraction(IRON_TRAPDOOR_CLOSE, IRON_TRAPDOOR_OPEN)
    }
}
