package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.networking.CultivationSyncMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.math.BigDecimal;

public class StatCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stat")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.literal("get")
                                .executes(StatCommand::getStats)
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("stat", StatArgument.id())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(StatCommand::setStat)
                                        )
                                )
                        )
                )
        );
    }

    public static void syncClientCultivation(ServerPlayer player) {
        WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CultivationSyncMessage(Cultivation.get(player)));
    }

    public static int getStats(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        ICultivation cultivation = Cultivation.get(target);
        TextComponent message = new TextComponent("");
        message.append("Player Stats: ").append("\n");
        for (var stat : PlayerStat.values()) {
            String statName = stat.name();
            message.append(statName).append(": ").append(String.format("%.1f", cultivation.getStat(stat))).append("\n");
        }
        ctx.getSource().sendSuccess(message, true);
        return 1;
    }

    public static int setStat(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        String stat = ctx.getArgument("stat", String.class);
        int amount = IntegerArgumentType.getInteger(ctx, "amount");

        ICultivation cultivation = Cultivation.get(target);
        TextComponent message = new TextComponent("");

        PlayerStat playerStat = PlayerStat.valueOf(stat);
        cultivation.setStat(playerStat, BigDecimal.valueOf(amount));

        message.append("Successfully set the target's " + stat + " stat.");
        ctx.getSource().sendSuccess(message, true);
        syncClientCultivation(target);
        return 1;
    }
}
