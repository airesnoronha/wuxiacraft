package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class FoodCommand extends CommandBase {

    private static Field foodStats;

    static {
        foodStats = ReflectionHelper.findField(FoodStats.class, "foodSaturationLevel", "field_75125_b");
        foodStats.setAccessible(true);
    }

    @Override
    @Nonnull
    public String getName() {
        return "food";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/food [set <player> <value> : get <player> : set <player> saturation/hunger <value>]";
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
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        if (args[0].equalsIgnoreCase("get")) {
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + "'s Food:");
                            targetPlayer.sendMessage(text);
                            text = new TextComponentString("Hunger level: " + targetPlayer.getFoodStats().getFoodLevel());
                            targetPlayer.sendMessage(text);
                            text = new TextComponentString("Saturation level: " + targetPlayer.getFoodStats().getSaturationLevel());
                            targetPlayer.sendMessage(text);
                            wrongUsage = false;
                        }
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                } else if (args.length == 3) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if (targetPlayer != null) {
                        if (args[0].equalsIgnoreCase("set")) {
                            int value = Integer.parseInt(args[2]);
                            targetPlayer.getFoodStats().setFoodLevel(value);
                            try {
                                foodStats.setFloat(targetPlayer.getFoodStats(), value);
                            } catch (Exception e) {
                                WuxiaCraft.logger.error("Couldn't help with food, sorry!");
                                e.printStackTrace();
                            }
                            wrongUsage = false;
                        } else if (args[0].equalsIgnoreCase("setSaturation")) {
                            int value = Integer.parseInt(args[2]);
                            try {
                                foodStats.setFloat(targetPlayer.getFoodStats(), value);
                            } catch (Exception e) {
                                WuxiaCraft.logger.error("Couldn't help with food, sorry!");
                                e.printStackTrace();
                            }
                            wrongUsage = false;
                        } else if (args[0].equalsIgnoreCase("setHunger")) {
                            int value = Integer.parseInt(args[2]);
                            targetPlayer.getFoodStats().setFoodLevel(value);
                            wrongUsage = false;
                        }
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                }

                if (wrongUsage) {
                    TextComponentString text = new TextComponentString("Invalid arguments, use /food [set <player> <value> : get <player> : set <player> saturation/hunger <value>]");
                    text.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(text);
                }
            }
        }
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("set".startsWith(args[0].toLowerCase())) {
                completions.add("set");
            }
            if ("setsaturation".startsWith(args[0].toLowerCase())) {
                completions.add("setSaturation");
            }
            if ("sethunger".startsWith(args[0].toLowerCase())) {
                completions.add("setHunger");
            }
            if ("get".startsWith(args[0].toLowerCase())) {
                completions.add("get");
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
