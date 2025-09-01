package org.teamvoided.createllaneous.content.contraptions.behaviour

import com.simibubi.create.content.contraptions.Contraption
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class InteractableMovingInteraction : SimpleBlockMovingInteraction() {
    override fun handle(
        player: Player?,
        contraption: Contraption?,
        pos: BlockPos,
        currentState: BlockState,
    ): BlockState {
        if (/*currentState.`is`(INTERACTABLE) &&*/ player != null) {
            val block = currentState.block
            if (player.mainHandItem == ItemStack.EMPTY) {
                currentState.useWithoutItem(
                    player.level(),
                    player,
                    BlockHitResult(pos.toVec3(), Direction.UP, pos, true)
                )
            }
        }
        return currentState
    }
}