package org.teamvoided.createllaneous.content.large_bell

import com.simibubi.create.foundation.utility.CreateLang
import net.createmod.catnip.data.Pair
import net.createmod.catnip.outliner.Outliner
import net.createmod.catnip.platform.CatnipServices
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.AABB
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

class LargeBellBlockItem(block: Block, properties: Properties) :
    BlockItem(block, properties) {
    override fun place(ctx: BlockPlaceContext): InteractionResult {
        var result = super.place(ctx)
        if (result != InteractionResult.FAIL) return result
        val clickedFace = ctx.clickedFace
        result = super.place(BlockPlaceContext.at(ctx, ctx.clickedPos.relative(clickedFace), clickedFace))
        if (result == InteractionResult.FAIL && ctx.level.isClientSide())
            CatnipServices.PLATFORM.executeOnClientOnly { Runnable { showBounds(ctx) } }
        return result
    }

    @OnlyIn(Dist.CLIENT)
    fun showBounds(context: BlockPlaceContext) {
        val player = context.player
        if (player !is LocalPlayer) return
        val pos = context.clickedPos
        Outliner.getInstance().showAABB(Pair.of("bell", pos), AABB(pos).inflate(1.0))
            .colored(-0xa294)
        CreateLang.translate("large_bell.not_enough_space")
            .color(-0xa294)
            .sendStatus(player)
    }
}