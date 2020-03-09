package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTESR;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.ItemFan;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class Cauldron extends BlockContainer implements IHasModel {

    public static final IProperty<Integer> CAULDRON = PropertyInteger.create("cauldron", 0, 2);

    public Cauldron (String name) {
        super(Materials.CAULDRON);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.setCreativeTab(Blocks.BLOCKS_TAB);

        setHardness(1f);
        setResistance(25f);

        Blocks.BLOCKS.add(this);
        Items.ITEMS.add(new ItemBlock(this).setRegistryName(name));
    }

    @Override
    public void registerModels() {
        WuxiaCraft.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
        ClientRegistry.bindTileEntitySpecialRenderer(CauldronTileEntity.class, new CauldronTESR());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemBlock.getItemFromBlock(this);
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        if(type.equals("pickaxe")) return true;
        else return super.isToolEffective(type, state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CauldronTileEntity();
    }

    private CauldronTileEntity getTE(World world, BlockPos pos) {
        return (CauldronTileEntity) world.getTileEntity(pos);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CAULDRON);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(CAULDRON, 0);
    }

    @Override
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
        CauldronTileEntity te = getTE(worldIn, pos);
        if(!playerIn.getHeldItem(hand).isEmpty()) {
            ItemStack itemStack = playerIn.getHeldItem(hand);
            if(itemStack.getItem() == net.minecraft.init.Items.STICK) {
                if(!te.isHasFirewood()) {
                    used = true;
                    te.setHasFirewood(true);
                    if(!playerIn.isCreative())
                        itemStack.shrink(1);
                    playerIn.openContainer.detectAndSendChanges();
                }
            }
            if(itemStack.getItem() == net.minecraft.init.Items.FLINT_AND_STEEL) {
                if(te.isHasFirewood() && !te.isLit()) {
                    used = true;
                    itemStack.damageItem(1, playerIn);
                    te.setOnFire();
                    playerIn.openContainer.detectAndSendChanges();
                }
            }
            if(itemStack.getItem() instanceof ItemFan) {
                if(te.isLit()) {
                    ItemFan item = (ItemFan) itemStack.getItem();
                    te.wiggleFan(item.getFanStrength(), item.getMaxFanStrength());
                    used = true;
                }
            }
        }
        return used;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        getTE(world,pos).prepareToDie();
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        WuxiaCraft.logger.info("Collided with " + entityIn.getName());
        if(entityIn instanceof EntityItem) {
            ItemStack stack = ((EntityItem)entityIn).getItem().copy();
            entityIn.setDead();
		}
	}

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return super.getCollisionBoundingBox(blockState, worldIn, pos).shrink(0.1);
    }
}
