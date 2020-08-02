package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
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
        return "/tribulation <player> <strength>";
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    @Nonnull
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("trib");
        aliases.add("punish");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        boolean wrongUsage = false;
        if (args.length == 2) {
            EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
            if (target == null) {
                TextComponentString text = new TextComponentString("Couldn't find target player, use tab completions ...");
                text.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(text);
            } else {
                try {
                    double strength = parseDouble(args[1], 0);
                    CultivationUtils.callCustomTribulation(target, strength, true);
                } catch (NumberInvalidException e) {
                    WuxiaCraft.logger.error("Couldn't parse tribulation strength!");
                    TextComponentString text = new TextComponentString("Couldn't parse tribulation strength!");
                    text.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(text);
                }
            }
        } else {
            wrongUsage = true;
        }
        if (wrongUsage) {
            TextComponentString text = new TextComponentString("Wrong usage, use" + getUsage(sender));
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
        }
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList(server.getOnlinePlayerNames()));
        }
        return completions;
    }
}
