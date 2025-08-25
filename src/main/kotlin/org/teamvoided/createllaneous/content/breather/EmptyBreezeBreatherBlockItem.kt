package org.teamvoided.createllaneous.content.breather

import net.createmod.catnip.math.VecHelper
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.SpawnerBlockEntity
import net.minecraft.world.phys.Vec3
import org.teamvoided.createllaneous.data.tags.CMEntityTags
import org.teamvoided.createllaneous.init.CMBlocks
import org.teamvoided.createllaneous.mixin.BaseSpawnerAccessor
import org.teamvoided.createllaneous.utils.isIn
import javax.annotation.ParametersAreNonnullByDefault


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
open class EmptyBreezeBreatherBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val world = context.level
        val pos = context.clickedPos
        val be = world.getBlockEntity(pos)
        val player = context.player

        val spawner = getBaseSpawner(be) ?: return super.useOn(context)

        val possibleSpawns = spawner.cm_getSpawnPotentials().unwrap()
            .mapNotNull { it?.data() }
            .toMutableList()

        if (possibleSpawns.isEmpty()) {
            val data = spawner.cm_getNextSpawnData()
            if (data != null) possibleSpawns.add(data)
        }

        for (e in possibleSpawns) {
            val optionalEntity = EntityType.by(e.entityToSpawn())
            if (optionalEntity.isEmpty || !optionalEntity.get().isIn(CMEntityTags.BREEZE_BURNER_CAPTURABLE)) continue

            spawnCaptureEffects(world, VecHelper.getCenterOf(pos))
            if (world.isClientSide || player == null) return InteractionResult.SUCCESS

            giveBurnerItemTo(player, context.itemInHand, context.hand)
            return InteractionResult.SUCCESS
        }

        return super.useOn(context)
    }

    override fun interactLivingEntity(
        heldItem: ItemStack, player: Player, entity: LivingEntity,
        hand: InteractionHand,
    ): InteractionResult {
        if (!entity.isIn(CMEntityTags.BREEZE_BURNER_CAPTURABLE)) return InteractionResult.PASS

        val world = player.level()
        spawnCaptureEffects(world, entity.position())
        if (world.isClientSide) return InteractionResult.FAIL

        giveBurnerItemTo(player, heldItem, hand)
        entity.discard()
        return InteractionResult.FAIL
    }

    protected fun giveBurnerItemTo(player: Player, heldItem: ItemStack, hand: InteractionHand) {
        val filled = CMBlocks.BREEZE_BREATHER.toStack()
        if (!player.isCreative) heldItem.shrink(1)
        if (heldItem.isEmpty) {
            player.setItemInHand(hand, filled)
            return
        }
        player.getInventory()
            .placeItemBackInInventory(filled)
    }

    private fun spawnCaptureEffects(world: Level, vec: Vec3) {
        if (world.isClientSide) {
            for (i in 0..39) {
                val motion = VecHelper.offsetRandomly(Vec3.ZERO, world.random, .125f)
                world.addParticle(ParticleTypes.SMALL_GUST, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z)
                val circle = motion.multiply(1.0, 0.0, 1.0)
                    .normalize()
                    .scale(.5)
                world.addParticle(ParticleTypes.SMALL_GUST, circle.x, vec.y, circle.z, 0.0, -0.125, 0.0)
            }
            return
        }

        val soundPos = BlockPos.containing(vec)
        world.playSound(null, soundPos, SoundEvents.BREEZE_HURT, SoundSource.HOSTILE, .25f, .75f)
//        world.playSound(null, soundPos, SoundEvents.BREEZE_INHALE, SoundSource.HOSTILE, .5f, .75f)
    }

    fun getBaseSpawner(be: BlockEntity?): BaseSpawnerAccessor? {
        if (be is SpawnerBlockEntity) {
            return be.spawner as BaseSpawnerAccessor

        }
        // (ender) Needs Custom logic
//        if (be is TrialSpawnerBlockEntity) {
//            return be.spawner as BaseSpawnerAccessor
//        }

        return null
    }

}

