package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.alchemy.Recipe;
import com.airesnor.wuxiacraft.alchemy.Recipes;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemRecipe extends Item implements IHasModel {

	public ItemRecipe(String name) {
		setRegistryName(name);
		setUnlocalizedName(name);
		WuxiaItems.ITEMS.add(this);
		setMaxStackSize(1);
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerCustomModelLocation(this, 0, "inventory", "wuxiacraft:recipe_scroll");
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String displayName = super.getItemStackDisplayName(stack);
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null) {
			if(tag.hasKey("recipe-name")) {
				displayName = tag.getString("recipe-name");
			}
		}
		return displayName;
	}

	public static void setRecipe(ItemStack stack, Recipe recipe) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) tag = new NBTTagCompound();
		int items = recipe.getRecipeItemCount();
		tag.setString("recipe-name", recipe.getName());
		tag.setInteger("recipe-count", items);
		List<Pair<Float, Item>> recipeItems = recipe.getRecipe();
		for(int i = 0; i < items; i++) {
			NBTTagCompound itemTag = new NBTTagCompound();
			ItemStack is = new ItemStack(recipeItems.get(i).getRight(), 1);
			is.writeToNBT(itemTag);
			tag.setTag("item-"+i, itemTag);
			tag.setString("temp-"+i, RecipeTemperature.getFromTemperature(recipeItems.get(i).getLeft()).toString());
		}
		stack.setTagCompound(tag);
	}

	public static void setRecipeAtRandom(ItemStack stack) {
		Random rand = new Random();
		int result = rand.nextInt(Recipes.RECIPES.size());
		Recipe recipe = Recipes.RECIPES.get(result);
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) tag = new NBTTagCompound();
		int items = recipe.getRecipeItemCount();
		tag.setString("recipe-name", recipe.getName());
		tag.setInteger("recipe-count", items);
		List<Pair<Float, Item>> recipeItems = recipe.getRecipe();
		for(int i = 0; i < items; i++) {
			result = rand.nextInt(100);
			if(result < 95) {
				NBTTagCompound itemTag = new NBTTagCompound();
				ItemStack is = new ItemStack(recipeItems.get(i).getRight(), 1);
				is.writeToNBT(itemTag);
				tag.setTag("item-" + i, itemTag);
			}
			result = rand.nextInt(100);
			if(result < 75) {
				tag.setString("temp-" + i, RecipeTemperature.getFromTemperature(recipeItems.get(i).getLeft()).toString());
			}
		}
		stack.setTagCompound(tag);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		BlockPos pos = playerIn.getPosition();
		if(worldIn.isRemote) {
			playerIn.openGui(WuxiaCraft.instance, GuiHandler.RECIPE_GUI_ID, Minecraft.getMinecraft().player.world, pos.getX(), pos.getY(), pos.getZ());
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public enum RecipeTemperature {
		COLD("cold"), WARM("warm"), HOT("hot"), MELTING("melting"), SUPER_HOT("super_hot");
		private final String name;

		RecipeTemperature(String name) {
			this.name = name;
		}

		public String getName() {
			return I18n.format("wuxiacraft.temperatures."+ this.name);
		}

		public static RecipeTemperature getFromTemperature(float temperature) {
			RecipeTemperature temp = COLD;
			if(temperature > 300f) temp = WARM;
			if(temperature > 600f) temp = HOT;
			if(temperature > 900f) temp = MELTING;
			if(temperature > 1200f) temp = SUPER_HOT;
			return temp;
		}
	}
}
