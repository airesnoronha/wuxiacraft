package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.WuxiaItemInventoryProvider;
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

import javax.annotation.Nullable;

public class ItemSpatialRing extends Item {

    private int spatialRingSize;
    public static int spatialRingRows;
    public static int spatialRingColumns;

    public ItemSpatialRing(String name, int spatialRingRows, int spatialRingColumns) {
        setRegistryName(name);
        setUnlocalizedName(name);
        WuxiaItems.ITEMS.add(this);
        setCreativeTab(WuxiaItems.WUXIACRAFT_GENERAL);
        setMaxStackSize(1);

        this.spatialRingSize = spatialRingRows * spatialRingColumns;
        this.spatialRingRows = spatialRingRows;
        this.spatialRingColumns = spatialRingColumns;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            playerIn.openGui(WuxiaCraft.instance, GuiHandler.SPATIAL_RING_GUI_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        }
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if(stack.getItem() instanceof ItemSpatialRing) {
            return new WuxiaItemInventoryProvider(this.spatialRingSize);
        }
        return null;
    }
}
