package org.teamvoided.createllaneous.content.large_bell

import com.mojang.serialization.MapCodec
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions
import org.teamvoided.createllaneous.init.CMBlocks

class LargeBellStructuralBlock(properties: Properties) : DirectionalBlock(properties),
    IProxyHoveringInformation {
    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder.add(FACING))
    }

    public override fun getRenderShape(pState: BlockState): RenderShape = RenderShape.INVISIBLE

    override fun getPistonPushReaction(pState: BlockState): PushReaction =PushReaction.BLOCK

    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player
    ): ItemStack = CMBlocks.LARGE_BELL.toStack()

    public override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pIsMoving: Boolean
    ) {
        if (stillValid(pLevel, pPos, pState, false)) pLevel.destroyBlock(getMaster(pLevel, pPos, pState), true)
    }

    override fun playerWillDestroy(pLevel: Level, pPos: BlockPos, pState: BlockState, pPlayer: Player): BlockState {
        if (stillValid(pLevel, pPos, pState, false)) {
            val masterPos = getMaster(pLevel, pPos, pState)
            pLevel.destroyBlockProgress(masterPos.hashCode(), masterPos, -1)
            if (!pLevel.isClientSide() && pPlayer.isCreative) pLevel.destroyBlock(masterPos, false)
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer)
    }

    public override fun updateShape(
        pState: BlockState, pFacing: Direction, pFacingState: BlockState, pLevel: LevelAccessor,
        pCurrentPos: BlockPos, pFacingPos: BlockPos
    ): BlockState {
        if (stillValid(pLevel, pCurrentPos, pState, false)) {
            val masterPos = getMaster(pLevel, pCurrentPos, pState)
            if (!pLevel.blockTicks.hasScheduledTick(masterPos, CMBlocks.LARGE_BELL.get())
            ) pLevel.scheduleTick(masterPos, CMBlocks.LARGE_BELL.get(), 1)
            return pState
        }
        if (pLevel !is Level || pLevel.isClientSide()) return pState
        if (!pLevel.getBlockTicks()
                .hasScheduledTick(pCurrentPos, this)
        ) pLevel.scheduleTick(pCurrentPos, this, 1)
        return pState
    }

    fun stillValid(level: BlockGetter, pos: BlockPos, state: BlockState, directlyAdjacent: Boolean): Boolean {
        if (!state.`is`(this)) return false

        val direction = state.getValue(FACING)
        val targetedPos = pos.relative(direction)
        val targetedState = level.getBlockState(targetedPos)

        if (!directlyAdjacent && stillValid(level, targetedPos, targetedState, true)) return true
        return (targetedState.block is LargeBellBlock)
    }

    public override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
        if (!stillValid(pLevel, pPos, pState, false)) pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState())
    }

    override fun addLandingEffects(
        state1: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        state2: BlockState,
        entity: LivingEntity,
        numberOfParticles: Int
    ): Boolean = true

    class RenderProperties : IClientBlockExtensions, MultiPosDestructionHandler {
        override fun addDestroyEffects(
            state: BlockState,
            Level: Level,
            pos: BlockPos,
            manager: ParticleEngine
        ): Boolean = true

        override fun addHitEffects(
            state: BlockState,
            level: Level,
            target: HitResult,
            manager: ParticleEngine
        ): Boolean {
            if (target is BlockHitResult) {
                val targetPos: BlockPos = target.blockPos
                val block = CMBlocks.LARGE_BELL_STRUCTURAL.get()
                if (block.stillValid(level, targetPos, state, false))
                    manager.crack(getMaster(level, targetPos, state), target.direction)
                return true
            }
            return super<IClientBlockExtensions>.addHitEffects(state, level, target, manager)
        }

        override fun getExtraPositions(
            level: ClientLevel,
            pos: BlockPos,
            blockState: BlockState,
            progress: Int
        ): Set<BlockPos>? {
            val waterWheelStructuralBlock = CMBlocks.LARGE_BELL_STRUCTURAL.get()
            if (!waterWheelStructuralBlock.stillValid(level, pos, blockState, false)) return null
            val set = HashSet<BlockPos>()
            set.add(getMaster(level, pos, blockState))
            return set
        }
    }

    override fun getInformationSource(level: Level, pos: BlockPos, state: BlockState): BlockPos {
        return if (stillValid(level, pos, state, false)) getMaster(level, pos, state) else pos
    }

    override fun isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Boolean {
        return false
    }

    override fun codec(): MapCodec<out DirectionalBlock> = CODEC

    companion object {
        val CODEC: MapCodec<LargeBellStructuralBlock> = simpleCodec(::LargeBellStructuralBlock)

        fun getMaster(level: BlockGetter, pos: BlockPos, state: BlockState): BlockPos {
            val direction = state.getValue(FACING)
            val targetedPos = pos.relative(direction)
            val targetedState = level.getBlockState(targetedPos)
            if (targetedState.`is`(CMBlocks.LARGE_BELL_STRUCTURAL.get()))
                return getMaster(level, targetedPos, targetedState)
            return targetedPos
        }
    }
}
