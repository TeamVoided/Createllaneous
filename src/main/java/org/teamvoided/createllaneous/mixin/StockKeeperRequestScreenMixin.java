package org.teamvoided.createllaneous.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestMenu;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.createmod.catnip.data.Iterate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.createllaneous.api.StockKeeperBlock;

import java.lang.ref.WeakReference;

@Mixin(StockKeeperRequestScreen.class)
public abstract class StockKeeperRequestScreenMixin extends AbstractContainerScreen<StockKeeperRequestMenu> {
    public StockKeeperRequestScreenMixin(StockKeeperRequestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Shadow
    StockTickerBlockEntity blockEntity;
    @Shadow
    private int windowHeight;
    @Shadow private WeakReference<BlazeBurnerBlockEntity> blaze;
    @Unique
    WeakReference<StockKeeperBlock> createllaneous$stockKeeperBlock = new WeakReference<>(null);


    @Inject(method = "<init>", at = @At("TAIL"))
    void modifyInit(StockKeeperRequestMenu container, Inventory inv, Component title, CallbackInfo ci) {
        // Find the custom keeper for rendering
        for (Direction side : Iterate.horizontalDirections) {
            BlockPos seatPos = blockEntity.getBlockPos().relative(side);

            if (blockEntity.getLevel().getBlockEntity(seatPos) instanceof StockKeeperBlock keeper) {
                createllaneous$stockKeeperBlock = new WeakReference<>(keeper);
                return;
            }
        }
    }


    @WrapOperation(method = "containerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;closeContainer()V"))
    void cancelCloseInv(Player instance, Operation<Void> original) {
        var keeper = createllaneous$stockKeeperBlock.get();
        if (keeper != null && keeper.isValid()) return;

        original.call(instance);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    void callCustomRenderer(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        var blockKeeper = createllaneous$stockKeeperBlock.get();
        if (blockKeeper != null && blockKeeper.isValid()) {
            var ms = graphics.pose();
            ms.pushPose();
            blockKeeper.render(blockKeeper, graphics, ms,  getGuiLeft(), getGuiTop(), windowHeight);
            ms.popPose();
        }
    }
}
