package org.teamvoided.createllaneous.content.breather

import com.simibubi.create.api.behaviour.movement.MovementBehaviour
import com.simibubi.create.content.contraptions.behaviour.MovementContext
import com.simibubi.create.content.contraptions.render.ContraptionMatrices
import com.simibubi.create.content.trains.entity.CarriageContraption
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld
import net.createmod.catnip.animation.LerpedFloat
import net.createmod.catnip.data.Iterate
import net.createmod.catnip.math.AngleHelper
import net.createmod.catnip.math.VecHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

class BreezeBreatherMovementBehavior : MovementBehaviour {
    override fun canBeDisabledVia(context: MovementContext): ItemStack? = null

    override fun tick(context: MovementContext) {
        if (!context.world.isClientSide()) return

        val r = context.world.getRandom()
        val c = context.position
        val v = c.add(
            VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
                .multiply(1.0, 0.0, 1.0)
        )
        if (r.nextInt(3) == 0 && context.motion.length() < 1 / 64f) context.world.addParticle(
            ParticleTypes.LARGE_SMOKE,
            v.x,
            v.y,
            v.z,
            0.0,
            0.0,
            0.0
        )

        val headAngle = getHeadAngle(context)
        val quickTurn = shouldRenderHat(context) && !Mth.equal(context.relativeMotion.length(), 0.0)
        headAngle.chase(
            (headAngle.value + AngleHelper.getShortestAngleDiff(
                headAngle.value.toDouble(),
                getTargetAngle(context).toDouble()
            )).toDouble(), .5,
            if (quickTurn) LerpedFloat.Chaser.EXP else LerpedFloat.Chaser.exp(5.0)
        )
        headAngle.tickChaser()
    }

    fun invalidate(context: MovementContext) {
        context.data.remove("Conductor")
    }

    private fun getHeadAngle(context: MovementContext): LerpedFloat {
        if (context.temporaryData !is LerpedFloat) context.temporaryData = LerpedFloat.angular()
            .startWithValue(getTargetAngle(context).toDouble())
        return context.temporaryData as LerpedFloat
    }

    private fun getTargetAngle(context: MovementContext): Float {
        if (shouldRenderHat(context) && !Mth.equal(context.relativeMotion.length(), 0.0)
            && context.contraption.entity is CarriageContraptionEntity
        ) {
            val angle = AngleHelper.deg(-Mth.atan2(context.relativeMotion.x, context.relativeMotion.z))
            return if ((context.contraption.entity as CarriageContraptionEntity).initialOrientation
                    .axis == Direction.Axis.X
            ) angle + 180 else angle
        }

        val player = Minecraft.getInstance().cameraEntity
        if (player != null && !player.isInvisible && context.position != null) {
            val applyRotation = context.contraption.entity.reverseRotation(
                player.position()
                    .subtract(context.position), 1f
            )
            val dx = applyRotation.x
            val dz = applyRotation.z
            return AngleHelper.deg(-Mth.atan2(dz, dx)) - 90
        }
        return 0f
    }

    private fun shouldRenderHat(context: MovementContext): Boolean {
        val data = context.data
        if (!data.contains("Conductor")) data.putBoolean("Conductor", determineIfConducting(context))
        return (data.getBoolean("Conductor") && (context.contraption.entity is CarriageContraptionEntity)
                && (context.contraption.entity as CarriageContraptionEntity).hasSchedule())
    }

    private fun determineIfConducting(context: MovementContext): Boolean {
        val contraption =
            context.contraption as? CarriageContraption ?: return false
        val assemblyDirection = contraption.assemblyDirection
        for (direction in Iterate.directionsInAxis(assemblyDirection.axis)) if (contraption.inControl(
                context.localPos,
                direction
            )
        ) return true
        return false
    }

    override fun disableBlockEntityRendering(): Boolean = true

    @OnlyIn(Dist.CLIENT)
    override fun renderInContraption(
        context: MovementContext, renderWorld: VirtualRenderWorld,
        matrices: ContraptionMatrices, buffer: MultiBufferSource
    ) {
        BreezeBreatherRenderer.renderInContraption(
            context,
            matrices,
            buffer,
            getHeadAngle(context),
            shouldRenderHat(context)
        )
    }
}