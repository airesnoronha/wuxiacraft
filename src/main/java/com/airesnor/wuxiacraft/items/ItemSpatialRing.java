package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.WuxiaItemInventoryProvider;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemSpatialRing extends Item {

    private final int spatialRingSize;
    private final int spatialRingRows;
    private final int spatialRingColumns;

    public ItemSpatialRing(String name, int spatialRingRows, int spatialRingColumns) {
        setRegistryName(name);
        setUnlocalizedName(name);
        WuxiaItems.ITEMS.add(this);
        setCreativeTab(WuxiaItems.WUXIACRAFT_GENERAL);
        setMaxStackSize(1);

        this.spatialRingSize = spatialRingRows * spatialRingColumns;
        this.spatialRingRows = spatialRingRows;
        this.spatialRingColumns = spatialRingColumns;

        WuxiaItems.ITEMS.add(this);
    }

    public int getSpatialRingRows() {
        return spatialRingRows;
    }

    public int getSpatialRingColumns() {
        return spatialRingColumns;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.getHeldItemMainhand().getItem() == this) {
            playerIn.openGui(WuxiaCraft.instance, GuiHandler.SPATIAL_RING_GUI_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        ItemStackHandler inventory = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        NBTTagCompound tag = super.getNBTShareTag(stack);
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setTag("ClientSyncInventory", inventory.serializeNBT());
        return tag;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        NBTTagCompound tag = stack.getTagCompound();
        ItemStackHandler inventory = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        inventory.deserializeNBT(tag.getCompoundTag("ClientSyncInventory"));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if(stack.getItem() == this) {
            return new WuxiaItemInventoryProvider(this.spatialRingSize);
        }
        return null;
    }
}
