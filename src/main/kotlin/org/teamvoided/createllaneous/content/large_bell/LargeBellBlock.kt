package org.teamvoided.createllaneous.content.large_bell

import com.mojang.serialization.MapCodec
import com.simibubi.create.foundation.block.IBE
import net.createmod.catnip.data.Iterate
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import org.teamvoided.createllaneous.init.CMBlockEntityTypes
import org.teamvoided.createllaneous.init.CMBlocks
import kotlin.math.sin

class LargeBellBlock(properties: Properties) : BaseEntityBlock(properties), IBE<LargeBellBlockEntity> {
    init {
        registerDefaultState(defaultBlockState().setValue(RINGING, false).setValue(POWERED, false))
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(RINGING, POWERED)
    }

    override fun getBlockEntityType(): BlockEntityType<out LargeBellBlockEntity> =
        CMBlockEntityTypes.LARGE_BELL_BLOCK_ENTITY.get()

    override fun getBlockEntityClass(): Class<LargeBellBlockEntity> = LargeBellBlockEntity::class.java


    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        return if (this.onHit(level, state, hitResult, player, true))
            InteractionResult.sidedSuccess(level.isClientSide)
        else
            InteractionResult.PASS
    }

    fun onHit(
        level: Level,
        state: BlockState,
        result: BlockHitResult,
        player: Player?,
        canRingBell: Boolean
    ): Boolean {
        val direction = result.direction
        val blockpos = result.blockPos
        val flag = !canRingBell || direction.axis != Direction.Axis.Y
        if (flag) {
            val flag1 = this.attemptToRing(player, level, blockpos, direction)
            if (flag1 && player != null) {
                player.awardStat(Stats.BELL_RING)
            }

            return true
        } else {
            return false
        }
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    ) {
        val flag = level.hasNeighborSignal(pos)
        if (flag != state.getValue(POWERED)) {
            if (flag) {
                this.attemptToRing(level, pos, null, LargeBellBlockEntity.REDSTONE_ID)
            }
            level.setBlockAndUpdate(pos, state.setValue(POWERED, flag))
        }
    }

    fun attemptToRing(
        level: Level,
        pos: BlockPos,
        direction: Direction?,
        type: Int = LargeBellBlockEntity.SMALL_HIT_ID
    ): Boolean = this.attemptToRing(null, level, pos, direction, type)

    fun attemptToRing(
        entity: Entity?,
        level: Level,
        pos: BlockPos,
        direction: Direction?,
        type: Int = LargeBellBlockEntity.SMALL_HIT_ID
    ): Boolean {
        val blockentity = level.getBlockEntity(pos)
        if (!level.isClientSide && blockentity is LargeBellBlockEntity) {
            blockentity.onHit(direction, type)
            level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
            return true
        } else {
            return false
        }
    }


    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val pos = context.clickedPos

        //for (x in -1..1) {
        //    for (y in -1..1) {
        //        for (z in -1..1) {
        //            val offset = BlockPos(x, y, z)
        //            if (offset == BlockPos.ZERO) continue
        //            val occupiedState = context.level.getBlockState(pos.offset(offset))
        //            if (!occupiedState.canBeReplaced()) return null
        //        }
        //    }
        //}

        return super.getStateForPlacement(context)
    }

    public override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
        for (side in Iterate.directions) {
            for (secondary in Iterate.falseAndTrue) {
                val targetSide = if (secondary) side.getClockWise(Direction.Axis.Y) else side
                val structurePos = (if (secondary) pPos.relative(side) else pPos).relative(targetSide)
                val occupiedState = pLevel.getBlockState(structurePos)
                val requiredStructure = CMBlocks.LARGE_BELL_STRUCTURAL.get().defaultBlockState()
                    .setValue(DirectionalBlock.FACING, targetSide.opposite)
                if (occupiedState == requiredStructure) continue
                if (!occupiedState.canBeReplaced()) {
                    pLevel.destroyBlock(pPos, false)
                    return
                }
                pLevel.setBlockAndUpdate(structurePos, requiredStructure)
            }
        }
    }

    public override fun getRenderShape(pState: BlockState): RenderShape = RenderShape.ENTITYBLOCK_ANIMATED

    override fun isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Boolean =
        false

    fun getStructuralBlockStates(offset: Vec3i) {
        if (offset.y != 0) {
            if (offset.y > 0) Direction.DOWN
            else Direction.UP
        } else if (offset.x != 0) {
            if (offset.x > 0) Direction.WEST
            else Direction.EAST
        } else {
            if (offset.z > 0) Direction.NORTH
            else Direction.SOUTH
        }
    }

    companion object {
        val CODEC: MapCodec<LargeBellBlock> = simpleCodec(::LargeBellBlock)
        val RINGING: BooleanProperty = BooleanProperty.create("ringing")
        val POWERED = BlockStateProperties.POWERED

        fun bellOscilation(ticksLeft: Int, tickDelta: Float = 0f): Double {
            val ticksRight = (ticksLeft + tickDelta) / 9.8f
            return (ticksRight) * (sin(Math.PI * ticksRight))
        }
    }
}
