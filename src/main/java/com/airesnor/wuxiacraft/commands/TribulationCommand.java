package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class TribulationCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "tribulation";
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/tribulation [get:perform] <player> or /tribulation get cult <level> <rank>";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("trib");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if(!player.world.isRemote) {
                boolean wrongUsage = true;
                WorldServer world = (WorldServer) player.world;
                EntityPlayerMP targetPlayer = player;

                if(args.length >= 2) {
                    targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                }

                ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                ICultivation tribulation = new Cultivation();

                tribulation.copyFrom(cultivation);
                tribulation.setCurrentSubLevel(tribulation.getCurrentSubLevel() + 1);

                if (tribulation.getCurrentSubLevel() >= tribulation.getCurrentLevel().subLevels) {
                    tribulation.setCurrentSubLevel(0);
                    tribulation.setCurrentLevel(tribulation.getCurrentLevel().getNextLevel());
                }

                IFoundation foundation = CultivationUtils.getFoundationFromEntity(targetPlayer);
                double resistance = foundation.getAgilityModifier() + foundation.getConstitutionModifier() +
                        foundation.getDexterityModifier() + foundation.getResistanceModifier() +
                        foundation.getSpiritModifier() + foundation.getStrengthModifier();
                int multiplier = world.getGameRules().hasRule("tribulationMultiplier") ? world.getGameRules().getInt("tribulationMultiplier") : 18; // even harder for those that weren't on the script
                double strength = tribulation.getStrengthIncrease() * multiplier;
                int bolts = MathUtils.clamp(1 + (int) (Math.round(resistance / (cultivation.getStrengthIncrease()*4))), 1, 12);
                float damage = (float) Math.max(2, strength - resistance);

                if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("get")) {
                        if(targetPlayer != null) {
                            TextComponentString text = new TextComponentString("Tribulation Information: ");
                            sender.sendMessage(text);
                            text = new TextComponentString("Resistance: " + resistance);
                            sender.sendMessage(text);
                            text = new TextComponentString("Multiplier: " + multiplier);
                            sender.sendMessage(text);
                            text = new TextComponentString("Strength: " + strength);
                            sender.sendMessage(text);
                            text = new TextComponentString("Damage: " + damage);
                            sender.sendMessage(text);
                            text = new TextComponentString("Bolts: " + bolts);
                            sender.sendMessage(text);
                            wrongUsage = false;
                        } else {
                            TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                            text.getStyle().setColor(TextFormatting.RED);
                            sender.sendMessage(text);
                        }
                    }
                    if(args[0].equalsIgnoreCase("perform")) {
                        CultivationUtils.callCustomTribulation(targetPlayer, cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), true);
                        wrongUsage = false;
                    }
                }else if(args.length == 4) {
                    if(args[0].equalsIgnoreCase("get")) {
                        if(args[1].equalsIgnoreCase("cult")) {
                            cultivation = CultivationUtils.getCultivationFromEntity(player);
                            tribulation = new Cultivation();
                            tribulation.copyFrom(cultivation);
                            CultivationLevel level = tribulation.getCurrentLevel();
                            int subLevel = Integer.parseInt(args[3]) - 1;
                            for (CultivationLevel l : CultivationLevel.REGISTERED_LEVELS.values()) {
                                if (l.getUName().equals(args[2])) {
                                    level = l;
                                    break;
                                }
                            }
                            tribulation.setCurrentSubLevel(subLevel + 1);
                            tribulation.setCurrentLevel(level);
                            if (tribulation.getCurrentSubLevel() >= tribulation.getCurrentLevel().subLevels) {
                                tribulation.setCurrentSubLevel(0);
                                tribulation.setCurrentLevel(level.getNextLevel());
                            }
                            multiplier = world.getGameRules().hasRule("tribulationMultiplier") ? world.getGameRules().getInt("tribulationMultiplier") : 18; // even harder for those that weren't on the script
                            strength = tribulation.getStrengthIncrease() * multiplier;

                            TextComponentString text = new TextComponentString("Tribulation Information: ");
                            sender.sendMessage(text);
                            text = new TextComponentString("Multiplier: " + multiplier);
                            sender.sendMessage(text);
                            text = new TextComponentString("Strength: " + strength);
                            sender.sendMessage(text);
                            wrongUsage = false;
                        }
                    }
                    if(args[0].equalsIgnoreCase("perform")) {
                        CultivationLevel level = cultivation.getCurrentLevel();
                        int subLevel = Integer.parseInt(args[3]) - 1;
                        for (CultivationLevel l : CultivationLevel.REGISTERED_LEVELS.values()) {
                            if (l.getUName().equals(args[2])) {
                                level = l;
                                break;
                            }
                        }
                        CultivationUtils.callCustomTribulation(targetPlayer, level, subLevel, true);
                        wrongUsage = false;
                    }
                }
                if (wrongUsage) {
                    TextComponentString text = new TextComponentString("Invalid arguments, use /tribulation [get <player>]:[get cult <level> <rank>]:[perform <player>]:[perform <player> <level> <rank>]");
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
        if(args.length == 1) {
            if("get".startsWith(args[0])) {
                completions.add("get");
            }
            if("perform".startsWith(args[0])) {
                completions.add("perform");
            }
        }else if(args.length == 2) {
            for(String player : server.getPlayerList().getOnlinePlayerNames()) {
                if(player.toLowerCase().startsWith(args[1])){
                    completions.add(player);
                }
            }
        }else if(args.length == 3) {
            for (CultivationLevel level : CultivationLevel.REGISTERED_LEVELS.values()) {
                if(level.getUName().toLowerCase().startsWith(args[2])) {
                    completions.add(level.getUName());
                }
            }
        }
        return completions;
    }
}
