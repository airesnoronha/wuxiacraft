package com.airesnor.wuxiacraft.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import javafx.scene.control.TextFormatter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemHerb extends Item {

    private int[] herbElements;
    private int minTemperature;
    private int maxTemperature;
    private int herbRarity;

    public ItemHerb(String name, int herbRarity) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(WuxiaItems.WUXIACRAFT_GENERAL);
        setMaxStackSize(64);

        this.herbElements = new int[5];
        this.herbRarity = herbRarity;

        WuxiaHerbs.HERBS.add(this);
    }

    public ItemHerb setHerbElements(int water, int wood, int fire, int earth, int metal) {
        herbElements[0] = water;
        herbElements[1] = wood;
        herbElements[2] = fire;
        herbElements[3] = earth;
        herbElements[4] = metal;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.AQUA + "Water: " + this.herbElements[0]);
        tooltip.add(TextFormatting.GREEN + "Wood: " + this.herbElements[1]);
        tooltip.add(TextFormatting.RED + "Fire: " + this.herbElements[2]);
        tooltip.add(TextFormatting.GOLD + "Earth: " + this.herbElements[3]);
        tooltip.add(TextFormatting.GRAY + "Metal: " + this.herbElements[4]);
    }

    public ItemHerb setTemperatures(int minTemperature, int maxTemperature) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        return this;
    }

    public int getMaxTemperature() {
        return this.maxTemperature;
    }

    public int getMinTemperature() {
        return this.minTemperature;
    }

    public int[] getHerbElements() {
        return herbElements;
    }

    public int getHerbRarity() {
        return herbRarity;
    }

}
