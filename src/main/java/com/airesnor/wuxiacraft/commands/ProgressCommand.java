package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class ProgressCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "progress";
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/progress [set:add] <player> <system> <amount>";
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
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            if (!playerMP.world.isRemote) {
                boolean wrongUsage = true;
                if (args.length == 4) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                        try {
                            double amount = Double.parseDouble(args[3]);
                            TextComponentString text;
                            switch(args[2]) {
                                case "body":
                                    if (args[0].equalsIgnoreCase("set")) {
                                        cultivation.setBodyProgress(amount);
                                        wrongUsage = false;
                                    } else if (args[0].equalsIgnoreCase("add")) {
                                        cultivation.addBodyProgress(amount, false);
                                        wrongUsage = false;
                                    }
                                    text = new TextComponentString("Your cultivation base has been modified ...");
                                    text.getStyle().setColor(TextFormatting.GRAY);
                                    targetPlayer.sendMessage(text);
                                    NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
                                    break;
                                case "divine":
                                    if (args[0].equalsIgnoreCase("set")) {
                                        cultivation.setDivineProgress(amount);
                                        wrongUsage = false;
                                    } else if (args[0].equalsIgnoreCase("add")) {
                                        cultivation.addDivineProgress(amount, false);
                                        wrongUsage = false;
                                    }
                                    text = new TextComponentString("Your cultivation base has been modified ...");
                                    text.getStyle().setColor(TextFormatting.GRAY);
                                    targetPlayer.sendMessage(text);
                                    NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
                                    break;
                                case "essence":
                                    if (args[0].equalsIgnoreCase("set")) {
                                        cultivation.setEssenceProgress(amount);
                                        wrongUsage = false;
                                    } else if (args[0].equalsIgnoreCase("add")) {
                                        cultivation.addEssenceProgress(amount, false);
                                        wrongUsage = false;
                                    }
                                    text = new TextComponentString("Your cultivation base has been modified ...");
                                    text.getStyle().setColor(TextFormatting.GRAY);
                                    targetPlayer.sendMessage(text);
                                    NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
                                    break;
                                case "three":
                                    if (args[0].equalsIgnoreCase("set")) {
                                        cultivation.setBodyProgress(amount);
                                        cultivation.setDivineProgress(amount);
                                        cultivation.setEssenceProgress(amount);
                                        wrongUsage = false;
                                    } else if (args[0].equalsIgnoreCase("add")) {
                                        cultivation.addBodyProgress(amount, false);
                                        cultivation.addDivineProgress(amount, false);
                                        cultivation.addEssenceProgress(amount, false);
                                        wrongUsage = false;
                                    }
                                    text = new TextComponentString("Your cultivation base has been modified ...");
                                    text.getStyle().setColor(TextFormatting.GRAY);
                                    targetPlayer.sendMessage(text);
                                    NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            TextComponentString text = new TextComponentString("Couldn't read number: " + args[3]);
                            text.getStyle().setColor(TextFormatting.RED);
                            sender.sendMessage(text);
                        } catch (Cultivation.RequiresTribulation trib) {
                            double strength = cultivation.getSystemLevel(trib.system).getModifierBySubLevel(trib.subLevel);
                            CultivationUtils.callTribulation(targetPlayer, strength, trib.system, trib.level, trib.subLevel);
                        }
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                }
                if (wrongUsage) {
                    TextComponentString text = new TextComponentString("Invalid arguments, use " + this.getUsage(sender));
                    text.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(text);
                }
            }
        } else throw new CommandException("Not used correctly!");
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("add".toLowerCase().startsWith(args[0].toLowerCase())) completions.add("add");
            if ("set".toLowerCase().startsWith(args[0].toLowerCase())) completions.add("set");
        } else if (args.length == 2) {
            for(String name : server.getOnlinePlayerNames()) {
                if(name.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(name);
                }
            }
        } else if(args.length == 3) {
            if ("body".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("body");
            else if ("divine".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("divine");
            else if ("essence".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("essence");
            else if ("three".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("three");
        }
        return completions;
    }
}
