package com.laffeynyaa.bocchichan.handler;

import com.laffeynyaa.bocchichan.BocchiChan;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class BocchiScreenHandler extends ScreenHandler {
    public BocchiScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(BocchiChan.BOCCHI_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    public static ScreenHandlerType<BocchiScreenHandler> register() {
        return Registry.register(
                Registries.SCREEN_HANDLER,
                "bocchi_screen_handler",
                new ScreenHandlerType<>(BocchiScreenHandler::new, FeatureSet.empty()));
    }
}
