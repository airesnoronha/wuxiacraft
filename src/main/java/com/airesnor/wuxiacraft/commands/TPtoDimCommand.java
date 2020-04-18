package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.utils.TeleportationUtil;
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

public class TPtoDimCommand extends CommandBase {

    public TPtoDimCommand() {
        super();
    }

    @Override
    @Nonnull
    public String getName() {
        return "tptodim";
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/tptodim <dimensionID> or /tptodim <player> <player>";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("tp2dim");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            if(!playerMP.world.isRemote) {
                boolean wrongUsage;
                if(args.length == 1) {
                    String dimension = args[0];
                    int dimensionID;
                    if (dimension.equalsIgnoreCase("nether")) {
                        dimensionID = -1;
                    } else if(dimension.equalsIgnoreCase("overworld")) {
                        dimensionID = 0;
                    } else if(dimension.equalsIgnoreCase("end")) {
                        dimensionID = 1;
                    } else {
                        dimensionID = Integer.parseInt(dimension);
                    }

                    if (dimensionID == 1) {
                        TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) sender, dimensionID,
                                100,  60, 0, 0f, 0f);
                    } else {
                        TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) sender, dimensionID,
                                sender.getPosition().getX(),  sender.getPosition().getY() + 20, sender.getPosition().getZ(),
                                0f, 0f);
                    }
                    wrongUsage = false;
                } else if (args.length == 3) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    EntityPlayerMP initialPlayer = server.getPlayerList().getPlayerByUsername(args[0]);
                    int dimensionID = 0;
                    try{
                        dimensionID = targetPlayer.world.provider.getDimension();
                    }catch (NullPointerException e) {
                        TextComponentString text = new TextComponentString("Target Player is not in a known dimension!");
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                    }
                    TeleportationUtil.teleportPlayerToPlayer(initialPlayer, targetPlayer, dimensionID,
                            targetPlayer.getPosition().getX(), targetPlayer.getPosition().getY(), targetPlayer.getPosition().getZ(), 0f, 0f);
                    wrongUsage = false;
                } else {
                    wrongUsage = true;
                }
                if (wrongUsage) {
                    TextComponentString text = new TextComponentString("Invalid arguments, use /tptodim <dimension> or /tptodim <player> <player>");
                    text.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(text);
                }
            }
        }else throw new CommandException("Not used correctly!");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("nether".startsWith(args[0]))
                completions.add("nether");
            if ("overworld".startsWith(args[0]))
                completions.add("overworld");
            if ("end".startsWith(args[0]))
                completions.add("end");
        }
        return completions;
    }

}
