package org.teamvoided.createllaneous.mixin.skb;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.createllaneous.api.StockKeeperBlock;

@Mixin(StockTickerBlockEntity.class)
public abstract class StockTickerBlockEntityMixin extends BlockEntity {
    public StockTickerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @ModifyReturnValue(method = "isKeeperPresent", at = @At(value = "RETURN", ordinal = 2))
    boolean modifyInit(boolean original) {
        if (level == null) return original;
        for (Direction side : Iterate.horizontalDirections) {
            BlockPos seatPos = worldPosition.relative(side);
            if (level.getBlockEntity(seatPos) instanceof StockKeeperBlock) {
                return true;
            }
        }
        return original;
    }
}
