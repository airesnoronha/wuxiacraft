package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.world.Sect;
import com.airesnor.wuxiacraft.world.data.WorldSectData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.LinkedList;
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
                        disbandSubCommand(server, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        leaveSubCommand(playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("help")) {
                        helpSubCommand(playerMP);
                    } else if (args[0].equalsIgnoreCase("invites")) {
                        invitesSubCommand(server, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("ranks")) {
                        ranksSubCommand(playerMP, sectData);
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        createSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("join")) {
                        acceptSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("decline")) {
                        declineSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("kick")) {
                        kickSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("rename")) {
                        renameSubCommand(server, args, playerMP, sectData);
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
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("setrank")) {
                        setRankSubCommand(server, args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("createrank")) {
                        createRankSubCommand(args, playerMP, sectData);
                    } else if (args[0].equalsIgnoreCase("setrankpermission")) {
                        setRankPermissionSubCommand(args, playerMP, sectData);
                    }
                }
//                if (wrongUsage) {
//                    TextComponentString text = new TextComponentString("Invalid arguments, use /sect ");
//                    text.getStyle().setColor(TextFormatting.RED);
//                    playerMP.sendMessage(text);
//                }
                sectData.saveChanges();
            }
        }
    }

    @Override
    @Nonnull
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
                sectData.SECTS.add(new Sect(args[1], playerMP.getUniqueID()));
                LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) playerMP.getPrefixes();
                TextComponentString prefix = new TextComponentString("[" + args[1] + "]");
                prefix.getStyle().setColor(TextFormatting.AQUA);
                prefixes.add(0, prefix);
                TextComponentString text = new TextComponentString("Sect " + args[1] + " has been created.");
                text.getStyle().setColor(TextFormatting.GREEN);
                playerMP.sendMessage(text);
            }
        } else {
            TextComponentString text = new TextComponentString("You are already in a sect. To create a new sect you either disband or leave your current sect.");
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void infoSubCommand(MinecraftServer server, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sectInfo = Sect.getSectByPlayer(playerMP, sectData);
        if (sectInfo != null) {
            TextComponentString text = new TextComponentString("Sect Name: " + sectInfo.getSectName());
            playerMP.sendMessage(text);
            text = new TextComponentString("Sect Leader: " + server.getPlayerList().getPlayerByUUID(sectInfo.getSectLeader()).getName());
            playerMP.sendMessage(text);
            text = new TextComponentString("Sect Members: ");
            playerMP.sendMessage(text);
            String[] memberOutputs = new String[sectInfo.getMembers().size() / 3 + 1];
            int indexToStartFromNext = 0;
            for (int i = 0; i < memberOutputs.length; i++) {
                String tempOutput = "";
                for (int j = indexToStartFromNext; j < sectInfo.getMembers().size(); j++) {
                    Pair<UUID, String> member = sectInfo.getMembers().get(j);
                    UUID memberUUID = member.getLeft();
                    String memberRank = member.getRight();
                    EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(memberUUID);
                    if (player != null) {
                        tempOutput += "[" + memberRank + "]" + player.getName() + ", ";
                    }
                    if (j % 3 == 2 && j != 0) {
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
            text = new TextComponentString("Sect Allies: WIP");
            playerMP.sendMessage(text);
            String allyOutput = "";
            for (Pair<String, Boolean> ally : sectInfo.getAllies()) {
                allyOutput += ally.getLeft() + ", ";
            }
            text = new TextComponentString(String.format("%5s", allyOutput));
            playerMP.sendMessage(text);
            text = new TextComponentString("Sect Enemies:");
            playerMP.sendMessage(text);
            String enemyOutput = "";
            for (Pair<String, Boolean> enemy: sectInfo.getEnemies()) {
                enemyOutput += enemy.getLeft() + ", ";
            }
            text = new TextComponentString(String.format("%5s", enemyOutput));
            playerMP.sendMessage(text);
        } else {
            TextComponentString text = new TextComponentString("You do not belong to a sect.");
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
                    TextComponentString text = new TextComponentString("[" + args[1] + "] permission level has been set to " + args[2] + ".");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString("["+ args[1] + "] is not a rank of the sect.");
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
                    TextComponentString text = new TextComponentString("Rank [" + args[1] + "] has been created with a permission level of " + args[2] + ".");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                } else {
                    TextComponentString text = new TextComponentString("Rank [" + args[1] + "] already exists. Please choose another rank name.");
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
                    LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) playerMP.getPrefixes();
                    TextComponentString prefix = new TextComponentString("[" + args[1] + "]");
                    prefix.getStyle().setColor(TextFormatting.AQUA);
                    prefixes.add(0, prefix);
                } else {
                    TextComponentString text = new TextComponentString("You have not been invited to that sect.");
                    text.getStyle().setColor(TextFormatting.AQUA);
                    playerMP.sendMessage(text);
                }
            } else {
                TextComponentString text = new TextComponentString("You are already in a sect and cannot join another one.");
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

    public void disbandSubCommand(MinecraftServer server, EntityPlayerMP playerMP, WorldSectData sectData) {
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
        Sect sectOfPlayer;
        Sect sectOfTarget;
        if (targetPlayer != null) {
            sectOfPlayer = Sect.getSectByPlayer(playerMP, sectData);
            sectOfTarget = Sect.getSectByPlayer(targetPlayer, sectData);
            if (sectOfPlayer != null && sectOfTarget != null) {
                if (sectOfPlayer.getSectName().equalsIgnoreCase(sectOfTarget.getSectName())) {
                    if (!playerMP.getUniqueID().equals(targetPlayer.getUniqueID())) {
                        Pair<String, Integer> sectElderRank = sectOfPlayer.getRank("SectElder");
                        int kickPermissionLevel = sectElderRank.getRight();
                        Pair<UUID, String> memberPlayer = sectOfPlayer.getMemberByUUID(playerMP.getUniqueID());
                        Pair<String, Integer> playerRank = sectOfPlayer.getRank(memberPlayer.getRight());
                        Pair<UUID, String> memberTarget = sectOfTarget.getMemberByUUID(targetPlayer.getUniqueID());
                        Pair<String, Integer> targetRank = sectOfTarget.getRank(memberTarget.getRight());
                        if (sectOfPlayer.getSectLeader().equals(playerMP.getUniqueID())) {
                            sectOfPlayer.removeMember(memberTarget.getLeft());
                            TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been kicked from the sect.");
                            text.getStyle().setColor(TextFormatting.GREEN);
                            playerMP.sendMessage(text);
                            text = new TextComponentString("You have been kicked from the sect.");
                            text.getStyle().setColor(TextFormatting.RED);
                            targetPlayer.sendMessage(text);
                            LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) targetPlayer.getPrefixes();
                            prefixes.remove(0);
                        } else {
                            if (playerRank.getRight() >= kickPermissionLevel && playerRank.getRight() > targetRank.getRight()) {
                                sectOfPlayer.removeMember(memberTarget.getLeft());
                                TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been kicked from the sect.");
                                text.getStyle().setColor(TextFormatting.GREEN);
                                playerMP.sendMessage(text);
                                text = new TextComponentString("You have been kicked from the sect.");
                                text.getStyle().setColor(TextFormatting.RED);
                                targetPlayer.sendMessage(text);
                                LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) targetPlayer.getPrefixes();
                                prefixes.remove(0);
                            } else if (!(playerRank.getRight() >= kickPermissionLevel)) {
                                TextComponentString text = new TextComponentString("You do not have the permission to kick a member of the sect.");
                                text.getStyle().setColor(TextFormatting.RED);
                                playerMP.sendMessage(text);
                            } else if (!(playerRank.getRight() > targetRank.getRight())) {
                                TextComponentString text = new TextComponentString(targetPlayer.getName() + " cannot be kicked as they either have the same or higher level of authority than you.");
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
                LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) playerMP.getPrefixes();
                prefixes.remove(0);
            }
        } else {
            TextComponentString text = new TextComponentString("Couldn't find sect of player: " + playerMP.getName());
            text.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(text);
        }
    }

    public void renameSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        if (sect != null) {
            if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                Sect testSect = Sect.getSectByName(args[1], sectData);
                if (testSect == null) {
                    sect.setSectName(args[1]);
                    for (Pair<UUID, String> member : sect.getMembers()) {
                        EntityPlayerMP memberPlayer = server.getPlayerList().getPlayerByUUID(member.getLeft());
                        if (memberPlayer != null) {
                            LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) memberPlayer.getPrefixes();
                            prefixes.remove(0);
                            TextComponentString prefix = new TextComponentString("[" + args[1] + "]");
                            prefix.getStyle().setColor(TextFormatting.AQUA);
                            prefixes.add(0, prefix);
                        }
                    }
                    EntityPlayerMP sectLeader = server.getPlayerList().getPlayerByUUID(sect.getSectLeader());
                    if (sectLeader != null) {
                        LinkedList<ITextComponent> prefixes = (LinkedList<ITextComponent>) sectLeader.getPrefixes();
                        prefixes.remove(0);
                        TextComponentString prefix = new TextComponentString("[" + args[1] + "]");
                        prefix.getStyle().setColor(TextFormatting.AQUA);
                        prefixes.add(0, prefix);
                    }
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
                EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                if (targetPlayer != null) {
                    if (sect.isChangingLeader()) {
                        boolean leaderChanged = false;
                        for (Pair<UUID, String> member : sect.getMembers()) {
                            if (member.getLeft().equals(targetPlayer.getUniqueID())) {
                                sect.setSectLeader(member.getLeft());
                                leaderChanged = true;
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
        TextComponentString text = new TextComponentString("Sect Help Information:");
        text.getStyle().setColor(TextFormatting.AQUA);
        playerMP.sendMessage(text);
        text = new TextComponentString("/sect create <sectName>");
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
    }

    public void inviteSubCommand(MinecraftServer server, String[] args, EntityPlayerMP playerMP, WorldSectData sectData) {
        Sect sect = Sect.getSectByPlayer(playerMP, sectData);
        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
        if (sect != null) {
            Pair<String, Integer> sectElderRank = sect.getRank("SectElder");
            int invitePermissionLevel = sectElderRank.getRight();
            Pair<UUID, String> memberPlayer = sect.getMemberByUUID(playerMP.getUniqueID());
            Pair<String, Integer> playerRank = sect.getRank(memberPlayer.getRight());
            if (targetPlayer != null) {
                if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                    sect.addPlayerToInvitation(targetPlayer.getUniqueID());
                    TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been invited to the sect.");
                    text.getStyle().setColor(TextFormatting.GREEN);
                    playerMP.sendMessage(text);
                } else {
                    if (playerRank.getRight() >= invitePermissionLevel) {
                        sect.addPlayerToInvitation(targetPlayer.getUniqueID());
                        TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been invited to the sect.");
                        text.getStyle().setColor(TextFormatting.GREEN);
                        playerMP.sendMessage(text);
                    } else {
                        TextComponentString text = new TextComponentString("You do not have the authority to invite players to the sect.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
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
            Pair<UUID, String> memberPlayer = sect.getMemberByUUID(playerMP.getUniqueID());
            Pair<String, Integer> playerRank = sect.getRank(memberPlayer.getRight());
            if (targetPlayer != null) {
                if (sect.getSectLeader().equals(playerMP.getUniqueID())) {
                    sect.removePlayerFromInvitations(targetPlayer.getUniqueID());
                } else {
                    if (playerRank.getRight() >= invitePermissionLevel) {
                        sect.removePlayerFromInvitations(targetPlayer.getUniqueID());
                    } else {
                        TextComponentString text = new TextComponentString("You do not have the authority to cancel invitations to the sect.");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
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
                EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                if (targetPlayer != null) {
                    Sect sectOfTarget = Sect.getSectByPlayer(targetPlayer, sectData);
                    if (sectOfTarget != null) {
                        if (sect.getSectName().equalsIgnoreCase(sectOfTarget.getSectName())) {
                            Pair<UUID, String> memberTarget = sect.getMemberByUUID(targetPlayer.getUniqueID());
                            Pair<String, Integer> sectRank = sect.getRank(args[2]);
                            if (sectRank != null) {
                                memberTarget.setValue(sectRank.getLeft());
                                TextComponentString text = new TextComponentString(targetPlayer.getName() + " has been set to [" + args[2] + "].");
                                text.getStyle().setColor(TextFormatting.GREEN);
                                playerMP.sendMessage(text);
                            } else {
                                TextComponentString text = new TextComponentString("["+ args[2] + "] is not a rank of the sect.");
                                text.getStyle().setColor(TextFormatting.RED);
                                playerMP.sendMessage(text);
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
        if (playerMP.isCreative()) {
            int i = 0;
            TextComponentString text = new TextComponentString("Total number of sects: " + sectData.SECTS.size());
            playerMP.sendMessage(text);
            for (Sect sect : sectData.SECTS) {
                i++;
                text = new TextComponentString(i + ". " + sect.getSectName());
                playerMP.sendMessage(text);
            }
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
}
