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
        if (id > END_ID) return super.triggerEvent(id, type)

        return when (id) {
            END_ID -> {
                ringingTicks = 0
                level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, false))
                Minecraft.getInstance().player?.sendSystemMessage(Component.literal("end"))
                true
            }

            SMALL_HIT_ID -> if (ringCooldown > 0) true else doSyncThings(50, type)
            REDSTONE_ID -> doSyncThings(500, type)
            else -> {
                Minecraft.getInstance().player?.sendSystemMessage(Component.literal("an oopsie, $type is not a bell type, " + if (level!!.isClientSide) "client" else "server"))
                true
            }
        }
    }

    private fun doSyncThings(addTicks: Int, direction: Int): Boolean {
        this.clickDirection = Direction.from3DDataValue(direction)
        val newTicks = ringingTicks + addTicks
        if (newTicks < MAX_TICKS) ringingTicks = newTicks
        ringCooldown += COOLDOWN
        level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, true))

        //Minecraft.getInstance().player?.sendSystemMessage(Component.literal("$direction, $clickDirection, $ringingTicks, " + if (level!!.isClientSide) "client" else "server"))
        return true
    }

    fun onHit(direction: Direction?, type: Int) {
        val dir = direction ?: clickDirection ?: Direction.entries[level!!.random.nextInt(4) + 2]
        val blockpos = this.blockPos
        this.clickDirection = dir
        level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, true))
        if (!level!!.isClientSide)
            level!!.blockEvent(blockpos, blockState.block, type, dir.get3DDataValue())
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
    fun isPowered(): Boolean = blockState.getValue(LargeBellBlock.POWERED)

    override fun tick() {
        super.tick()
        if (blockState.getValue(LargeBellBlock.RINGING)) {
            if (ringingTicks <= 0) {
                level?.setBlockAndUpdate(blockPos, blockState.setValue(LargeBellBlock.RINGING, false))
                ringingTicks = 0
                clickDirection = null
                if (!level!!.isClientSide) level!!.blockEvent(blockPos, blockState.block, END_ID, 0)
            } else {
                if (ringingTicks % PERIOD == 0 && ringingTicks > 150) ring()
                if (isPowered() && ringingTicks < 400) ringingTicks += PERIOD
                ringingTicks--

                //Minecraft.getInstance().player?.sendSystemMessage(Component.literal((if (level!!.isClientSide) "client" else "server") + " ticking, $ringingTicks"))
            }
        }
        if (ringCooldown > 0) ringCooldown--
    }

    private fun ring() = level!!.playSound(null, blockPos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2f, 0f)

    companion object {
        const val COOLDOWN = 10
        const val MAX_TICKS = 800

        private const val PERIOD = 40

        const val SMALL_HIT_ID = 1
        const val REDSTONE_ID = 2
        const val DAMPEN_ID = 3
        const val END_ID = 10
    }
}