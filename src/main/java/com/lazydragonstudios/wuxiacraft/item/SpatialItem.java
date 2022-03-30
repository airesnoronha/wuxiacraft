package com.lazydragonstudios.wuxiacraft.item;

import com.lazydragonstudios.wuxiacraft.capabilities.SpatialItemInventoryProvider;
import com.lazydragonstudios.wuxiacraft.client.gui.SpatialItemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpatialItem extends Item {

    private int spatialItemSize;
    private int spatialItemRows;
    private int spatialItemColumns;

    public SpatialItem(Properties properties, int spatialItemRows, int spatialItemColumns) {
        super(properties);
        this.spatialItemSize = spatialItemRows * spatialItemColumns;
        this.spatialItemRows = spatialItemRows;
        this.spatialItemColumns = spatialItemColumns;
    }

    public int getSpatialItemRows() {
        return spatialItemRows;
    }

    public int getSpatialItemColumns() {
        return spatialItemColumns;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player.getMainHandItem().getItem() == this) {

        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

//    @Nullable
//    @Override
//    public CompoundTag getShareTag(ItemStack stack) {
//        var inventoryCapability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//        var inventory = (ItemStackHandler) inventoryCapability.orElse(new ItemStackHandler());
//        CompoundTag tag = super.getShareTag(stack);
//        if (tag == null) {
//            tag = new CompoundTag();
//        }
//        tag.put("clientSyncInventory", inventory.serializeNBT());
//        return tag;
//    }
//
//    @Override
//    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
//        super.readShareTag(stack, nbt);
//        var inventoryCapability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//        var inventory = (ItemStackHandler) inventoryCapability.orElse(new ItemStackHandler());
//        if (nbt != null && nbt.contains("clientSyncInventory")) {
//            inventory.deserializeNBT(nbt.getCompound("clientSyncInventory"));
//        }
//    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (stack.getItem() == this) {
            return new SpatialItemInventoryProvider(this.spatialItemSize);
        }
        return null;
    }
}
