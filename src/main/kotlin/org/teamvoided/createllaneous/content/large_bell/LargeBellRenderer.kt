package org.teamvoided.createllaneous.content.large_bell

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer
import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SuperByteBuffer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import org.joml.Quaternionf
import java.lang.Math.toRadians
import kotlin.math.sin

class LargeBellRenderer : SafeBlockEntityRenderer<LargeBellBlockEntity>() {

    override fun renderSafe(
        blockEntity: LargeBellBlockEntity,
        tickDelta: Float,
        ms: PoseStack,
        bufferSource: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {

        val blockState: BlockState = blockEntity.blockState
        var f1 = 0f
        var f2 = 0f
        if (blockState.getValue(LargeBellBlock.RINGING)) {
            val ticksRight = (blockEntity.ringingTicks - tickDelta) / 40
            val f3 = toRadians(ticksRight * sin(ticksRight * Math.PI)).toFloat() * 4
            when (blockEntity.clickDirection) {
                Direction.NORTH -> f1 = -f3
                Direction.SOUTH -> f1 = f3
                Direction.EAST -> f2 = -f3
                Direction.WEST -> f2 = f3
                else -> {}
            }
        }

        val vb: VertexConsumer = bufferSource.getBuffer(RenderType.cutoutMipped())
        val quaternionf = Quaternionf().rotateZ(f1).rotateX(f2)
        CachedBuffers.block(blockState)
            .rotateAround(quaternionf, 0.5f, 2f, 0.5f)
            .light<SuperByteBuffer>(255)
            .renderInto(ms, vb)

        //this.bellBody.xRot = f1
        //this.bellBody.zRot = f2
    }

    override fun getViewDistance(): Int = super.getViewDistance() * 4

    override fun getRenderBoundingBox(blockEntity: LargeBellBlockEntity): AABB =
        AABB(blockEntity.blockPos).inflate(if (blockEntity.isRinging()) 3.0 else 1.0)

}