package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class SystemArgument implements ArgumentType<String> {

	private static final Collection<String> EXAMPLES = Arrays.asList("essence", "body", "divine");

	private static final DynamicCommandExceptionType ERROR_UNKNOWN_SYSTEM = new DynamicCommandExceptionType(
			(system) -> new TranslatableComponent("wuxiacraft.system_not_found", system)
	);


	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readString();
	}

	public static System getSystem(CommandContext<CommandSourceStack> ctx, String argName) throws CommandSyntaxException {
		var systemName = ctx.getArgument(argName, String.class);
		try {
			return System.valueOf(systemName.toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw ERROR_UNKNOWN_SYSTEM.create(systemName);
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(EnumSet.allOf(System.class), builder, Object::toString, stat -> stat::toString);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static StatArgument system() {
		return new StatArgument();
	}
}
