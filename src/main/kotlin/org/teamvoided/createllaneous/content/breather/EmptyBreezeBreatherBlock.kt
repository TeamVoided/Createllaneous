//package org.teamvoided.createllaneous.content.breather
//
//import com.simibubi.create.AllBlocks
//import com.simibubi.create.content.equipment.wrench.IWrenchable
//import net.minecraft.core.BlockPos
//import net.minecraft.world.level.BlockGetter
//import net.minecraft.world.level.block.Block
//import net.minecraft.world.level.block.state.BlockState
//import net.minecraft.world.phys.shapes.CollisionContext
//import net.minecraft.world.phys.shapes.VoxelShape
//
//class EmptyBreezeBreatherBlock(properties: Properties) : Block(properties), IWrenchable {
//
//
//    override fun getShape(
//        state: BlockState,
//        reader: BlockGetter,
//        pos: BlockPos,
//        context: CollisionContext
//    ): VoxelShape = AllBlocks.BLAZE_BURNER.get().getShape(state, reader, pos, context)
//}