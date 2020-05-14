package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.ISealing;
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

public class SealCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "seal";
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/seal <player> [cultivation <level> <rank> : foundation <attribute> <value> : both <level> <rank> <attribute> <value> : status] and to unseal /seal <player> [foundation : cultivation : (nothing to unseal both)]";
    }

    @Override
    @Nonnull
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
            boolean wrongUsage = true;
            if(!playerMP.world.isRemote) {
                if (args.length == 1) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[0]);
                    ISealing sealing = CultivationUtils.getSealingFromEntity(targetPlayer);
                    if (sealing.isCultivationSealed() && sealing.isFoundationSealed()) {
                        ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                        IFoundation foundation = CultivationUtils.getFoundationFromEntity(targetPlayer);
                        setCultivationUnsealed(sealing, cultivation);
                        setFoundationUnsealed(sealing, foundation);
                        NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
                        sealing.setSealed("both", false);
                    } else if (sealing.isCultivationSealed()) {
                        ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                        setCultivationUnsealed(sealing, cultivation);
                        NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
                        sealing.setSealed("cultivation", false);
                    } else if (sealing.isFoundationSealed()) {
                        IFoundation foundation = CultivationUtils.getFoundationFromEntity(targetPlayer);
                        setFoundationUnsealed(sealing, foundation);
                        sealing.setSealed("foundation", false);
                    } else {
                        TextComponentString text = new TextComponentString( targetPlayer.getName() + " is not sealed!");
                        text.getStyle().setColor(TextFormatting.RED);
                        playerMP.sendMessage(text);
                    }
                } else if (args.length == 2) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[0]);
                    ISealing sealing = CultivationUtils.getSealingFromEntity(targetPlayer);
                    if (args[1].equalsIgnoreCase("cultivation")) {
                        if (sealing.isCultivationSealed()) {
                            ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                            setCultivationUnsealed(sealing, cultivation);
                        } else {
                            TextComponentString text = new TextComponentString( targetPlayer.getName() + "'s cultivation is not sealed!");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        }
                        sealing.setSealed("cultivation", false);
                    } else if (args[1].equalsIgnoreCase("foundation")) {
                        if (sealing.isFoundationSealed()) {
                            IFoundation foundation = CultivationUtils.getFoundationFromEntity(targetPlayer);
                            setFoundationUnsealed(sealing, foundation);
                        } else {
                            TextComponentString text = new TextComponentString( targetPlayer.getName() + "'s foundation is not sealed!");
                            text.getStyle().setColor(TextFormatting.RED);
                            playerMP.sendMessage(text);
                        }
                        sealing.setSealed("foundation", false);
                    }
                } else if (args.length == 4) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[0]);
                    if (args[1].equalsIgnoreCase("cultivation")) {
                        ISealing sealing = CultivationUtils.getSealingFromEntity(targetPlayer);
                        ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                        IFoundation foundation = CultivationUtils.getFoundationFromEntity(targetPlayer);
                        if (!sealing.isCultivationSealed() && !sealing.isFoundationSealed()) {
                            sealing.copyFromBoth(cultivation, foundation);
                            int subLevel = Integer.parseInt(args[3]) - 1;
                            CultivationLevel level = cultivation.getCurrentLevel();
                            boolean found_level = false;
                            for (CultivationLevel l : CultivationLevel.LOADED_LEVELS.values()) {
                                if (l.getUName().equals(args[2])) {
                                    level = l;
                                    found_level = true;
                                    break;
                                }
                            }
                            if (found_level) {
                                cultivation.setCurrentLevel(level);
                                cultivation.setCurrentSubLevel(subLevel);
                            }
                        } else if (!sealing.isCultivationSealed() && sealing.isFoundationSealed()) {
                            sealing.copyFromCultivation(cultivation);
                            int subLevel = Integer.parseInt(args[3]) - 1;
                            CultivationLevel level = cultivation.getCurrentLevel();
                            boolean found_level = false;
                            for (CultivationLevel l : CultivationLevel.LOADED_LEVELS.values()) {
                                if (l.getUName().equals(args[2])) {
                                    level = l;
                                    found_level = true;
                                    break;
                                }
                            }
                            if (found_level) {
                                cultivation.setCurrentLevel(level);
                                cultivation.setCurrentSubLevel(subLevel);
                            }
                        } else {
                            int subLevel = Integer.parseInt(args[3]) - 1;
                            CultivationLevel level = cultivation.getCurrentLevel();
                            boolean found_level = false;
                            for (CultivationLevel l : CultivationLevel.LOADED_LEVELS.values()) {
                                if (l.getUName().equals(args[2])) {
                                    level = l;
                                    found_level = true;
                                    break;
                                }
                            }
                            if (found_level) {
                                cultivation.setCurrentLevel(level);
                                cultivation.setCurrentSubLevel(subLevel);
                            }
                        }
                        sealing.setSealed("cultivation", true);
                    } else if (args[1].equalsIgnoreCase("foundation")) {
                        ISealing sealing = CultivationUtils.getSealingFromEntity(targetPlayer);
                        ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                        IFoundation foundation = CultivationUtils.getFoundationFromEntity(targetPlayer);
                        long value = Long.parseLong(args[3]);
                        if (!sealing.isFoundationSealed() && !sealing.isCultivationSealed()) {
                            sealing.copyFromBoth(cultivation, foundation);
                            setAttrValue(foundation, args[3], value);
                        } else if (!sealing.isFoundationSealed() && sealing.isCultivationSealed()) {
                            sealing.copyFromFoundation(foundation);
                            setAttrValue(foundation, args[3], value);
                        } else {
                            setAttrValue(foundation, args[3], value);
                        }
                        sealing.setSealed("foundation", true);
                    }
                } else if (args.length == 6) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[0]);
                    if (args[1].equalsIgnoreCase("both")) {
                        
                    }
                }
                if (wrongUsage) {
                    TextComponentString text = new TextComponentString("Invalid arguments, use /seal <player> [cultivation <level> <rank> : foundation <attribute> <value> : both <level> <rank> <attribute> <value>] and to unseal /seal <player> [foundation : cultivation : (nothing to unseal both)]");
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
            for (String name : server.getPlayerList().getOnlinePlayerNames()) {
                if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(name);
                }
            }
        }
        if (args.length == 2) {
            if ("cultivation".startsWith(args[1].toLowerCase())) {
                completions.add("cultivation");
            }
            if ("foundation".startsWith(args[1].toLowerCase())) {
                completions.add("foundation");
            }
            if ("both".startsWith(args[1].toLowerCase())) {
                completions.add("both");
            }
            if ("status".startsWith(args[1].toLowerCase())) {
                completions.add("status");
            }
        }
        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("cultivation") || args[1].equalsIgnoreCase("both")) {
                for (CultivationLevel level : CultivationLevel.LOADED_LEVELS.values()) {
                    if (level.levelName.toLowerCase().startsWith(args[2].toLowerCase()))
                        completions.add(level.getUName());
                }
            }
            if (args[1].equalsIgnoreCase("foundation")) {
                if("agi".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("agi");
                }
                if("con".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("con");
                }
                if("dex".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("dex");
                }
                if("res".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("res");
                }
                if("spi".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("spi");
                }
                if("str".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("str");
                }
                if("all".toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add("all");
                }
            }
        }
        if (args.length == 5) {
            if("agi".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("agi");
            }
            if("con".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("con");
            }
            if("dex".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("dex");
            }
            if("res".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("res");
            }
            if("spi".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("spi");
            }
            if("str".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("str");
            }
            if("all".toLowerCase().startsWith(args[4].toLowerCase())) {
                completions.add("all");
            }
        }
        return completions;
    }

    public void setAttrValue(IFoundation foundation, String attribute, long value) {
        if (attribute.equalsIgnoreCase("agi")) {
            foundation.setAgility(value);
        } else if (attribute.equalsIgnoreCase("con")) {
            foundation.setConstitution(value);
        } else if (attribute.equalsIgnoreCase("dex")) {
            foundation.setDexterity(value);
        } else if (attribute.equalsIgnoreCase("res")) {
            foundation.setResistance(value);
        } else if (attribute.equalsIgnoreCase("spi")) {
            foundation.setSpirit(value);
        } else if (attribute.equalsIgnoreCase("str")) {
            foundation.setStrength(value);
        } else if (attribute.equalsIgnoreCase("all")) {
            foundation.setAgility(value);
            foundation.setConstitution(value);
            foundation.setDexterity(value);
            foundation.setResistance(value);
            foundation.setSpirit(value);
            foundation.setStrength(value);
        }
    }

    public void setCultivationUnsealed(ISealing sealing, ICultivation cultivation) {
        cultivation.setCurrentLevel(sealing.getCurrentLevel());
        cultivation.setCurrentSubLevel(sealing.getCurrentSubLevel());
        cultivation.setEnergy(sealing.getEnergy());
        cultivation.setProgress(sealing.getCurrentProgress());
        cultivation.setHasteLimit(sealing.getHasteLimit());
        cultivation.setJumpLimit(sealing.getJumpLimit());
        cultivation.setMaxSpeed(sealing.getMaxSpeed());
    }

    public void setFoundationUnsealed(ISealing sealing, IFoundation foundation) {
        foundation.setAgility(sealing.getAgility());
        foundation.setAgilityProgress(sealing.getAgilityProgress());
        foundation.setConstitution(sealing.getConstitution());
        foundation.setConstitutionProgress(sealing.getConstitutionProgress());
        foundation.setDexterity(sealing.getDexterity());
        foundation.setDexterityProgress(sealing.getDexterityProgress());
        foundation.setResistance(sealing.getResistance());
        foundation.setResistanceProgress(sealing.getResistanceProgress());
        foundation.setSpirit(sealing.getSpirit());
        foundation.setSpiritProgress(sealing.getSpiritProgress());
        foundation.setStrength(sealing.getStrength());
        foundation.setStrengthProgress(sealing.getStrengthProgress());
    }
}
