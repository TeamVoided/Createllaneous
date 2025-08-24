//package org.teamvoided.createllaneous.content.breather
//
//import com.simibubi.create.AllPartialModels
//import com.simibubi.create.AllSpriteShifts
//import com.simibubi.create.content.processing.burner.ScrollInstance
//import com.simibubi.create.foundation.render.AllInstanceTypes
//import dev.engine_room.flywheel.api.instance.Instance
//import dev.engine_room.flywheel.api.visual.DynamicVisual
//import dev.engine_room.flywheel.api.visual.TickableVisual
//import dev.engine_room.flywheel.api.visualization.VisualizationContext
//import dev.engine_room.flywheel.lib.instance.InstanceTypes
//import dev.engine_room.flywheel.lib.instance.TransformedInstance
//import dev.engine_room.flywheel.lib.model.Models
//import dev.engine_room.flywheel.lib.transform.Translate
//import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual
//import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual
//import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual
//import net.createmod.catnip.animation.AnimationTickHolder
//import net.createmod.catnip.math.AngleHelper
//import net.minecraft.client.renderer.LightTexture
//import net.minecraft.core.Direction
//import net.minecraft.util.Mth
//import org.teamvoided.createllaneous.content.breather.BreezeBreatherBlock.WindLevel
//import java.util.function.Consumer
//
//class BreezeBreatherVisual(ctx: VisualizationContext?, blockEntity: BreezeBreatherBlockEntity, partialTick: Float) :
//    AbstractBlockEntityVisual<BreezeBreatherBlockEntity>(ctx, blockEntity, partialTick),
//    SimpleDynamicVisual, SimpleTickableVisual {
//    private var windLevel: WindLevel
//
//    private val head: TransformedInstance
//
//    private val isInert: Boolean
//
//    private var smallRods: TransformedInstance? = null
//    private var largeRods: TransformedInstance? = null
//    private var flame: ScrollInstance? = null
//    private var goggles: TransformedInstance? = null
//    private var hat: TransformedInstance? = null
//
//    private var validBlockAbove: Boolean
//
//    init {
//        windLevel = WindLevel.BREEZY
//        validBlockAbove = blockEntity.isValidBlockAbove
//
//        val blazeModel = BreezeBreatherRenderer.getBreezeModel(windLevel, validBlockAbove)
//        isInert = blazeModel == AllPartialModels.BLAZE_INERT
//
//        head = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(blazeModel))
//            .createInstance()
//
//        head.light(LightTexture.FULL_BRIGHT)
//
//        animate(partialTick)
//    }
//
//    override fun tick(context: TickableVisual.Context) {
//        blockEntity!!.tickAnimation()
//    }
//
//    override fun beginFrame(ctx: DynamicVisual.Context) {
//        if (!isVisible(ctx.frustum()) || doDistanceLimitThisFrame(ctx)) {
//            return
//        }
//
//        animate(ctx.partialTick())
//    }
//
//    private fun animate(partialTicks: Float) {
//        val animation = blockEntity!!.headAnimation.getValue(partialTicks) * .175f
//
//        val validBlockAbove = animation > 0.125f
//        val heatLevel = blockEntity!!.heatLevelForRender
//
//        if (validBlockAbove != this.validBlockAbove || heatLevel != this.windLevel) {
//            this.validBlockAbove = validBlockAbove
//
//            val blazeModel = BreezeBreatherRenderer.getBreezeModel(heatLevel, validBlockAbove)
//            instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(blazeModel))
//                .stealInstance(head)
//
//            val needsRods = heatLevel.isAtLeast(WindLevel.DWINDLING)
//            val hasRods = this.windLevel.isAtLeast(WindLevel.DWINDLING)
//
//            if (needsRods && !hasRods) {
//                val rodsModel = if (heatLevel == WindLevel.GALE) AllPartialModels.BLAZE_BURNER_SUPER_RODS
//                else AllPartialModels.BLAZE_BURNER_RODS
//                val rodsModel2 = if (heatLevel == WindLevel.GALE) AllPartialModels.BLAZE_BURNER_SUPER_RODS_2
//                else AllPartialModels.BLAZE_BURNER_RODS_2
//
//                smallRods = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(rodsModel))
//                    .createInstance()
//                largeRods = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(rodsModel2))
//                    .createInstance()
//
//                smallRods!!.light(LightTexture.FULL_BRIGHT)
//                largeRods!!.light(LightTexture.FULL_BRIGHT)
//            } else if (!needsRods && hasRods) {
//                if (smallRods != null) smallRods!!.delete()
//                if (largeRods != null) largeRods!!.delete()
//                smallRods = null
//                largeRods = null
//            }
//
//            this.windLevel = heatLevel
//        }
//
//        // Switch between showing/hiding the flame
//        if (validBlockAbove && flame == null) {
//            setupFlameInstance()
//        } else if (!validBlockAbove && flame != null) {
//            flame!!.delete()
//            flame = null
//        }
//
//        if (blockEntity!!.goggles && goggles == null) {
//            goggles = instancerProvider().instancer(
//                InstanceTypes.TRANSFORMED,
//                Models.partial(if (isInert) AllPartialModels.BLAZE_GOGGLES_SMALL else AllPartialModels.BLAZE_GOGGLES)
//            ).createInstance()
//            goggles!!.light(LightTexture.FULL_BRIGHT)
//        } else if (!blockEntity!!.goggles && goggles != null) {
//            goggles!!.delete()
//            goggles = null
//        }
//
//        val hatPresent = blockEntity!!.hat || blockEntity!!.stockKeeper
//        if (hatPresent && hat == null) {
//            hat = instancerProvider()
//                .instancer(
//                    InstanceTypes.TRANSFORMED,
//                    Models.partial(
//                        if (blockEntity!!.stockKeeper) AllPartialModels.LOGISTICS_HAT else AllPartialModels.TRAIN_HAT
//                    )
//                )
//                .createInstance()
//            hat!!.light(LightTexture.FULL_BRIGHT)
//        } else if (!hatPresent && hat != null) {
//            hat!!.delete()
//            hat = null
//        }
//
//        val hashCode = blockEntity.hashCode()
//        val time = AnimationTickHolder.getRenderTime(level)
//        val renderTick = time + (hashCode % 13) * 16f
//        val offsetMult = (if (heatLevel.isAtLeast(WindLevel.DWINDLING)) 64 else 16).toFloat()
//        val offset = Mth.sin(((renderTick / 16f) % (2 * Math.PI)).toFloat()) / offsetMult
//        val headY = offset - (animation * .75f)
//
//        val horizontalAngle = AngleHelper.rad(blockEntity!!.headAngle.getValue(partialTicks).toDouble())
//
//        head.setIdentityTransform()
//            .translate(visualPosition)
//            .translateY(headY)
//            .translate(Translate.CENTER)
//            .rotateY(horizontalAngle)
//            .translateBack(Translate.CENTER)
//            .setChanged()
//
//        if (goggles != null) {
//            goggles!!.setIdentityTransform()
//                .translate(visualPosition)
//                .translateY(headY + 8 / 16f)
//                .translate(Translate.CENTER)
//                .rotateY(horizontalAngle)
//                .translateBack(Translate.CENTER)
//                .setChanged()
//        }
//
//        if (hat != null) {
//            hat!!.setIdentityTransform()
//                .translate(visualPosition)
//                .translateY(headY)
//                .translateY(0.75f)
//            hat!!.rotateCentered(horizontalAngle + Mth.PI, Direction.UP)
//                .translate(0.5f, 0f, 0.5f)
//                .light(LightTexture.FULL_BRIGHT)
//
//            hat!!.setChanged()
//        }
//
//        if (smallRods != null) {
//            val offset1 = Mth.sin(((renderTick / 16f + Math.PI) % (2 * Math.PI)).toFloat()) / offsetMult
//
//            smallRods!!.setIdentityTransform()
//                .translate(visualPosition)
//                .translateY(offset1 + animation + .125f)
//                .setChanged()
//        }
//
//        if (largeRods != null) {
//            val offset2 = Mth.sin(((renderTick / 16f + Math.PI / 2) % (2 * Math.PI)).toFloat()) / offsetMult
//
//            largeRods!!.setIdentityTransform()
//                .translate(visualPosition)
//                .translateY(offset2 + animation - 3 / 16f)
//                .setChanged()
//        }
//    }
//
//    private fun setupFlameInstance() {
//        flame = instancerProvider().instancer(
//            AllInstanceTypes.SCROLLING,
//            Models.partial(AllPartialModels.BLAZE_BURNER_FLAME)
//        ).createInstance()
//
//        flame!!.position(visualPosition).light(LightTexture.FULL_BRIGHT)
//
//        val spriteShift =
//            if (windLevel == WindLevel.GALE) AllSpriteShifts.SUPER_BURNER_FLAME else AllSpriteShifts.BURNER_FLAME
//
//        val spriteWidth = (spriteShift.target.u1 - spriteShift.target.u0)
//
//        val spriteHeight = (spriteShift.target.v1 - spriteShift.target.v0)
//
//        val speed = 1 / 32f + 1 / 64f * windLevel.ordinal
//
//        flame!!.speedU = speed / 2
//        flame!!.speedV = speed
//
//        flame!!.scaleU = spriteWidth / 2
//        flame!!.scaleV = spriteHeight / 2
//
//        flame!!.diffU = spriteShift.target.u0 - spriteShift.original.u0
//        flame!!.diffV = spriteShift.target.v0 - spriteShift.original.v0
//    }
//
//    override fun updateLight(partialTick: Float) {}
//
//    override fun collectCrumblingInstances(consumer: Consumer<Instance?>) {}
//
//    override fun _delete() {
//        head.delete()
//        if (smallRods != null) {
//            smallRods!!.delete()
//        }
//        if (largeRods != null) {
//            largeRods!!.delete()
//        }
//        if (flame != null) {
//            flame!!.delete()
//        }
//        if (goggles != null) {
//            goggles!!.delete()
//        }
//        if (hat != null) {
//            hat!!.delete()
//        }
//    }
//}