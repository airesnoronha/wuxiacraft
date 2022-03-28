package com.lazydragonstudios.wuxiacraft.client.gui;

import com.lazydragonstudios.wuxiacraft.container.SpatialItemMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class SpatialItemScreen extends InventoryScreen {

    private static SpatialItemMenu oldItemMenu;

    public SpatialItemScreen(Player player) {
        super(player);
    }

    public SpatialItemScreen(SpatialItemMenu spatialItemMenu, Inventory inventory, Component title) {
        super(inventory.player);
    }

    public static Player swapItemMenu(Player player, SpatialItemMenu newItemMenu) {
        
        return player;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack p_98889_, int p_98890_, int p_98891_) {
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
