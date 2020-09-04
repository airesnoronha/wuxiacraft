package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.world.Sect;
import com.airesnor.wuxiacraft.world.data.WorldSectData;
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
import java.util.UUID;

public class SectAdminCommand extends CommandBase {

    @Override
    @MethodsReturnNonnullByDefault
    public String getName() {
        return "sectadmin";
    }

    @Override
    @MethodsReturnNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return "/sectadmin";
    }

    @Override
    @MethodsReturnNonnullByDefault
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("sa");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        boolean wrongUsage = false;
        WorldSectData sectData = WorldSectData.get(server.getEntityWorld());
        if (args.length == 1) {

        } else if (args.length == 2) {

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("rename")) {
                renameSubCommand(sender, args, sectData);
            } else if (args[0].equalsIgnoreCase("setleader")) {
                setLeaderSubCommand(server, sender, args, sectData);
            } else if (args[0].equalsIgnoreCase("settag")) {
                setTagSubCommand(sender, args, sectData);
            } else if (args[0].equalsIgnoreCase("settagcolour")) {
                setTagColourSubCommand(sender, args, sectData);
            } else {
                wrongUsage = true;
            }
        }
        if (wrongUsage) {
            TextComponentString text = new TextComponentString("Invalid arguments, use " + this.getUsage(sender));
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        WorldSectData sectData = WorldSectData.get(server.getEntityWorld());
        if (args.length == 1) {
            if ("rename".startsWith(args[0].toLowerCase())) {
                completions.add("rename");
            }
            if ("setleader".startsWith(args[0].toLowerCase())) {
                completions.add("setLeader");
            }
            if ("settag".startsWith(args[0].toLowerCase())) {
                completions.add("setTag");
            }
            if ("settagcolour".startsWith(args[0].toLowerCase())) {
                completions.add("setTagColour");
            }
        } else if (args.length == 2) {
            for (Sect sect : sectData.SECTS) {
                if (sect.getSectName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(sect.getSectName());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setleader")) {
                for (String name : server.getPlayerList().getOnlinePlayerNames()) {
                    if (name.toLowerCase().startsWith(args[2].toLowerCase())) {
                        completions.add(name);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("settagcolour")) {
                String[] colours = { "aqua", "black", "dark_aqua", "blue", "dark_blue", "dark_gray", "dark_green",
                        "dark_purple", "dark_red", "gold", "gray", "green", "light_purple", "red", "white", "yellow" };
                for (String colour : colours) {
                    if (colour.toLowerCase().startsWith(args[2].toLowerCase())) {
                        completions.add(colour);
                    }
                }
            }
        }
        return completions;
    }

    public void renameSubCommand(ICommandSender sender, String[] args, WorldSectData sectData) {
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sect != null) {
            Sect testSect = Sect.getSectByName(args[2], sectData);
            if (testSect != null) {
                TextComponentString text = new TextComponentString("A Sect with that name already exists, please try another name.");
                text.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(text);
            } else {
                sect.setSectName(args[2]);
                TextComponentString text = new TextComponentString("The sect has been succesfully renamed to \"" + args[2] + "\"");
                text.getStyle().setColor(TextFormatting.GREEN);
                sender.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect.");
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
        }
    }

    public void setLeaderSubCommand(MinecraftServer server, ICommandSender sender, String[] args, WorldSectData sectData) {
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sect != null) {
            EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[2]);
            if (targetPlayer != null) {
                Sect sectOfTarget = Sect.getSectByPlayer(targetPlayer, sectData);
                if (sectOfTarget != null) {
                    if (sect.getSectName().equalsIgnoreCase(sectOfTarget.getSectName())) {
                        UUID oldSectLeader = sect.getSectLeader();
                        sect.addMember(oldSectLeader, sect.getDefaultRanks().get(sect.getDefaultRanks().size() - 1).getKey());
                        sect.setSectLeader(targetPlayer.getUniqueID());
                        TextComponentString text = new TextComponentString("The Sect's sect leader has been changed to " + targetPlayer.getName() + ".");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        sender.sendMessage(text);
                    } else {
                        TextComponentString text = new TextComponentString( targetPlayer.getName() + " does not belong to that sect.");
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                    }
                } else {
                    sect.setSectLeader(targetPlayer.getUniqueID());
                    TextComponentString text = new TextComponentString("The Sect's sect leader has been changed to " + targetPlayer.getName() + ".");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    sender.sendMessage(text);
                }
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect.");
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
        }
    }

    public void setTagSubCommand(ICommandSender sender, String[] args, WorldSectData sectData) {
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sect != null) {
            if (args[2].length() <= 5) {
                boolean tagExists = false;
                for (Sect testSect : sectData.SECTS) {
                    if (testSect.getSectTag().equalsIgnoreCase(args[1])) {
                        tagExists = true;
                        break;
                    }
                }
                if (!tagExists) {
                    sect.setSectTag(args[2]);
                    TextComponentString text = new TextComponentString( "Sect Tag has been set to \"" + args[2] + "\"");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    sender.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString( "That Sect Tag already exists. Please try again.");
                    text.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString( "The Sect Tag cannot be longer than 5 characters. Please try again.");
                text.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect.");
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
        }
    }

    public void setTagColourSubCommand(ICommandSender sender, String[] args, WorldSectData sectData) {
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sect != null) {
            sect.setColour(args[2]);
            TextComponentString text = new TextComponentString( "Sect Tag Colour has been successfully changed.");
            text.getStyle().setColor(TextFormatting.GREEN);
            sender.sendMessage(text);
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect.");
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
        }
    }
}
