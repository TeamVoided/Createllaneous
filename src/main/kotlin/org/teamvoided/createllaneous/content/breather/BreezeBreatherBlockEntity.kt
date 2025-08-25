package org.teamvoided.createllaneous.content.breather

import com.mojang.blaze3d.platform.Lighting
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import com.simibubi.create.AllBlocks
import com.simibubi.create.AllPartialModels
import com.simibubi.create.content.fluids.tank.FluidTankBlock
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity
import com.simibubi.create.content.processing.basin.BasinBlock
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import dev.engine_room.flywheel.api.visualization.VisualizationManager
import net.createmod.catnip.animation.AnimationTickHolder
import net.createmod.catnip.animation.LerpedFloat
import net.createmod.catnip.data.Iterate
import net.createmod.catnip.math.AngleHelper
import net.createmod.catnip.math.VecHelper
import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SuperByteBuffer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.teamvoided.createllaneous.api.StockKeeperBlock
import org.teamvoided.createllaneous.client.CMPartialModels
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock.WindLevel
import org.teamvoided.createllaneous.init.CMBlockEntityTypes

open class BreezeBreatherBlockEntity(pos: BlockPos, state: BlockState) :
    SmartBlockEntity(CMBlockEntityTypes.BREEZE_BREATHER_BLOCK_ENTITY.get(), pos, state), StockKeeperBlock {
    var headAnimation: LerpedFloat
    var stockKeeper: Boolean = false
    var goggles: Boolean = false
    var hat: Boolean = false

    var headAngle: LerpedFloat


    init {
        headAnimation = LerpedFloat.linear()
        headAngle = LerpedFloat.angular()

        headAngle.startWithValue(
            ((AngleHelper.horizontalAngle(
                state.getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.SOUTH)
            ) + 180) % 360).toDouble()
        )
    }

    override fun tick() {
        super.tick()

        if (level!!.isClientSide) {
            if (shouldTickAnimation()) tickAnimation()
            if (!isVirtual) spawnParticles(windLevelFromBlock, 1.0)
            return
        }
    }

    override fun lazyTick() {
        super.lazyTick()
        stockKeeper = getStockTicker(level!!, worldPosition) != null
    }

    @OnlyIn(Dist.CLIENT)
    private fun shouldTickAnimation(): Boolean {
        // Offload the animation tick to the visual when flywheel in enabled
        return !VisualizationManager.supportsVisualization(level)
    }

    @OnlyIn(Dist.CLIENT)
    fun tickAnimation() {
        val active = windLevelFromBlock.isAtLeast(WindLevel.DWINDLING) && isValidBlockAbove

        if (!active) {
            var target = 0f
            val player = Minecraft.getInstance().player
            if (player != null && !player.isInvisible) {
                val x: Double
                val z: Double
                if (isVirtual) {
                    x = -4.0
                    z = -10.0
                } else {
                    x = player.x
                    z = player.z
                }
                val dx = x - (blockPos.x + 0.5)
                val dz = z - (blockPos.z + 0.5)
                target = AngleHelper.deg(-Mth.atan2(dz, dx)) - 90
            }
            target = headAngle.value + AngleHelper.getShortestAngleDiff(headAngle.value.toDouble(), target.toDouble())
            headAngle.chase(target.toDouble(), .25, LerpedFloat.Chaser.exp(5.0))
            headAngle.tickChaser()
        } else {
            headAngle.chase(
                ((AngleHelper.horizontalAngle(
                    blockState.getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.SOUTH)
                ) + 180) % 360).toDouble(), .125, LerpedFloat.Chaser.EXP
            )
            headAngle.tickChaser()
        }

        headAnimation.chase((if (active) 1 else 0).toDouble(), .25, LerpedFloat.Chaser.exp(.25))
        headAnimation.tickChaser()
    }

    override fun addBehaviours(behaviours: List<BlockEntityBehaviour>) {}

    public override fun write(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        if (goggles) compound.putBoolean("Goggles", true)
        if (hat) compound.putBoolean("TrainHat", true)
        super.write(compound, registries, clientPacket)
    }

    override fun read(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        goggles = compound.contains("Goggles")
        hat = compound.contains("TrainHat")
        super.read(compound, registries, clientPacket)
    }

    val windLevelFromBlock: WindLevel
        get() = BreezeBreatherBlock.getWindLevelOf(blockState)

    fun getWindLevelForRender(): WindLevel {
        val windLevel = windLevelFromBlock
        if (!windLevel.isAtLeast(WindLevel.DWINDLING) && stockKeeper) return WindLevel.DWINDLING
        return windLevel
    }

    protected fun setBlockWind(wind: WindLevel) {
        val inBlockState = windLevelFromBlock
        if (inBlockState == wind) return
        level!!.setBlockAndUpdate(worldPosition, blockState.setValue(BreezeBreatherBlock.WIND_LEVEL, wind))
        notifyUpdate()
    }

    val isValidBlockAbove: Boolean
        get() {
            if (isVirtual) return false
            val blockState = level!!.getBlockState(worldPosition.above())
            return BasinBlock.isBasin(level, worldPosition.above()) || blockState.block is FluidTankBlock
        }

    fun playSound() {
        level!!.playSound(
            null, worldPosition, SoundEvents.BREEZE_SHOOT, SoundSource.BLOCKS,
            .125f + level!!.random.nextFloat() * .125f, .75f - level!!.random.nextFloat() * .25f
        )
    }

    protected fun spawnParticles(windLevel: WindLevel, burstMult: Double) {
        /*
        if (level == null) return
        if (windLevel == WindLevel.NONE) return

        val r = level!!.getRandom()

        val c = VecHelper.getCenterOf(worldPosition)
        val v = c.add(
            VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
                .multiply(1.0, 0.0, 1.0)
        )

        if (r.nextInt(4) != 0) return

        val empty = level!!.getBlockState(worldPosition.above())
            .getCollisionShape(level, worldPosition.above())
            .isEmpty

        if (empty || r.nextInt(8) == 0) level!!.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y, v.z, 0.0, 0.0, 0.0)

        val yMotion = if (empty) .0625 else r.nextDouble() * .0125f
        val v2 = c.add(
            VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
                .multiply(1.0, .25, 1.0)
                .normalize()
                .scale((if (empty) .25 else .5) + r.nextDouble() * .125f)
        )
            .add(0.0, .5, 0.0)

        if (windLevel.isAtLeast(WindLevel.GALE)) {
            level!!.addParticle(ParticleTypes.SOUL_FIRE_FLAME, v2.x, v2.y, v2.z, 0.0, yMotion, 0.0)
        } else if (windLevel.isAtLeast(WindLevel.DWINDLING)) {
            level!!.addParticle(ParticleTypes.FLAME, v2.x, v2.y, v2.z, 0.0, yMotion, 0.0)
        }
        return
        */
    }

    fun spawnParticleBurst(soulFlame: Boolean) {
        val c = VecHelper.getCenterOf(worldPosition)
        val r = level!!.random
        repeat(if (soulFlame) 5 else 19) {
            val offset = VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
                .multiply(1.0, .25, 1.0)
                .normalize()
            val v = c.add(offset.scale(.5 + r.nextDouble() * .125f))
                .add(0.0, .125, 0.0)
            val m = offset.scale((1 / 32f).toDouble())

            level!!.addParticle(
                if (soulFlame) ParticleTypes.GUST else ParticleTypes.SMALL_GUST,
                v.x, v.y, v.z, m.x, m.y, m.z
            )
        }
    }

    override fun isValid(): Boolean = !isRemoved
    override fun render(
        keeperBlock: StockKeeperBlock,
        graphics: GuiGraphics,
        matrix: PoseStack,
        x: Int,
        y: Int,
        windowHeight: Int,
    ) {
        val keeperBE = keeperBlock
        if (keeperBE !is BreezeBreatherBlockEntity) return

        matrix.pushPose()
        val entityX = x - 35
        val entityY = y + windowHeight - 43
        matrix.translate(entityX.toFloat(), entityY.toFloat(), -0f)
        matrix.mulPose(Axis.XP.rotationDegrees(-22.5f))
        matrix.mulPose(Axis.YP.rotationDegrees(-45f))
        matrix.scale(48f, -48f, 48f)
        val animation = keeperBE.headAnimation.getValue(AnimationTickHolder.getPartialTicks()) * .175f
        val horizontalAngle = AngleHelper.rad(270.0)
        val heatLevel = keeperBE.getWindLevelForRender()
        val canDrawFlame = heatLevel.isAtLeast(WindLevel.DWINDLING)
        val drawGoggles = keeperBE.goggles
        val drawHat = AllPartialModels.LOGISTICS_HAT
        val hashCode = keeperBE.hashCode()
        Lighting.setupForEntityInInventory()

        val cutout: VertexConsumer = graphics.bufferSource().getBuffer(RenderType.cutoutMipped())
        CachedBuffers.partial(CMPartialModels.BREEZE_CAGE, keeperBE.blockState)
            .rotateCentered(horizontalAngle + Mth.PI, Direction.UP)
            .light<SuperByteBuffer>(LightTexture.FULL_BRIGHT)
            .renderInto(matrix, cutout)

        BreezeBreatherRenderer.renderShared(
            matrix, null, graphics.bufferSource(), Minecraft.getInstance().level,
            keeperBE.blockState, heatLevel, animation, horizontalAngle, canDrawFlame, drawGoggles, drawHat,
            hashCode
        )
        Lighting.setupFor3DItems()
        matrix.popPose()
    }

    enum class FuelType {
        NONE, NORMAL, SPECIAL
    }

    companion object {
        const val MAX_HEAT_CAPACITY: Int = 10000
        const val INSERTION_THRESHOLD: Int = 500

        fun getStockTicker(level: LevelAccessor, pos: BlockPos): StockTickerBlockEntity? {
            for (direction in Iterate.horizontalDirections) {
                if (level is Level && !level.isLoaded(pos)) return null
                val blockState = level.getBlockState(pos.relative(direction))
                if (!AllBlocks.STOCK_TICKER.has(blockState)) continue //Ask ender what this does
                val stbe = level.getBlockEntity(pos.relative(direction))
                if (stbe is StockTickerBlockEntity) return stbe
            }
            return null
        }
    }
}