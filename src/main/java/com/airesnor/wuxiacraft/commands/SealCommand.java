package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.ISealing;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
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
        return "/seal <player> <system> [<level> <rank> : release] ";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        boolean wrongUsage = false;
        if (args.length == 3) {
            if (args[2].equals("release")) {
                EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
                if (player != null) {
                    ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
                    ISealing sealing = CultivationUtils.getSealingFromEntity(player);
                    switch (args[1]) {
                        case "body":
                            if (sealing.isBodySealed()) {
                                sealing.releaseBody(cultivation);
                                ITextComponent text = new TextComponentString("Player's body released");
                                sender.sendMessage(text);
                                text = new TextComponentString("Your body seal has been lifted!");
                                text.getStyle().setColor(TextFormatting.GRAY);
                                player.sendMessage(text);
                            } else {
                                ITextComponent text = new TextComponentString("Player's body wasn't sealed, bro");
                                text.getStyle().setColor(TextFormatting.RED);
                                sender.sendMessage(text);
                            }
                            break;
                        case "divine":
                            if (sealing.isDivineSealed()) {
                                sealing.releaseDivine(cultivation);
                                ITextComponent text = new TextComponentString("Player's divinity released");
                                sender.sendMessage(text);
                                text = new TextComponentString("Your divinity seal has been lifted!");
                                text.getStyle().setColor(TextFormatting.GRAY);
                                player.sendMessage(text);
                            } else {
                                ITextComponent text = new TextComponentString("Player's divinity wasn't sealed, bro");
                                text.getStyle().setColor(TextFormatting.RED);
                                sender.sendMessage(text);
                            }
                            break;
                        case "essence":
                            if (sealing.isEssenceSealed()) {
                                sealing.releaseEssence(cultivation);
                                ITextComponent text = new TextComponentString("Player's essence released");
                                sender.sendMessage(text);
                                text = new TextComponentString("Your essence seal has been lifted!");
                                text.getStyle().setColor(TextFormatting.GRAY);
                                player.sendMessage(text);
                            } else {
                                ITextComponent text = new TextComponentString("Player's essence wasn't sealed, bro");
                                text.getStyle().setColor(TextFormatting.RED);
                                sender.sendMessage(text);
                            }
                            break;
                    }

                } else {
                    ITextComponent textComponent = new TextComponentString("Player wasn't found! Use tab completions bro ...");
                    textComponent.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(textComponent);
                }
            } else {
                wrongUsage = true;
            }
        } else if (args.length == 4) {
            EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
            if (player != null) {
                ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
                ISealing sealing = CultivationUtils.getSealingFromEntity(player);
                BaseSystemLevel toLevel = null;
                switch (args[1]) {
                    case "body":
                        toLevel = BaseSystemLevel.getLevelInListByName(BaseSystemLevel.BODY_LEVELS, args[2]);
                        break;
                    case "divine":
                        toLevel = BaseSystemLevel.getLevelInListByName(BaseSystemLevel.DIVINE_LEVELS, args[2]);
                        break;
                    case "essence":
                        toLevel = BaseSystemLevel.getLevelInListByName(BaseSystemLevel.ESSENCE_LEVELS, args[2]);
                        break;
                    default:
                        ITextComponent textComponent = new TextComponentString("System not recognized, use tab completions");
                        textComponent.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(textComponent);
                }
                if (toLevel == null) {
                    ITextComponent textComponent = new TextComponentString("Level wasn't found, use tab completions ...");
                    textComponent.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(textComponent);
                } else {
                    try {
                        int toRank = Integer.parseInt(args[3]);
                        if (MathUtils.between(toRank, 0, toLevel.subLevels - 1)) {
                            switch (args[1]) {
                                case "body":
                                    if (sealing.isBodySealed()) {
                                        sealing.changeBodySeal(cultivation, toLevel, toRank);
                                    } else {
                                        sealing.sealBody(cultivation, toLevel, toRank);
                                    }
                                    break;
                                case "divine":
                                    if (sealing.isDivineSealed()) {
                                        sealing.changeDivineSeal(cultivation, toLevel, toRank);
                                    } else {
                                        sealing.sealDivine(cultivation, toLevel, toRank);
                                    }
                                    break;
                                case "essence":
                                    if (sealing.isEssenceSealed()) {
                                        sealing.changeEssenceSeal(cultivation, toLevel, toRank);
                                    } else {
                                        sealing.sealEssence(cultivation, toLevel, toRank);
                                    }
                                    break;
                            }
                        } else {
                            ITextComponent textComponent = new TextComponentString("Rank isn't supported for level ...");
                            textComponent.getStyle().setColor(TextFormatting.RED);
                            sender.sendMessage(textComponent);
                        }
                    } catch (NumberFormatException e) {
                        wrongUsage = true;
                    }

                }
            } else {
                ITextComponent textComponent = new TextComponentString("Player wasn't found! Use tab completions bro ...");
                textComponent.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(textComponent);
            }
        } else {
            wrongUsage = true;
        }
        if (wrongUsage) {
            ITextComponent textComponent = new TextComponentString(this.getUsage(sender));
            textComponent.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textComponent);
        }
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1: // names
                completions.addAll(Arrays.asList(server.getOnlinePlayerNames()));
                break;
            case 2: //system
                completions.add("body");
                completions.add("divine");
                completions.add("essence");
                break;
            case 3: //system level or release
                completions.add("release");
                switch (args[1]) {
                    case "body":
                        for (BaseSystemLevel level : BaseSystemLevel.BODY_LEVELS) {
                            completions.add(level.levelName);
                        }
                        break;
                    case "divine":
                        for (BaseSystemLevel level : BaseSystemLevel.DIVINE_LEVELS) {
                            completions.add(level.levelName);
                        }
                        break;
                    case "essence":
                        for (BaseSystemLevel level : BaseSystemLevel.ESSENCE_LEVELS) {
                            completions.add(level.levelName);
                        }
                        break;
                }
                break;
        } //no completion for rank after all it's a number
        return completions;
    }
}
