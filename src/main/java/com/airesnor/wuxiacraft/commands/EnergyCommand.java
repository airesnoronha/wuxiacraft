package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class EnergyCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "energy";
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/energy set <amount>";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 2) {
            if(sender instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                ICultivation cultivation = CultivationUtils.getCultivationFromEntity(playerMP);
                if(args[0].equalsIgnoreCase("set")) {
                    if(args[1].equalsIgnoreCase("max")) {
                        cultivation.setEnergy(cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()));
                    } else {
                        float amount = Float.parseFloat(args[1]);
                        cultivation.setEnergy(amount);
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("set".startsWith(args[0]))
                completions.add("set");
        } else if(args.length == 2) {
            if("max".startsWith(args[1]))
                completions.add("max");
        }
        return completions;
    }
}
