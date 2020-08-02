package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CultInfoCommand extends CommandBase {

	public CultInfoCommand() {
		super();
	}

	private static final List<String> progressFill = new ArrayList<>();
	private static final List<String> energyFill = new ArrayList<>();

	static {
		progressFill.add("initial");
		progressFill.add("early");
		progressFill.add("middle");
		progressFill.add("advanced");
		progressFill.add("final");
		energyFill.add("empty");
		energyFill.add("low");
		energyFill.add("medium");
		energyFill.add("high");
		energyFill.add("full");
	}

	@Override
	@Nonnull
	public String getName() {
		return "cultinfo";
	}

	@Override
	@Nonnull
	public String getUsage(ICommandSender sender) {
		return "/cultinfo";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("cultinfo");
		return aliases;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			if(sender instanceof EntityPlayerMP) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity((EntityLivingBase) sender);
				String message = String.format("You are at %s", cultivation.getEssenceLevel().getLevelName(cultivation.getEssenceSubLevel()));
				TextComponentString text = new TextComponentString(message);
				sender.sendMessage(text);

				double progress = cultivation.getEssenceProgress() / cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel());
				int fillIndexProgress = (int)(Math.round(progress*4));
				if ((int)(progress*100f) == 0) {
					message = String.format("Progress: %s (%d%%)", progressFill.get(0), (int)(progress*100f));
				} else {
					message = String.format("Progress: %s (%d%%)", progressFill.get(fillIndexProgress), (int)(progress*100f));
				}
				text = new TextComponentString(message);
				sender.sendMessage(text);

				double energy = cultivation.getEnergy() / cultivation.getMaxEnergy();
				int fillIndexEnergy = (int)(Math.round(energy*4));
				if ((int)(energy*100f) == 0) {
					message = String.format("Energy: %s (%d%%)", energyFill.get(0), (int)(energy*100f));
				} else {
					message = String.format("Energy: %s (%d%%)", energyFill.get(fillIndexEnergy), (int)(energy*100f));
				}
				text = new TextComponentString(message);
				sender.sendMessage(text);
			}
		} else throw new CommandException("Not used correctly!");
	}
}
