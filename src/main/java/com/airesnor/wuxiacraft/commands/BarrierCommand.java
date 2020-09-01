package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Barrier;
import com.airesnor.wuxiacraft.cultivation.IBarrier;
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
import java.util.ArrayList;
import java.util.List;

public class BarrierCommand extends CommandBase {

    @Override
    @MethodsReturnNonnullByDefault
    public String getName() {
        return "barrier";
    }

    @Override
    @MethodsReturnNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return "/barrier [set <player> <value> : setActive <player> <true : false> : get <player> : resetCD <player> : reset <player>] ";
    }

    @Override
    @MethodsReturnNonnullByDefault
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            if (!playerMP.world.isRemote) {
                boolean wrongUsage = true;
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("get")) {
                        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                        if (targetPlayer != null) {
                            IBarrier barrier = CultivationUtils.getBarrierFromEntity(targetPlayer);
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s Barrier:");
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Amount: " + barrier.getBarrierAmount());
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Max Amount: " + barrier.getBarrierMaxAmount());
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Regen Rate: " + barrier.getBarrierRegenRate());
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Active: " + barrier.isBarrierActive());
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Regen Active: " + barrier.isBarrierRegenActive());
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Cooldown: " + barrier.getBarrierCooldown());
                            playerMP.sendMessage(text);
                            text = new TextComponentString("Barrier Broken: " + barrier.isBarrierBroken());
                            playerMP.sendMessage(text);
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
                            NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), (EntityPlayerMP) targetPlayer);
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s barrier cooldown has been reset!");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            playerMP.sendMessage(text);
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
                            IBarrier barrier = CultivationUtils.getBarrierFromEntity(playerMP);
                            IBarrier newBarier = new Barrier();
                            barrier.copyFrom(newBarier);
                            NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), (EntityPlayerMP) targetPlayer);
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s barrier has been reset!");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            playerMP.sendMessage(text);
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
                                NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), (EntityPlayerMP) targetPlayer);
                                TextComponentString text = new TextComponentString("Setting Barrier Amount successful");
                                text.getStyle().setColor(TextFormatting.GREEN);
                                playerMP.sendMessage(text);
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
                            IBarrier barrier = CultivationUtils.getBarrierFromEntity(playerMP);
                            boolean active = Boolean.parseBoolean(args[2]);
                            barrier.setBarrierActive(active);
                            NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), (EntityPlayerMP) targetPlayer);
                            wrongUsage = false;
                        } else {
                            TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                            text.getStyle().setColor(TextFormatting.RED);
                            sender.sendMessage(text);
                            wrongUsage = true;
                        }
                    } else if (args[0].equalsIgnoreCase("setRegenRate")) {
                        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                        if (targetPlayer != null) {
                            IBarrier barrier = CultivationUtils.getBarrierFromEntity(playerMP);
                            float value = Float.parseFloat(args[2]);
                            if (value >= 0) {
                                barrier.setBarrierRegenRate(value);
                                NetworkWrapper.INSTANCE.sendTo(new BarrierMessage(barrier, targetPlayer.getUniqueID()), (EntityPlayerMP) targetPlayer);
                                wrongUsage = false;
                            }
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
