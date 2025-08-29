package org.teamvoided.createllaneous.content.large_bell

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.createllaneous.init.CMBlockEntityTypes

class LargeBellBlockEntity(pos: BlockPos, state: BlockState) :
    SmartBlockEntity(CMBlockEntityTypes.LARGE_BELL_BLOCK_ENTITY.get(), pos, state) {
    private var ringCooldown: Int = 0
    var ringingTicks: Int = 0
    var clickDirection: Direction? = null

    override fun triggerEvent(id: Int, type: Int): Boolean {
        if (id == 1) {
            val direction = type % 10
            val ticks = (type - direction) / 10
            this.clickDirection = Direction.from3DDataValue(direction)
            this.ringingTicks = ticks
            level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, true))
            return true
        } else if (id == 2) {
            ringingTicks = 0
            level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, false))
            return true
        } else {
            return super.triggerEvent(id, type)
        }
    }

    fun onHit(direction: Direction?, ticks: Int = 50) {
        val dir = direction ?: clickDirection ?: Direction.entries[level!!.random.nextInt(4) + 2]
        val blockpos = this.blockPos
        this.clickDirection = dir
        if (this.isRinging()) {
            this.ringingTicks += ticks
        } else {
            level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, true))
        }

        level!!.blockEvent(blockpos, blockState.block, 1, dir.get3DDataValue() + (ringingTicks * 10))
    }


    override fun addBehaviours(behaviours: MutableList<BlockEntityBehaviour>) {}

    public override fun write(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        if (ringCooldown != 0) compound.putInt("RingCooldown", ringCooldown)
        if (ringingTicks != 0) compound.putInt("RingingTicks", ringingTicks)
        super.write(compound, registries, clientPacket)
    }

    override fun read(compound: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        ringCooldown = compound.getInt("RingCooldown")
        ringingTicks = compound.getInt("RingingTicks")
        super.read(compound, registries, clientPacket)
    }

    //fun onHit(direction: Direction?, ticks: Int = 50) {
    //    if (level != null &&
    //        ringingTicks <= MAX_TICKS &&
    //        ringCooldown <= 0 &&
    //        (direction == null || direction.axis != Direction.Axis.Y)
    //    ) {
    //        level!!.setBlockAndUpdate(pos, blockState.setValue(LargeBellBlock.RINGING, true))
    //        ringingTicks += ticks
    //        ringCooldown = COOLDOWN
    //        clickDirection = direction ?: clickDirection ?: Direction.entries[level!!.random.nextInt(4) + 2]
    //    }
    //}

    fun isRinging(): Boolean = blockState.getValue(LargeBellBlock.RINGING)

    override fun tick() {
        super.tick()
        if (blockState.getValue(LargeBellBlock.RINGING)) {
            if (ringingTicks <= 0) {
                level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, false))
                ringingTicks = 0
                clickDirection = null
                level!!.blockEvent(blockPos, blockState.block, 2, 0)
            } else {
                if (ringingTicks % 40 == 0 && ringingTicks > 100)
                    ring(level!!)
                ringingTicks--

                if (level!!.isClientSide)
                    Minecraft.getInstance().player?.sendSystemMessage(Component.literal(ringingTicks.toString()))
            }
        }
        if (ringCooldown > 0) ringCooldown--
    }

    fun ring(level: Level) {
        level.playSound(null, blockPos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2f, 0f)
    }

    companion object {
        const val COOLDOWN = 10
        const val MAX_TICKS = 800

        private const val PERIOD = 40
    }
}