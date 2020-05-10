package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class ItemSpaceRing extends Item {

    private SpaceRingProvider spaceRingProvider;

    public ItemSpaceRing(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        WuxiaItems.ITEMS.add(this);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        NBTTagCompound tag = new NBTTagCompound();
        spaceRingProvider.deserializeNBT(tag);
        playerIn.openGui(WuxiaCraft.instance, GuiHandler.SPACE_RING_GUI_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        spaceRingProvider.serializeNBT();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if(stack.getItem() instanceof ItemSpaceRing) {
            return new SpaceRingProvider();
        }
        return null;
    }
}
