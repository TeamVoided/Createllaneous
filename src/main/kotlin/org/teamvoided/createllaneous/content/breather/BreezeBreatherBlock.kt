package org.teamvoided.createllaneous.content.breather

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.simibubi.create.AllItems
import com.simibubi.create.AllShapes
import com.simibubi.create.api.behaviour.interaction.ConductorBlockInteractionBehavior
import com.simibubi.create.content.equipment.wrench.IWrenchable
import com.simibubi.create.content.logistics.stockTicker.StockTickerInteractionHandler
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem
import com.simibubi.create.foundation.block.IBE
import net.createmod.catnip.lang.Lang
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.common.util.FakePlayer
import org.teamvoided.createllaneous.init.CMBlockEntityTypes
import kotlin.math.max

class BreezeBreatherBlock(properties: Properties) :
    HorizontalDirectionalBlock(properties), IBE<BreezeBreatherBlockEntity>, IWrenchable {

    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = CODEC

    init {
        registerDefaultState(defaultBlockState().setValue(WIND_LEVEL, WindLevel.NONE))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(WIND_LEVEL, FACING)
    }

    override fun getBlockEntityClass(): Class<BreezeBreatherBlockEntity> = BreezeBreatherBlockEntity::class.java

    override fun getBlockEntityType(): BlockEntityType<out BreezeBreatherBlockEntity> =
        CMBlockEntityTypes.BREEZE_BREATHER_BLOCK_ENTITY.get()

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return if (state.getValue(WIND_LEVEL) == WindLevel.NONE) null
        else super.newBlockEntity(pos, state)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val wind = state.getValue(WIND_LEVEL)

        if (AllItems.GOGGLES.isIn(stack) && wind != WindLevel.NONE) return onBlockEntityUseItemOn(
            level, pos
        ) { bbte: BreezeBreatherBlockEntity ->
            if (bbte.goggles) return@onBlockEntityUseItemOn ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            bbte.goggles = true
            bbte.notifyUpdate()
            ItemInteractionResult.SUCCESS
        }

        val be: BreezeBreatherBlockEntity? = getBlockEntity(level, pos)
        if (be != null && be.stockKeeper) {
            val stockTicker = BreezeBreatherBlockEntity.getStockTicker(level, pos)
            if (stockTicker != null)
                StockTickerInteractionHandler.interactWithLogisticsManagerAt(player, level, stockTicker.blockPos)
            return ItemInteractionResult.SUCCESS
        }

        if (stack.isEmpty && wind != WindLevel.NONE) return onBlockEntityUseItemOn(
            level,
            pos
        ) { bbte: BreezeBreatherBlockEntity ->
            if (!bbte.goggles) return@onBlockEntityUseItemOn ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            bbte.goggles = false
            bbte.notifyUpdate()
            ItemInteractionResult.SUCCESS
        }

        //BRASIER BLOCK
        //if (wind == WindLevel.NONE) {
        //    if (stack.item is FlintAndSteelItem) {
        //        level.playSound(
        //            player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f,
        //            level.random.nextFloat() * 0.4f + 0.8f
        //        )
        //        if (level.isClientSide) return ItemInteractionResult.SUCCESS
        //        stack.hurtAndBreak(
        //            1,
        //            player,
        //            if (hand == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
        //        )
        //        level.setBlockAndUpdate(pos, AllBlocks.LIT_BLAZE_BURNER.defaultState)
        //        return ItemInteractionResult.SUCCESS
        //    }
        //    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        //}

        val doNotConsume = player.isCreative
        val forceOverflow = player !is FakePlayer

        val res = tryInsert(state, level, pos, stack, doNotConsume, forceOverflow, false)
        val leftover = res.getObject()
        if (!level.isClientSide && !doNotConsume && !leftover.isEmpty) {
            if (stack.isEmpty) {
                player.setItemInHand(hand, leftover)
            } else if (!player.inventory
                    .add(leftover)
            ) {
                player.drop(leftover, false)
            }
        }

        return if (res.result == InteractionResult.SUCCESS) ItemInteractionResult.SUCCESS else ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    fun tryInsert(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        stack: ItemStack,
        doNotConsume: Boolean,
        forceOverflow: Boolean,
        simulate: Boolean
    ): InteractionResultHolder<ItemStack> {
        if (!state.hasBlockEntity()) return InteractionResultHolder.fail(ItemStack.EMPTY)

        val be =
            world.getBlockEntity(pos) as? BreezeBreatherBlockEntity
                ?: return InteractionResultHolder.fail(ItemStack.EMPTY)

        if (be.isCreativeFuel(stack)) {
            if (!simulate) be.applyCreativeFuel()
            return InteractionResultHolder.success(ItemStack.EMPTY)
        }
        if (!be.tryUpdateFuel(stack, forceOverflow, simulate)) return InteractionResultHolder.fail(ItemStack.EMPTY)

        if (!doNotConsume) {
            val container = if (stack.hasCraftingRemainingItem()) stack.craftingRemainingItem else ItemStack.EMPTY
            if (!world.isClientSide) {
                stack.shrink(1)
            }
            return InteractionResultHolder.success(container)
        }
        return InteractionResultHolder.success(ItemStack.EMPTY)
    }


    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        val item = context.itemInHand.item
        val defaultState = defaultBlockState()
        if (item !is BlazeBurnerBlockItem) return defaultState
        val initialHeat = if (item.hasCapturedBlaze()) WindLevel.BREEZY else WindLevel.NONE
        return defaultState.setValue(WIND_LEVEL, initialHeat)
            .setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun getShape(
        state: BlockState,
        reader: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = AllShapes.HEATER_BLOCK_SHAPE

    override fun getCollisionShape(
        state: BlockState, getter: BlockGetter, pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        if (context === CollisionContext.empty()) return AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE
        return getShape(state, getter, pos, context)
    }


    public override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    public override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int =
        max(0, (state.getValue(WIND_LEVEL).ordinal - 1))

    override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean = false

    @OnlyIn(Dist.CLIENT)
    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(10) != 0) return
        if (!state.getValue(WIND_LEVEL).isAtLeast(WindLevel.BREEZY)) return
        world.playLocalSound(
            pos.x + 0.5, pos.y + 0.5, pos.z + 0.5,
            SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
            0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false
        )
    }

    enum class WindLevel : StringRepresentable {
        NONE, BREEZY, DWINDLING, SQUALL, GALE;

        /** NONE: Empty
         * BREEZY, Smouldering: Has breeze
         * DWINDLING, Fading: Has a small amount of wind time left
         * SQUALL, Kindled: Has wind time
         * GALE, Seething: Superheated
         */

        fun nextActiveLevel(): WindLevel {
            return byIndex(ordinal % (entries.size - 1) + 1)
        }

        fun isAtLeast(windLevel: WindLevel): Boolean {
            return this.ordinal >= windLevel.ordinal
        }

        override fun getSerializedName(): String {
            return Lang.asId(name)
        }

        companion object {
            val CODEC: Codec<WindLevel> = StringRepresentable.fromEnum { entries.toTypedArray() }
            fun byIndex(index: Int): WindLevel {
                return entries[index]
            }
        }
    }

    class BreezeBreatherConductor : ConductorBlockInteractionBehavior() {
        override fun isValidConductor(state: BlockState): Boolean = state.getValue(WIND_LEVEL) != WindLevel.NONE
    }

    companion object {
        val CODEC: MapCodec<BreezeBreatherBlock> =
            simpleCodec { properties: Properties -> BreezeBreatherBlock(properties) }
        val WIND_LEVEL: EnumProperty<WindLevel> = EnumProperty.create("breeze", WindLevel::class.java)


        fun getWindLevelOf(blockState: BlockState): WindLevel {
            return if (blockState.hasProperty(WIND_LEVEL)) blockState.getValue(WIND_LEVEL)
            else WindLevel.NONE
        }
    }
}