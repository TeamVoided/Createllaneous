package org.teamvoided.createllaneous.content.breather

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.simibubi.create.AllPartialModels
import com.simibubi.create.content.contraptions.behaviour.MovementContext
import com.simibubi.create.content.contraptions.render.ContraptionMatrices
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import net.createmod.catnip.animation.AnimationTickHolder
import net.createmod.catnip.animation.LerpedFloat
import net.createmod.catnip.math.AngleHelper
import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SuperByteBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.createllaneous.client.CMPartialModels
import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock.WindLevel

class BreezeBreatherRenderer() :
    SafeBlockEntityRenderer<BreezeBreatherBlockEntity>() {

    protected override fun renderSafe(
        be: BreezeBreatherBlockEntity,
        partialTicks: Float,
        ms: PoseStack,
        bufferSource: MultiBufferSource,
        light: Int,
        overlay: Int,
    ) {
        val windLevel = be.windLevelFromBlock

        val level = be.level
        val blockState = be.blockState
        val animation = be.headAnimation.getValue(partialTicks) * .175f
        val horizontalAngle = AngleHelper.rad(be.headAngle.getValue(partialTicks).toDouble())
        val canDrawFlame = windLevel.isAtLeast(WindLevel.DWINDLING)
        val drawGoggles = be.goggles
        val drawHat =
            if (be.hat) AllPartialModels.TRAIN_HAT else (if (be.stockKeeper) AllPartialModels.LOGISTICS_HAT else null)
        val hashCode = be.hashCode()

        renderShared(
            ms, null, bufferSource,
            level, blockState, windLevel, animation, horizontalAngle,
            canDrawFlame, drawGoggles, drawHat, hashCode
        )
    }


    companion object {
        fun getBreezeModel(windLevel: WindLevel): PartialModel {
            return if (windLevel.isAtLeast(WindLevel.GALE)) {
                CMPartialModels.BREEZE_SUPER
            } else if (windLevel.isAtLeast(WindLevel.SQUALL)) {
                CMPartialModels.BREEZE_IDLE
            } else if (windLevel.isAtLeast(WindLevel.DWINDLING)) {
                CMPartialModels.BREEZE_INERT
            } else {
                CMPartialModels.BREEZE_TINY
            }
        }

        fun renderShared(
            ms: PoseStack, modelTransform: PoseStack?, bufferSource: MultiBufferSource,
            level: Level?, blockState: BlockState, windLevel: WindLevel, animation: Float, horizontalAngle: Float,
            canDrawFlame: Boolean, drawGoggles: Boolean, drawHat: PartialModel?, hashCode: Int,
        ) {
            val time = AnimationTickHolder.getRenderTime(level)
            val renderTick = time + (hashCode % 13) * 16f
            val offsetMult = (if (windLevel.isAtLeast(WindLevel.DWINDLING)) 64 else 16).toFloat()
            val offset = Mth.sin(((renderTick / 16f) % (2 * Math.PI)).toFloat()) / offsetMult
            val offset1 = Mth.sin(((renderTick / 16f + Math.PI) % (2 * Math.PI)).toFloat()) / offsetMult
            val offset2 = Mth.sin(((renderTick / 16f + Math.PI / 2) % (2 * Math.PI)).toFloat()) / offsetMult
            val headY = offset - (animation * .75f)

            ms.pushPose()

            val breezeModel = getBreezeModel(windLevel)

            val breezeBuffer = CachedBuffers.partial(breezeModel, blockState)
            if (modelTransform != null) breezeBuffer.transform(modelTransform)
            breezeBuffer.translate(0f, headY, 0f)
            draw(breezeBuffer, horizontalAngle, ms, bufferSource.getBuffer(RenderType.solid()))

            if (drawGoggles) {
                val gogglesModel = if (breezeModel == CMPartialModels.BREEZE_INERT) AllPartialModels.BLAZE_GOGGLES_SMALL
                else AllPartialModels.BLAZE_GOGGLES

                val gogglesBuffer = CachedBuffers.partial(gogglesModel, blockState)
                if (modelTransform != null) gogglesBuffer.transform(modelTransform)
                gogglesBuffer.translate(0f, headY + 8 / 16f, 0f)
                draw(gogglesBuffer, horizontalAngle, ms, bufferSource.getBuffer(RenderType.solid()))
            }

            if (drawHat != null) {
                val hatBuffer = CachedBuffers.partial(drawHat, blockState)
                if (modelTransform != null) hatBuffer.transform(modelTransform)
                hatBuffer.translate(0f, headY, 0f)
                if (breezeModel == CMPartialModels.BREEZE_INERT) {
                    hatBuffer.translateY(0.5f)
                        .center()
                        .scale(0.75f)
                        .uncenter()
                } else {
                    hatBuffer.translateY(0.75f)
                }
                val cutout = bufferSource.getBuffer(RenderType.cutoutMipped())
                hatBuffer
                    .rotateCentered(horizontalAngle + Mth.PI, Direction.UP)
                    .translate(0.5f, 0f, 0.5f)
                    .light<SuperByteBuffer>(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, cutout)
            }

            /*
            if (windLevel.isAtLeast(WindLevel.DWINDLING)) {
                val rodsModel = if (windLevel == WindLevel.GALE) AllPartialModels.BLAZE_BURNER_SUPER_RODS
                else AllPartialModels.BLAZE_BURNER_RODS
                val rodsModel2 = if (windLevel == WindLevel.GALE) AllPartialModels.BLAZE_BURNER_SUPER_RODS_2
                else AllPartialModels.BLAZE_BURNER_RODS_2

                val rodsBuffer = CachedBuffers.partial(rodsModel, blockState)
                if (modelTransform != null) rodsBuffer.transform(modelTransform)
                rodsBuffer.translate(0f, offset1 + animation + .125f, 0f)
                    .light<SuperByteBuffer>(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()))

                val rodsBuffer2 = CachedBuffers.partial(rodsModel2, blockState)
                if (modelTransform != null) rodsBuffer2.transform(modelTransform)
                rodsBuffer2.translate(0f, offset2 + animation - 3 / 16f, 0f)
                    .light<SuperByteBuffer>(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()))
            }

            if (canDrawFlame && blockAbove) {
                val spriteShift =
                    if (windLevel == WindLevel.GALE) AllSpriteShifts.SUPER_BURNER_FLAME else AllSpriteShifts.BURNER_FLAME

                val spriteWidth = (spriteShift.target.u1 - spriteShift.target.u0)

                val spriteHeight = (spriteShift.target.v1 - spriteShift.target.v0)

                val speed = 1 / 32f + 1 / 64f * windLevel.ordinal

                var vScroll = (speed * time).toDouble()
                vScroll -= floor(vScroll)
                vScroll *= spriteHeight / 2

                var uScroll = (speed * time / 2).toDouble()
                uScroll -= floor(uScroll)
                uScroll *= spriteWidth / 2

                val flameBuffer = CachedBuffers.partial(AllPartialModels.BLAZE_BURNER_FLAME, blockState)
                if (modelTransform != null) flameBuffer.transform(modelTransform)
                flameBuffer.shiftUVScrolling<SuperByteBuffer>(spriteShift, uScroll.toFloat(), vScroll.toFloat())

                val cutout = bufferSource.getBuffer(RenderType.cutoutMipped())
                draw(flameBuffer, horizontalAngle, ms, cutout)
            }
            */
            ms.popPose()
        }

        fun renderInContraption(
            context: MovementContext,
            matrices: ContraptionMatrices,
            bufferSource: MultiBufferSource,
            headAngle: LerpedFloat,
            conductor: Boolean
        ) {
            val state = context.state
            val windLevel = BreezeBreatherBlock.getWindLevelOf(state)

            val level = context.world
            val horizontalAngle =
                AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)).toDouble())
            val drawGoggles = context.blockEntityData.contains("Goggles")
            val drawHat = conductor || context.blockEntityData.contains("TrainHat")
            val hashCode = context.hashCode()

            renderShared(
                matrices.viewProjection, matrices.model, bufferSource,
                level, state, windLevel, 0f, horizontalAngle,
                false, drawGoggles, if (drawHat) AllPartialModels.TRAIN_HAT else null, hashCode
            )
        }

        private fun draw(buffer: SuperByteBuffer, horizontalAngle: Float, ms: PoseStack, vc: VertexConsumer) {
            buffer.rotateCentered(horizontalAngle, Direction.UP)
                .light<SuperByteBuffer>(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc)
        }
    }
}