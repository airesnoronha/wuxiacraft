package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class StatArgument implements ArgumentType<String> {

    private static final Collection<String> EXAMPLES = Arrays.asList("health", "lives");

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(EnumSet.allOf(PlayerStat.class), builder, Object::toString, stat -> stat::toString);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static StatArgument id() {
        return new StatArgument();
    }
}
