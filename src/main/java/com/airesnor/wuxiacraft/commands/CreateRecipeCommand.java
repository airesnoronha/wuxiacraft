package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.alchemy.Recipe;
import com.airesnor.wuxiacraft.alchemy.Recipes;
import com.airesnor.wuxiacraft.items.ItemRecipe;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeCommand extends CommandBase {

	@Override
	public String getName() {
		return "createRecipe";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/createRecipe <recipe_name>";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("crecipe");
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!sender.getEntityWorld().isRemote) {
			boolean wrongUsage = false;
			if(args.length == 1) {
				String recipe_name = args[0];
				for(Recipe recipe : Recipes.RECIPES) {
					if(recipe_name.equals(recipe.getUnlocalizedName())) {
						ItemStack stack = new ItemStack(Items.RECIPE_SCROLL, 1);
						ItemRecipe.setRecipe(stack, recipe);
						Vec3d pos = sender.getPositionVector();
						EntityItem item = new EntityItem(sender.getEntityWorld(), pos.x, pos.y, pos.z, stack);
						sender.getEntityWorld().spawnEntity(item);
					} else {
						TextComponentString text = new TextComponentString("Couldn't find recipe");
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				}
			} else {
				wrongUsage = true;
			}
			if (wrongUsage) {
				TextComponentString text = new TextComponentString("Invalid arguments, use /createRecipe <recipe_name>");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		}
	}
}
