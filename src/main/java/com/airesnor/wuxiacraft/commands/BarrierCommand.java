package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Barrier;
import com.airesnor.wuxiacraft.cultivation.IBarrier;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.BarrierMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BarrierCommand extends CommandBase {

    @Override
    public String getName() {
        return "barrier";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/barrier [set <player> <value> : setActive <player> <true : false> : get <player> : resetCD <player> : reset <player>] ";
    }

    @Override
    public List<String> getAliases() {
		return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!sender.getEntityWorld().isRemote) {
            boolean wrongUsage = true;
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("get")) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                    if (targetPlayer != null) {
                        IBarrier barrier = CultivationUtils.getBarrierFromEntity(targetPlayer);
                        TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s Barrier:");
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Amount: " + barrier.getBarrierAmount());
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Max Amount: " + barrier.getBarrierMaxAmount(cultivation));
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Regen Rate: " + barrier.getBarrierRegenRate(cultivation));
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Active: " + barrier.isBarrierActive());
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Regen Active: " + barrier.isBarrierRegenActive());
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Cooldown: " + barrier.getBarrierCooldown());
                        sender.sendMessage(text);
                        text = new TextComponentString("Barrier Broken: " + barrier.isBarrierBroken());
                        sender.sendMessage(text);
                        wrongUsage = false;
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                } else if (args[0].equalsIgnoreCase("resetCD")) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        IBarrier barrier = CultivationUtils.getBarrierFromEntity(targetPlayer);
                        barrier.setBarrierCooldown(0);
                        NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), targetPlayer);
                        TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s barrier cooldown has been reset!");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        sender.sendMessage(text);
                        wrongUsage = false;
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        IBarrier barrier = CultivationUtils.getBarrierFromEntity(targetPlayer);
                        IBarrier newBarier = new Barrier();
                        barrier.copyFrom(newBarier);
                        NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), targetPlayer);
                        TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s barrier has been reset!");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        sender.sendMessage(text);
                        wrongUsage = false;
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        IBarrier barrier = CultivationUtils.getBarrierFromEntity(targetPlayer);
                        float value = Float.parseFloat(args[2]);
                        if (value >= 0) {
                            barrier.setBarrierAmount(value);
                            NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), targetPlayer);
                            TextComponentString text = new TextComponentString("Setting Barrier Amount successful");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            sender.sendMessage(text);
                            wrongUsage = false;
                        }
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                } else if (args[0].equalsIgnoreCase("setActive")) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        IBarrier barrier = CultivationUtils.getBarrierFromEntity(targetPlayer);
                        boolean active = Boolean.parseBoolean(args[2]);
                        barrier.setBarrierActive(active);
                        NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), targetPlayer);
                        wrongUsage = false;
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                }
            }

            if (wrongUsage) {
                TextComponentString text = new TextComponentString("Invalid arguments, use /barrier [set <player> <value> : setActive <player> <true : false> : get <player> : resetcd <player> : reset <player>] ");
                text.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(text);
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("get".startsWith(args[0].toLowerCase())) {
                completions.add("get");
            }
            if ("set".startsWith(args[0].toLowerCase())) {
                completions.add("set");
            }
            if ("setactive".startsWith(args[0].toLowerCase())) {
                completions.add("setActive");
            }
            if ("resetcd".startsWith(args[0].toLowerCase())) {
                completions.add("resetCD");
            }
            if ("reset".startsWith(args[0].toLowerCase())) {
                completions.add("reset");
            }
            if ("setregenrate".startsWith(args[0].toLowerCase())) {
                completions.add("setRegenRate");
            }
            if ("fix".startsWith(args[0].toLowerCase())) {
                completions.add("fix");
            }
            if ("fixall".startsWith(args[0].toLowerCase())) {
                completions.add("fixAll");
            }
        } else if (args.length == 2) {
            for (String name : server.getPlayerList().getOnlinePlayerNames()) {
                if (name.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(name);
                }
            }
        }
        return completions;
    }
}
