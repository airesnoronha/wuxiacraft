package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.items.ItemFan;
import com.airesnor.wuxiacraft.items.WuxiaItems;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.ShrinkEntityItemMessage;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Cauldron extends BlockContainer {

	private static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.81, 1);
	private int rightClickCounter;

	public static final IProperty<Integer> CAULDRON = PropertyInteger.create("cauldron", 0, 2);

	public Cauldron(String name) {
		super(Materials.CAULDRON);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.setCreativeTab(WuxiaBlocks.BLOCKS_TAB);

		setHardness(1f);
		setResistance(25f);
		rightClickCounter = 0;

		WuxiaBlocks.BLOCKS.add(this);
		WuxiaItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
	}

	@SuppressWarnings("deprecation")
	@Override
	@ParametersAreNonnullByDefault
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@Override
	@Nonnull
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemBlock.getItemFromBlock(this);
	}

	@Override
	public boolean isToolEffective(String type, IBlockState state) {
		if (type.equals("pickaxe")) return true;
		else return super.isToolEffective(type, state);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CauldronTileEntity();
	}

	@Nullable
	private CauldronTileEntity getTE(World world, BlockPos pos) {
		return (CauldronTileEntity) world.getTileEntity(pos);
	}

	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CAULDRON);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(CAULDRON, 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean used = false;
		if (!worldIn.isRemote) {
			CauldronTileEntity te = getTE(worldIn, pos);
			if (te != null) {
				if (!playerIn.getHeldItem(hand).isEmpty()) {
					ItemStack itemStack = playerIn.getHeldItem(hand);
					if (!te.hasFirewood()) {
						if (itemStack.getItem() == net.minecraft.init.Items.STICK) {
							used = true;
							te.addWood(2000);
							if (!playerIn.isCreative())
								itemStack.shrink(1);
							playerIn.openContainer.detectAndSendChanges();
						}
						if (itemStack.getItem() == net.minecraft.init.Items.COAL) {
							used = true;
							te.addWood(16000);
							if (!playerIn.isCreative())
								itemStack.shrink(1);
							playerIn.openContainer.detectAndSendChanges();
						}

						if (itemStack.getItem() == ItemBlock.getItemFromBlock(net.minecraft.init.Blocks.COAL_BLOCK)) {
							used = true;
							te.addWood(64000);
							if (!playerIn.isCreative())
								itemStack.shrink(1);
							playerIn.openContainer.detectAndSendChanges();
						}
					}
					if (itemStack.getItem() == net.minecraft.init.Items.FLINT_AND_STEEL) {
						if (te.hasFirewood() && !te.isLit()) {
							used = true;
							itemStack.damageItem(1, playerIn);
							te.setOnFire();
							playerIn.openContainer.detectAndSendChanges();
						}
					}
					if (itemStack.getItem() instanceof ItemFan) {
						if (te.isLit()) {
							ItemFan item = (ItemFan) itemStack.getItem();
							te.wiggleFan(item.getFanStrength(), item.getMaxFanStrength());
							used = true;
						}
					}
				}
				/*if (playerIn.getHeldItem(hand).isEmpty() && !playerIn.isSneaking()) {
					TextComponentString text = new TextComponentString("Burning Time: " + te.getBurningTime());
					playerIn.sendMessage(text);
				}*/
				if (playerIn.getHeldItem(hand).isEmpty() && playerIn.isSneaking()) {
					TextComponentString text = new TextComponentString("The Cauldron has been emptied.");
					text.getStyle().setColor(TextFormatting.GREEN);
					if (te.hasWater()) {
						if (te.getCauldronState() == CauldronTileEntity.EnumCauldronState.WRONG_RECIPE) {
							te.emptyCauldron();
							if (!playerIn.world.isRemote) {
								playerIn.sendMessage(text);
							}
							used = true;
						} else if (te.getCauldronState() != CauldronTileEntity.EnumCauldronState.WRONG_RECIPE && rightClickCounter >= 2) {
							te.emptyCauldron();
							if (!playerIn.world.isRemote) {
								playerIn.sendMessage(text);
							}
							used = true;
							rightClickCounter = 0;
						} else if (te.getCauldronState() != CauldronTileEntity.EnumCauldronState.WRONG_RECIPE) {
							rightClickCounter++;
						}
					}
				}
			}
		}
		return used;
	}

	@Override
	@Nonnull
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		CauldronTileEntity te = getTE(world, pos);
		if (te != null) {
			te.prepareToDie();
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			CauldronTileEntity te = getTE(worldIn, pos);
			if (te != null) {
				if (entityIn instanceof EntityItem) {
					ItemStack stack = ((EntityItem) entityIn).getItem().copy();
					te.addRecipeInput(stack.getItem());
					stack.shrink(1);
					if (stack.isEmpty()) entityIn.setDead();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@ParametersAreNonnullByDefault
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX);
	}
}
