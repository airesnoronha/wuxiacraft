package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.world.Sect;
import com.airesnor.wuxiacraft.world.data.WorldSectData;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO - Finish this.
public class SectCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "sect";
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public String getUsage(ICommandSender sender) {
        return "/sect";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            if (!playerMP.world.isRemote) {
                boolean wrongUsage = false;
                WorldSectData sectData = WorldSectData.get(playerMP.world);
                if (args.length == 0) {
                    helpSubCommand(playerMP);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("info")) {
                        infoSubCommand(server, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("list")) {
                        listSubCommand(playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("disband")) {
                        disbandSubCommand(playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        leaveSubCommand(playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("help")) {
                        helpSubCommand(playerMP);
                    } else if (args[0].equalsIgnoreCase("invites")) {
                        invitesSubCommand(server, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("ranks")) {
                        ranksSubCommand(playerMP, sectData);
                    } else {
                        wrongUsage = true;
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("join")) {
                        acceptSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("decline")) {
                        declineSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("kick")) {
                        kickSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("rename")) {
                        renameSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("deleterank")) {
                        deleteRankSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("setleader")) {
                        setLeaderSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("invite")) {
                        inviteSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("cancelinvite")) {
                        cancelInviteSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("ally")) {
                        allySubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("enemy")) {
                        enemySubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("viewplayer")) {
                        viewPlayerSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("viewsect")) {
                        viewSectSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("settag")) {
                        setTagSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("settagcolour")) {
                        setTagColourSubCommand(args, playerMP, sectData);
                    } else {
                        wrongUsage = true;
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("create")) {
                        createSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("setrank")) {
                        setRankSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("createrank")) {
                        createRankSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("setrankpermission")) {
                        setRankPermissionSubCommand(args, playerMP, sectData);
                    }
                }
                if (wrongUsage) {
                    helpSubCommand(playerMP);
                }
                sectData.saveChanges();
            }
        }
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        WorldSectData sectData = WorldSectData.get(sender.getEntityWorld());
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("create".startsWith(args[0].toLowerCase())) {
                completions.add("create");
            }
            if ("info".startsWith(args[0].toLowerCase())) {
                completions.add("info");
            }
            if ("setrank".startsWith(args[0].toLowerCase())) {
                completions.add("setRank");
            }
            if ("createrank".startsWith(args[0].toLowerCase())) {
                completions.add("createRank");
            }
            if ("accept".startsWith(args[0].toLowerCase())) {
                completions.add("accept");
            }
            if ("decline".startsWith(args[0].toLowerCase())) {
                completions.add("decline");
            }
            if ("disband".startsWith(args[0].toLowerCase())) {
                completions.add("disband");
            }
            if ("kick".startsWith(args[0].toLowerCase())) {
                completions.add("kick");
            }
            if ("leave".startsWith(args[0].toLowerCase())) {
                completions.add("leave");
            }
            if ("rename".startsWith(args[0].toLowerCase())) {
                completions.add("rename");
            }
            if ("deleterank".startsWith(args[0].toLowerCase())) {
                completions.add("deleteRank");
            }
            if ("setleader".startsWith(args[0].toLowerCase())) {
                completions.add("setLeader");
            }
            if ("help".startsWith(args[0].toLowerCase())) {
                completions.add("help");
            }
            if ("invite".startsWith(args[0].toLowerCase())) {
                completions.add("invite");
            }
            if ("invites".startsWith(args[0].toLowerCase())) {
                completions.add("invites");
            }
            if ("cancelinvite".startsWith(args[0].toLowerCase())) {
                completions.add("cancelInvite");
            }
            if ("setrankpermission".startsWith(args[0].toLowerCase())) {
                completions.add("setRankPermission");
            }
            if ("ally".startsWith(args[0].toLowerCase())) {
                completions.add("ally");
            }
            if ("enemy".startsWith(args[0].toLowerCase())) {
                completions.add("enemy");
            }
            if ("ranks".startsWith(args[0].toLowerCase())) {
                completions.add("ranks");
            }
            if ("viewplayer".startsWith(args[0].toLowerCase())) {
                completions.add("viewPlayer");
            }
            if ("viewsect".startsWith(args[0].toLowerCase())) {
                completions.add("viewSect");
            }
            if ("settag".startsWith(args[0].toLowerCase())) {
                completions.add("setTag");
            }
            if ("settagcolour".startsWith(args[0].toLowerCase())) {
                completions.add("setTagColour");
            }
         } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("decline") ||
                    args[0].equalsIgnoreCase("ally") || args[0].equalsIgnoreCase("enemy")) {
                for (Sect sect : sectData.SECTS) {
                    String sectName = sect.getSectName();
                    if (sectName.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(sectName);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("setrank") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("setleader")) {
               if (sender instanceof EntityPlayerMP) {
                   Sect sect = Sect.getSectByPlayer((EntityPlayerMP) sender, sectData);
                   if (sect != null) {
                       for (Pair<UUID, String> member : sect.getMembers()) {
                           EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUUID(member.getLeft());
                           if (playerMP != null) {
                               if (playerMP.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                                   completions.add(playerMP.getName());
                               }
                           }
                       }
                   }
               }
            }
            if (args[0].equalsIgnoreCase("deleterank") || args[0].equalsIgnoreCase("setrankpermission")) {
                if (sender instanceof EntityPlayerMP) {
                    Sect sect = Sect.getSectByPlayer((EntityPlayerMP) sender, sectData);
                    if (sect != null) {
                        for (Pair<String, Integer> rank : sect.getRanks()) {
                            if (rank.getLeft().toLowerCase().startsWith(args[1].toLowerCase())) {
                                completions.add(rank.getLeft());
                            }
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("invite")) {
                for (String name : server.getPlayerList().getOnlinePlayerNames()) {
                    if (name.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(name);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("cancelinvite")) {
                if (sender instanceof EntityPlayerMP) {
                    Sect sect = Sect.getSectByPlayer((EntityPlayerMP) sender, sectData);
                    if (sect != null) {
                        for (UUID invitation : sect.getInvitations()) {
                            EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUUID(invitation);
                            if (playerMP != null) {
                                if (playerMP.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                                    completions.add(playerMP.getName());
                                }
                            }
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("viewplayer")) {
                for (String playerName : server.getPlayerProfileCache().getUsernames()) {
                    if (playerName.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(playerName);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("viewsect")) {
                for (Sect sect : sectData.SECTS) {
                    if (sect.getSectName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(sect.getSectName());
                    }
                }
            }
            if (args[0].equalsIgnoreCase("settagcolour")) {
                String[] colours = { "aqua", "black", "dark_aqua", "blue", "dark_blue", "dark_gray", "dark_green",
                        "dark_purple", "dark_red", "gold", "gray", "green", "light_purple", "red", "white", "yellow" };
                for (String colour : colours) {
                    if (colour.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(colour);
                    }
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setrank")) {
                if (sender instanceof  EntityPlayerMP) {
                    Sect sect = Sect.getSectByPlayer((EntityPlayerMP) sender, sectData);
                    if (sect != null) {
                        for (Pair<String, Integer> rank : sect.getRanks()) {
                            if (rank.getLeft().toLowerCase().startsWith(args[2].toLowerCase())) {
                                completions.add(rank.getLeft());
                            }
                        }
                    }
                }
            }
        }
        return completions;
    }

    public void createSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sectOfPlayer = Sect.getSectByPlayer(playerMP, sectData);
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sectOfPlayer == null) {
            if (sect != null) {
                TextComponentString text = new TextComponentString("A sect with that name already exists, please use another sect name.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            } else {
                if (args[2].length() <= 5) {
                    boolean tagExists = false;
                    for (Sect testSect : sectData.SECTS) {
                        if (testSect.getSectTag().equalsIgnoreCase(args[2])) {
                            tagExists = true;
                            break;
                        }
                    }
                    if (!tagExists) {
                        sectData.SECTS.add(new Sect(args[1], args[2], playerMP.getUniqueID()));
                        TextComponentString text = new TextComponentString("Sect " + args[1] + " has been created.");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        playerMP.sendMessage(text);
                    } else {
                        TextComponentString text = new TextComponentString( "That Sect Tag already exists. Please try again.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    }
                } else {
                    TextComponentString text = new TextComponentString( "The Sect Tag cannot be longer than 5 characters. Please try again.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            }
        } else {
            TextComponentString text = new TextComponentString("You are already in a sect. To create a new sect you either disband or leave your current sect.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void infoSubCommand(MinecraftServer server, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sectInfo = Sect.getSectByPlayer(playerMP, sectData);
        TextComponentString text;
        if (sectInfo != null) {
            text = new TextComponentString("--------------------------------------------------");
            text.getStyle().setColor(TextFormatting.AQUA);
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Name: " + TextFormatting.WHITE + sectInfo.getSectName());
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Tag: " + TextFormatting.WHITE + sectInfo.getSectTag());
            playerMP.sendMessage(text);
            String sectLeaderName = "";
            GameProfile sectLeader = server.getPlayerProfileCache().getProfileByUUID(sectInfo.getSectLeader());
            if (sectLeader != null) {
                sectLeaderName = sectLeader.getName();
            }
            text = new TextComponentString(TextFormatting.GREEN + "Sect Leader: " + TextFormatting.WHITE + sectLeaderName);
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Members: ");
            playerMP.sendMessage(text);
            String[] memberOutputs = new String[sectInfo.getMembers().size() / 2 + 1];
            int indexToStartFromNext = 0;
            for (int i = 0; i < memberOutputs.length; i++) {
                String tempOutput = "";
                for (int j = indexToStartFromNext; j < sectInfo.getMembers().size(); j++) {
                    Pair<UUID, String> member = sectInfo.getMembers().get(j);
                    UUID memberUUID = member.getLeft();
                    String memberRank = member.getRight();
                    GameProfile memberProfile = server.getPlayerProfileCache().getProfileByUUID(memberUUID);
                    String playerName = "";
                    if (memberProfile != null) {
                        playerName = memberProfile.getName();
                    }
                    tempOutput += "[" + memberRank + "]" + playerName + ", ";
                    if (j % 2 == 1 && j != 0) {
                        indexToStartFromNext = j + 1;
                        break;
                    }
                }
                memberOutputs[i] = String.format("%5s", tempOutput);
            }
            for (String memberOutput : memberOutputs) {
                text = new TextComponentString(memberOutput);
                playerMP.sendMessage(text);
            }
            text = new TextComponentString(TextFormatting.GREEN + "Sect Allies: WIP");
            playerMP.sendMessage(text);
            String allyOutput = "";
            for (Pair<String, Boolean> ally : sectInfo.getAllies()) {
                allyOutput += ally.getLeft() + ", ";
            }
            text = new TextComponentString(String.format("%5s", allyOutput));
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Enemies:");
            playerMP.sendMessage(text);
            String enemyOutput = "";
            for (Pair<String, Boolean> enemy: sectInfo.getEnemies()) {
                enemyOutput += enemy.getLeft() + ", ";
            }
            text = new TextComponentString(String.format("%5s", enemyOutput));
            playerMP.sendMessage(text);
            text = new TextComponentString("--------------------------------------------------");
            text.getStyle().setColor(TextFormatting.AQUA);
            playerMP.sendMessage(text);
        } else {
            text = new TextComponentString("You do not belong to a sect.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void setRankPermissionSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                Pair<String, Integer> sectRank = sect.getRank(args[1]);
                if (sectRank != null) {
                    sect.setRankPermissionLevel(sectRank.getLeft(), Integer.parseInt(args[2]));
                    TextComponentString text = new TextComponentString("\"" + args[1] + "\" permission level has been set to " + args[2] + ".");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString("\""+ args[1] + "\" is not a rank of the sect.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Only the sect leader has the permission to change sect rank permissions.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void createRankSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                Pair<String, Integer> rank = sect.getRank(args[1]);
                if (rank == null) {
                    sect.addRank(args[1], Integer.parseInt(args[2]));
                    TextComponentString text = new TextComponentString("Rank \"" + args[1] + "\" has been created with a permission level of " + args[2] + ".");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString("Rank \"" + args[1] + "\" already exists. Please choose another rank name.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Only the sect leader has the permission to create ranks.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void acceptSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sect != null) {
            List<UUID> invitations = sect.getInvitations();
            boolean isInvitedBySect = false;
            for (UUID invitation : invitations) {
                if (invitation.equals(playerMP.getUniqueID())) {
                    isInvitedBySect = true;
                    break;
                }
            }
            if (Sect.getSectByPlayer(playerMP, sectData) == null) {
                if (isInvitedBySect) {
                    sect.addMember(playerMP.getUniqueID(), sect.getDefaultRank());
                } else {
                    TextComponentString text = new TextComponentString("You have not been invited to that sect.");
                    text.getStyle().setColor(TextFormatting.AQUA);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("You are already in a sect and cannot join another one until you leave your current one.");
                text.getStyle().setColor(TextFormatting.AQUA);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect with that name.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void declineSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByName(args[1], sectData);
        if (sect != null) {
            List<UUID> invitations = sect.getInvitations();
            boolean isInvitedBySect = false;
            for (UUID invitation : invitations) {
                if (invitation.equals(playerMP.getUniqueID())) {
                    isInvitedBySect = true;
                    break;
                }
            }
            if (isInvitedBySect) {
                sect.removePlayerFromInvitations(playerMP.getUniqueID());
            } else {
                TextComponentString text = new TextComponentString("You have not been invited to that sect.");
                text.getStyle().setColor(TextFormatting.AQUA);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect with that name.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void disbandSubCommand(EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                if (((System.currentTimeMillis() / 1000) - (sect.getDisbandTime() / 1000)) >= 30) {
                    sect.setDisband(false);
                }
                if (sect.isDisbanding()) {
                    sectData.SECTS.remove(sect);
                } else {
                    TextComponentString text = new TextComponentString("To confirm that you would like to disband the sect. Please type the command \"/sect disband\" one more time. You have a total of 30 seconds to confirm the disbanding of the sect.");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                    sect.setDisband(true);
                    sect.setDisbandTime(System.currentTimeMillis());
                }
            } else {
                TextComponentString text = new TextComponentString("You are not the sect leader of the sect. You can not disband a sect you are not a leader of.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void kickSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
        if (targetPlayer != null) {
            Sect sectOfPlayer = Sect.getSectByPlayer(playerMP, sectData);
            Sect sectOfTarget = Sect.getSectByPlayer(targetPlayer, sectData);
            if (sectOfPlayer != null && sectOfTarget != null) {
                if (sectOfPlayer.getSectName().equalsIgnoreCase(sectOfTarget.getSectName())) {
                    if (!playerMP.getUniqueID().equals(targetPlayer.getUniqueID())) {
                        Pair<String, Integer> sectElderRank = sectOfPlayer.getRank("SectElder");
                        int kickPermissionLevel = sectElderRank.getRight();
                        if (sectOfPlayer.getSectLeader().equals(playerMP.getUniqueID())) {
                            sectOfPlayer.removeMember(targetPlayer.getUniqueID());
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been kicked from the sect.");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            playerMP.sendMessage(text);
                            text = new TextComponentString("You have been kicked from the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            targetPlayer.sendMessage(text);
                        } else {
                            Pair<UUID, String> memberPlayer = sectOfPlayer.getMemberByUUID(playerMP.getUniqueID());
                            Pair<String, Integer> playerRank = null;
                            Pair<UUID, String> memberTarget = sectOfTarget.getMemberByUUID(targetPlayer.getUniqueID());
                            Pair<String, Integer> targetRank = null;
                            if (memberPlayer != null) {
                                playerRank = sectOfPlayer.getRank(memberPlayer.getRight());
                            }
                            if (memberTarget != null) {
                                targetRank = sectOfTarget.getRank(memberTarget.getRight());
                            }
                            if (memberPlayer != null && playerRank != null && memberTarget != null && targetRank != null) {
                                if (playerRank.getRight() >= kickPermissionLevel && playerRank.getRight() > targetRank.getRight()) {
                                    sectOfPlayer.removeMember(memberTarget.getLeft());
                                    TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been kicked from the sect.");
                                    text.getStyle().setColor(TextFormatting.GREEN);
                                    playerMP.sendMessage(text);
                                    text = new TextComponentString("You have been kicked from the sect.");
                                    text.getStyle().setColor(TextFormatting.RED);
                                    targetPlayer.sendMessage(text);
                                } else if (!(playerRank.getRight() >= kickPermissionLevel)) {
                                    TextComponentString text = new TextComponentString("You do not have the permission to kick a member of the sect.");
                                    text.getStyle().setColor(TextFormatting.RED);
                                    playerMP.sendMessage(text);
                                } else if (!(playerRank.getRight() > targetRank.getRight())) {
                                    TextComponentString text = new TextComponentString(targetPlayer.getName() + " cannot be kicked as they either have the same or higher level of authority than you.");
                                    text.getStyle().setColor(TextFormatting.RED);
                                    playerMP.sendMessage(text);
                                }
                            } else if (memberTarget == null && targetRank == null) {
                                TextComponentString text = new TextComponentString("You can not kick the Sect Leader.");
                                text.getStyle().setColor(TextFormatting.RED);
                                playerMP.sendMessage(text);
                            }
                        }
                    } else {
                        TextComponentString text = new TextComponentString("You cannot kick yourself from the sect.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    }
                } else {
                    TextComponentString text = new TextComponentString(targetPlayer.getName() + " does not belong to your sect.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else if (sectOfPlayer == null) {
                TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            } else if (sectOfTarget == null) {
                TextComponentString text = new TextComponentString(targetPlayer.getName() + " does not belong to your sect.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void leaveSubCommand(EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                if (((System.currentTimeMillis() / 1000) - (sect.getDisbandTime() / 1000)) >= 30) {
                    sect.setDisband(false);
                }
                if (sect.isDisbanding()) {
                    sectData.SECTS.remove(sect);
                } else {
                    TextComponentString text = new TextComponentString("Please note that leaving the sect as the sect leader will disband the sect. Please type the command \"/sect leave\" one more time to confirm you would like to leave the sect. You have a total of 30 seconds to confirm the disbanding of the sect.");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                    sect.setDisband(true);
                    sect.setDisbandTime(System.currentTimeMillis());
                }
            } else {
                sect.removeMember(playerMP.getUniqueID());
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void renameSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                Sect testSect = Sect.getSectByName(args[1], sectData);
                if (testSect == null) {
                    sect.setSectName(args[1]);
                    TextComponentString text = new TextComponentString("Sect has been renamed to \"" + args[1] + "\".");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString("A sect with that name has already been created. Please rename your sect to something else.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Only the sect leader can rename the sect.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void deleteRankSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                String rankName = null;
                for (Pair<String, Integer> defaultRank : sect.getDefaultRanks()) {
                    if (!defaultRank.getLeft().equalsIgnoreCase(args[1])) {
                        rankName = args[1];
                    } else {
                        rankName = null;
                    }
                }
                if (rankName != null) {
                    Pair<String, Integer> rank = sect.getRank(rankName);
                    if (rank != null) {
                        int indexOfRank = 0;
                        for (int i = 0; i < sect.getRanks().size(); i++) {
                            if (sect.getRanks().get(i).getLeft().equalsIgnoreCase(rank.getLeft())) {
                                indexOfRank = i;
                                break;
                            }
                        }
                        for (Pair<UUID, String> member : sect.getMembers()) {
                            if (member.getRight().equalsIgnoreCase(rank.getLeft())) {
                                member.setValue(sect.getRanks().get(indexOfRank - 1).getLeft());
                            }
                        }
                        sect.removeRank(rank.getLeft());
                    } else {
                        TextComponentString text = new TextComponentString("That sect rank does not exist.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    }
                } else {
                    TextComponentString text = new TextComponentString("You cannot delete a default sect rank");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Only the sect leader can delete ranks of the sect");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void setLeaderSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                if (((System.currentTimeMillis() / 1000) - (sect.getChangeLeaderTime() / 1000)) >= 30) {
                    sect.setChangeLeader(false);
                }
                GameProfile targetPlayer = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
                if (targetPlayer != null) {
                    if (sect.isChangingLeader()) {
                        boolean leaderChanged = false;
                        UUID oldSectLeader = sect.getSectLeader();
                        for (Pair<UUID, String> member : sect.getMembers()) {
                            if (member.getLeft().equals(targetPlayer.getId())) {
                                sect.setSectLeader(member.getLeft());
                                leaderChanged = true;
                                sect.removeMember(member.getLeft());
                                TextComponentString text = new TextComponentString("The sect leader is now " + targetPlayer.getName() + ".");
                                text.getStyle().setColor(TextFormatting.GREEN);
                                playerMP.sendMessage(text);
                                break;
                            }
                        }
                        if (!leaderChanged) {
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + " is not a member of the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        } else {
                            sect.addMember(oldSectLeader, sect.getDefaultRanks().get(sect.getDefaultRanks().size() - 1).getKey());
                        }
                    } else {
                        TextComponentString text = new TextComponentString("To confirm that you would like to pass the sect leader position to " + targetPlayer.getName() + ". Please type the command \"/sect setleader " + targetPlayer.getName() + "\" one more time. You have a total of 30 seconds to confirm passing your position to the player.");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        playerMP.sendMessage(text);
                        sect.setChangeLeader(true);
                        sect.setChangeLeaderTime(System.currentTimeMillis());
                    }
                } else {
                    TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Only the sect leader can change the sect leader of the sect");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void helpSubCommand(EntityPlayerMP playerMP) {
        TextComponentString text = new TextComponentString("Sect Help Information:\n-----------------------------------");
        text.getStyle().setColor(TextFormatting.AQUA);
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect create <sectName> <sectTag>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect info");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect setRank <player> <rank>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect createRank <rankName> <permissionLevel>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect accept <sectName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect decline <sectName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect disband");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect kick <player>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect leave");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect rename <newSectName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect setTag <sectTag>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect setTagColour <colour>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect deleteRank <rankName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect setLeader <player>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect invite <player>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect invites");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect cancelInvite <player>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect setRankPermission <rank> <permissionLevel>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect ally <sectName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect enemy <sectName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect viewSect <sectName>");
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect viewPlayer <player>");
        playerMP.sendMessage(text);
        text = new TextComponentString("-----------------------------------");
        text.getStyle().setColor(TextFormatting.AQUA);
        playerMP.sendMessage(text);
    }

    public void inviteSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
        if (sect != null) {
            Pair<String, Integer> sectElderRank = sect.getRank("SectElder");
            int invitePermissionLevel = sectElderRank.getRight();
            if (targetPlayer != null) {
                if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                    if (sect.getSectLeader().equals(targetPlayer.getUniqueID())) {
                        TextComponentString text = new TextComponentString("You cannot invite yourself to the sect.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    } else {
                        Pair<UUID, String> memberTarget = sect.getMemberByUUID(targetPlayer.getUniqueID());
                        if (memberTarget != null) {
                            TextComponentString text = new TextComponentString("That player is already a member of the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        } else {
                            sect.addPlayerToInvitation(targetPlayer.getUniqueID());
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been invited to the sect.");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            playerMP.sendMessage(text);
                            text = new TextComponentString("You have been invited to the sect, type \"/sect join " + sect.getSectName() + "\".");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            targetPlayer.sendMessage(text);
                        }
                    }
                } else {
                    Pair<UUID, String> memberPlayer = sect.getMemberByUUID(playerMP.getUniqueID());
                    Pair<String, Integer> playerRank = null;
                    if (memberPlayer != null) {
                        playerRank = sect.getRank(memberPlayer.getRight());
                    }
                    if (playerRank != null) {
                        if (playerRank.getRight() >= invitePermissionLevel) {
                            if (sect.getSectLeader().equals(targetPlayer.getUniqueID())) {
                                TextComponentString text = new TextComponentString("You cannot invite the sect leader to his own sect.");
                                text.getStyle().setColor(TextFormatting.RED);
                                playerMP.sendMessage(text);
                            } else {
                                Pair<UUID, String> memberTarget = sect.getMemberByUUID(targetPlayer.getUniqueID());
                                if (memberTarget != null) {
                                    TextComponentString text = new TextComponentString("That player is already a member of the sect.");
                                    text.getStyle().setColor(TextFormatting.RED);
                                    playerMP.sendMessage(text);
                                } else {
                                    sect.addPlayerToInvitation(targetPlayer.getUniqueID());
                                    TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been invited to the sect.");
                                    text.getStyle().setColor(TextFormatting.GREEN);
                                    playerMP.sendMessage(text);
                                    text = new TextComponentString("You have been invited to the sect, type \"/sect join " + sect.getSectName() + "\".");
                                    text.getStyle().setColor(TextFormatting.GREEN);
                                    targetPlayer.sendMessage(text);
                                }
                            }
                        } else {
                            TextComponentString text = new TextComponentString("You do not have the authority to invite players to the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        }
                    }
                }
            } else {
                TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void invitesSubCommand(MinecraftServer server, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                List<UUID> invitations = sect.getInvitations();
                String output = "";
                for (UUID invitation : invitations) {
                    EntityPlayerMP invitedPlayer = server.getPlayerList().getPlayerByUUID(invitation);
                    if (invitedPlayer != null) {
                        output += invitedPlayer.getName() + ", ";
                    }
                }
                TextComponentString text = new TextComponentString("Number of invitations: " + invitations.size());
                playerMP.sendMessage(text);
                text = new TextComponentString("Invitations: \n" + output);
                playerMP.sendMessage(text);
            } else {
                Pair<UUID, String> memberPlayer = sect.getMemberByUUID(playerMP.getUniqueID());
                Pair<String, Integer> playerRank = sect.getRank(memberPlayer.getRight());
                Pair<String, Integer> sectElderRank = sect.getRank("SectElder");
                int invitePermissionLevel = sectElderRank.getRight();
                if (playerRank.getRight() >= invitePermissionLevel) {
                    List<UUID> invitations = sect.getInvitations();
                    String output = "";
                    for (UUID invitation : invitations) {
                        EntityPlayerMP invitedPlayer = server.getPlayerList().getPlayerByUUID(invitation);
                        if (invitedPlayer != null) {
                            output += invitedPlayer.getName() + ", ";
                        }
                    }
                    TextComponentString text = new TextComponentString("Number of invitations: " + invitations.size());
                    playerMP.sendMessage(text);
                    text = new TextComponentString("Invitations: \n" + output);
                    playerMP.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString("You do not have the permission to view the invites of the sect.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void cancelInviteSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
        if (sect != null) {
            Pair<String, Integer> sectElderRank = sect.getRank("SectElder");
            int invitePermissionLevel = sectElderRank.getRight();
            if (targetPlayer != null) {
                if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                    sect.removePlayerFromInvitations(targetPlayer.getUniqueID());
                } else {
                    Pair<UUID, String> memberPlayer = sect.getMemberByUUID(playerMP.getUniqueID());
                    Pair<String, Integer> playerRank = null;
                    if (memberPlayer != null) {
                        playerRank = sect.getRank(memberPlayer.getRight());
                    }
                    if (playerRank != null) {
                        if (playerRank.getRight() >= invitePermissionLevel) {
                            sect.removePlayerFromInvitations(targetPlayer.getUniqueID());
                        } else {
                            TextComponentString text = new TextComponentString("You do not have the authority to cancel invitations to the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        }
                    }
                }
            } else {
                TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void setRankSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                GameProfile targetPlayer = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
                if (targetPlayer != null) {
                    Sect sectOfTarget = Sect.getSectByPlayerUUID(targetPlayer.getId(), sectData);
                    if (sectOfTarget != null) {
                        if (sect.getSectName().equalsIgnoreCase(sectOfTarget.getSectName())) { // they are part of the same sect
                            if (sect.getSectLeader().equals(targetPlayer.getId())) {
                                TextComponentString text = new TextComponentString("You cannot change the Sect Leader's rank.");
                                text.getStyle().setColor(TextFormatting.RED);
                                playerMP.sendMessage(text);
                            } else {
                                Pair<UUID, String> memberTarget = sect.getMemberByUUID(targetPlayer.getId());
                                Pair<String, Integer> sectRank = sect.getRank(args[2]);
                                if (sectRank != null && memberTarget != null) {
                                    sect.setMemberRank(memberTarget.getLeft(), sectRank.getLeft());
                                    TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been set to \"" + args[2] + "\".");
                                    text.getStyle().setColor(TextFormatting.GREEN);
                                    playerMP.sendMessage(text);
                                } else {
                                    TextComponentString text = new TextComponentString("\""+ args[2] + "\" is not a rank of the sect.");
                                    text.getStyle().setColor(TextFormatting.RED);
                                    playerMP.sendMessage(text);
                                }
                            }
                        } else {
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + " is not a member of the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        }
                    } else {
                        TextComponentString text = new TextComponentString(targetPlayer.getName() + " is not a member of the sect.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    }
                } else {
                    TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Only the sect leader has the permission to change ranks.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void allySubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) { // TODO - this another time, only sectLeader can do this
        // requires confirmation to be allies
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            Sect allySect = Sect.getSectByName(args[1], sectData);
            if (allySect != null) {
                if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                    if (sect.isAlly(allySect.getSectName())) {

                    } else {

                    }
                } else {
                    TextComponentString text = new TextComponentString("Only the sect leader has the permission to create enemies.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Couldn't find sect with that name.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void enemySubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            Sect enemySect = Sect.getSectByName(args[1], sectData);
            if (enemySect != null) {
                if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                    if (sect.isEnemy(enemySect.getSectName())) {
                        sect.removeEnemy(enemySect.getSectName());
                        TextComponentString text = new TextComponentString("You no longer consider " + enemySect.getSectName() + " as an enemy.");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        playerMP.sendMessage(text);
                    } else {
                        TextComponentString text = new TextComponentString("You are now enemies with the sect: " + enemySect.getSectName());
                        text.getStyle().setColor(TextFormatting.GREEN);
                        sect.addEnemy(enemySect.getSectName(), false);
                        playerMP.sendMessage(text);
                        for (Pair<UUID, String> member : sect.getMembers()) {
                            EntityPlayerMP memberPlayer = server.getPlayerList().getPlayerByUUID(member.getLeft());
                            if (memberPlayer != null) {
                                memberPlayer.sendMessage(text);
                            }
                        }
                        text = new TextComponentString("You are now enemies with the sect: " + sect.getSectName());
                        text.getStyle().setColor(TextFormatting.GREEN);
                        enemySect.addEnemy(sect.getSectName(), false);
                        for (Pair<UUID, String> enemyMembers : enemySect.getMembers()) {
                            EntityPlayerMP enemyPlayer = server.getPlayerList().getPlayerByUUID(enemyMembers.getLeft());
                            if (enemyPlayer != null) {
                                enemyPlayer.sendMessage(text);
                            }
                        }
                        EntityPlayerMP enemySectLeader = server.getPlayerList().getPlayerByUUID(enemySect.getSectLeader());
                        if (enemySectLeader != null) {
                            enemySectLeader.sendMessage(text);
                        }
                        if (sect.isAlly(enemySect.getSectName())) {
                            sect.removeAlly(enemySect.getSectName());
                        }
                        if (enemySect.isAlly(sect.getSectName())) {
                            enemySect.removeAlly(sect.getSectName());
                        }
                    }
                } else {
                    TextComponentString text = new TextComponentString("Only the sect leader has the permission to create enemies.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("Couldn't find sect with that name.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void listSubCommand(EntityPlayerMP playerMP, WorldSectData sectData) {
        int i = 0;
        TextComponentString text = new TextComponentString("Total number of sects: " + sectData.SECTS.size());
        playerMP.sendMessage(text);
        for (Sect sect : sectData.SECTS) {
            i++;
            text = new TextComponentString(i + ". " + sect.getSectName());
            playerMP.sendMessage(text);
        }
    }

    public void ranksSubCommand(EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            TextComponentString text = new TextComponentString("Ranks:");
            text.getStyle().setColor(TextFormatting.AQUA);
            playerMP.sendMessage(text);
            for (Pair<String, Integer> rank : sect.getRanks()) {
                text = new TextComponentString(rank.getLeft() + ": " + rank.getRight());
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void viewPlayerSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        GameProfile targetPlayer = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
        if (targetPlayer != null) {
            Sect sectInfo = Sect.getSectByPlayerUUID(targetPlayer.getId(), sectData);
            if (sectInfo != null) {
                TextComponentString text = new TextComponentString("--------------------------------------------------");
                text.getStyle().setColor(TextFormatting.AQUA);
                playerMP.sendMessage(text);
                text = new TextComponentString(TextFormatting.GREEN + "Sect Name: " + TextFormatting.WHITE + sectInfo.getSectName());
                playerMP.sendMessage(text);
                text = new TextComponentString(TextFormatting.GREEN + "Sect Tag: " + TextFormatting.WHITE + sectInfo.getSectTag());
                playerMP.sendMessage(text);
                GameProfile sectLeader = server.getPlayerProfileCache().getProfileByUUID(sectInfo.getSectLeader());
                String sectLeaderName = "";
                if (sectLeader != null) {
                    sectLeaderName = sectLeader.getName();
                }
                text = new TextComponentString(TextFormatting.GREEN + "Sect Leader: " + TextFormatting.WHITE + sectLeaderName);
                playerMP.sendMessage(text);
                text = new TextComponentString(TextFormatting.GREEN + "Sect Members: ");
                playerMP.sendMessage(text);
                String[] memberOutputs = new String[sectInfo.getMembers().size() / 2 + 1];
                int indexToStartFromNext = 0;
                for (int i = 0; i < memberOutputs.length; i++) {
                    String tempOutput = "";
                    for (int j = indexToStartFromNext; j < sectInfo.getMembers().size(); j++) {
                        Pair<UUID, String> member = sectInfo.getMembers().get(j);
                        UUID memberUUID = member.getLeft();
                        String memberRank = member.getRight();
                        GameProfile memberProfile = server.getPlayerProfileCache().getProfileByUUID(memberUUID);
                        String playerName = "";
                        if (memberProfile != null) {
                            playerName = memberProfile.getName();
                        }
                        tempOutput += "[" + memberRank + "]" + playerName + ", ";
                        if (j % 2 == 1 && j != 0) {
                            indexToStartFromNext = j + 1;
                            break;
                        }
                    }
                    memberOutputs[i] = String.format("%5s", tempOutput);
                }
                for (String memberOutput : memberOutputs) {
                    text = new TextComponentString(memberOutput);
                    playerMP.sendMessage(text);
                }
                text = new TextComponentString(TextFormatting.GREEN + "Sect Allies: WIP");
                playerMP.sendMessage(text);
                String allyOutput = "";
                for (Pair<String, Boolean> ally : sectInfo.getAllies()) {
                    allyOutput += ally.getLeft() + ", ";
                }
                text = new TextComponentString(String.format("%5s", allyOutput));
                playerMP.sendMessage(text);
                text = new TextComponentString(TextFormatting.GREEN + "Sect Enemies:");
                playerMP.sendMessage(text);
                String enemyOutput = "";
                for (Pair<String, Boolean> enemy: sectInfo.getEnemies()) {
                    enemyOutput += enemy.getLeft() + ", ";
                }
                text = new TextComponentString(String.format("%5s", enemyOutput));
                playerMP.sendMessage(text);
                text = new TextComponentString("--------------------------------------------------");
                text.getStyle().setColor(TextFormatting.AQUA);
                playerMP.sendMessage(text);
            } else {
                TextComponentString text = new TextComponentString(targetPlayer.getName() + " does not belong to a sect.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        }
    }

    public void viewSectSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sectInfo = Sect.getSectByName(args[1], sectData);
        if (sectInfo != null) {
            TextComponentString text = new TextComponentString("--------------------------------------------------");
            text.getStyle().setColor(TextFormatting.AQUA);
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Name: " + TextFormatting.WHITE + sectInfo.getSectName());
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Tag: " + TextFormatting.WHITE + sectInfo.getSectTag());
            playerMP.sendMessage(text);
            GameProfile sectLeader = server.getPlayerProfileCache().getProfileByUUID(sectInfo.getSectLeader());
            String sectLeaderName = "";
            if (sectLeader != null) {
                sectLeaderName = sectLeader.getName();
            }
            text = new TextComponentString(TextFormatting.GREEN + "Sect Leader: " + TextFormatting.WHITE + sectLeaderName);
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Members: ");
            playerMP.sendMessage(text);
            String[] memberOutputs = new String[sectInfo.getMembers().size() / 2 + 1];
            int indexToStartFromNext = 0;
            for (int i = 0; i < memberOutputs.length; i++) {
                String tempOutput = "";
                for (int j = indexToStartFromNext; j < sectInfo.getMembers().size(); j++) {
                    Pair<UUID, String> member = sectInfo.getMembers().get(j);
                    UUID memberUUID = member.getLeft();
                    String memberRank = member.getRight();
                    GameProfile memberProfile = server.getPlayerProfileCache().getProfileByUUID(memberUUID);
                    String playerName = "";
                    if (memberProfile != null) {
                        playerName = memberProfile.getName();
                    }
                    tempOutput += "[" + memberRank + "]" + playerName + ", ";
                    if (j % 2 == 1 && j != 0) {
                        indexToStartFromNext = j + 1;
                        break;
                    }
                }
                memberOutputs[i] = String.format("%5s", tempOutput);
            }
            for (String memberOutput : memberOutputs) {
                text = new TextComponentString(memberOutput);
                playerMP.sendMessage(text);
            }
            text = new TextComponentString(TextFormatting.GREEN + "Sect Allies: WIP");
            playerMP.sendMessage(text);
            String allyOutput = "";
            for (Pair<String, Boolean> ally : sectInfo.getAllies()) {
                allyOutput += ally.getLeft() + ", ";
            }
            text = new TextComponentString(String.format("%5s", allyOutput));
            playerMP.sendMessage(text);
            text = new TextComponentString(TextFormatting.GREEN + "Sect Enemies:");
            playerMP.sendMessage(text);
            String enemyOutput = "";
            for (Pair<String, Boolean> enemy: sectInfo.getEnemies()) {
                enemyOutput += enemy.getLeft() + ", ";
            }
            text = new TextComponentString(String.format("%5s", enemyOutput));
            playerMP.sendMessage(text);
            text = new TextComponentString("--------------------------------------------------");
            text.getStyle().setColor(TextFormatting.AQUA);
            playerMP.sendMessage(text);
        } else {
            TextComponentString text = new TextComponentString( "That sect does not exist.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void setTagSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                if (args[1].length() <= 5) {
                    boolean tagExists = false;
                    for (Sect testSect : sectData.SECTS) {
                        if (testSect.getSectTag().equalsIgnoreCase(args[1])) {
                            tagExists = true;
                            break;
                        }
                    }
                    if (!tagExists) {
                        sect.setSectTag(args[1]);
                        TextComponentString text = new TextComponentString( "Sect Tag has been set to \"" + args[1] + "\"");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        playerMP.sendMessage(text);
                    } else {
                        TextComponentString text = new TextComponentString( "That Sect Tag already exists. Please try again.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    }
                } else {
                    TextComponentString text = new TextComponentString( "The Sect Tag cannot be longer than 5 characters. Please try again.");
                    text.getStyle().setColor(TextFormatting.RED);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString( "Only the Sect Leader has permission to change the Sect Tag.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString( "You do not belong to a sect.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void setTagColourSubCommand(String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                sect.setColour(args[1]);
            } else {
                TextComponentString text = new TextComponentString( "Only the Sect Leader has permission to change the Sect Tag.");
                text.getStyle().setColor(TextFormatting.RED);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString( "You do not belong to a sect.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }
}
