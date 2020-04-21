package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.items.ItemSpiritStone;
import com.airesnor.wuxiacraft.items.Items;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpiritVeinOre extends BlockBase {

	private Item droppedItem;

	public SpiritVeinOre(String name) {
		super(name, Material.ROCK);
		this.setResistance(25f);
		this.setLightLevel(3f);
		this.setHardness(40f);
		this.setHarvestLevel("pickaxe", 3);
	}

	public SpiritVeinOre setDroppedItem(Item drop) {
		this.droppedItem = drop;
		return this;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return net.minecraft.init.Items.AIR;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(!worldIn.isRemote) {
			if(!player.isCreative()) {
				ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
				if (heldItem.getItem() instanceof ItemTool) {
					if (!EnchantmentHelper.getEnchantments(heldItem).containsKey(Enchantments.SILK_TOUCH)) {
						if (heldItem.getItem().getHarvestLevel(heldItem, Objects.requireNonNull(getHarvestTool(state)), player, state) > 2) {
							if (this.droppedItem != null) {
								if (this.droppedItem instanceof ItemSpiritStone) {
									int mainDrop = worldIn.rand.nextInt(64);
									int sideDrop = worldIn.rand.nextInt(16);
									int evenLessDrop = worldIn.rand.nextInt(4);
									Item prev = Items.ITEMS.get(Items.ITEMS.indexOf(this.droppedItem) - 1);
									Item nextPrev = Items.ITEMS.get(Items.ITEMS.indexOf(prev) - 1);
									ItemStack superDrop = new ItemStack(this.droppedItem, evenLessDrop);
									EntityItem superDropItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, superDrop);
									superDropItem.setOwner(player.getName());
									superDropItem.setNoPickupDelay();
									worldIn.spawnEntity(superDropItem);
									if (prev instanceof ItemSpiritStone) {
										ItemStack mediumDrop = new ItemStack(prev, sideDrop);
										EntityItem mediumDropItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, mediumDrop);
										mediumDropItem.setOwner(player.getName());
										mediumDropItem.setNoPickupDelay();
										worldIn.spawnEntity(mediumDropItem);
									}
									if (nextPrev instanceof ItemSpiritStone) {
										ItemStack lowDrop = new ItemStack(nextPrev, mainDrop);
										EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, lowDrop);
										item.setOwner(player.getName());
										item.setNoPickupDelay();
										worldIn.spawnEntity(item);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerCustomModelLocation(ItemBlock.getItemFromBlock(this), 0, "inventory", "wuxiacraft:spirit_vein_ore");
	}
}
