package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ElementArgument extends ResourceLocationArgument {

	private static final Collection<String> EXAMPLES = Arrays.asList("wuxiacraft:fire", "wuxiacraft:earth");

	private static final DynamicCommandExceptionType ERROR_UNKNOWN_ELEMENT = new DynamicCommandExceptionType(
			(aspectLocation) -> new TranslatableComponent("wuxiacraft.element_not_found", aspectLocation)
	);

	public static ResourceLocation getAspectLocation(CommandContext<CommandSourceStack> ctx, String argName) throws CommandSyntaxException {
		ResourceLocation resourcelocation = ctx.getArgument(argName, ResourceLocation.class);
		if (!WuxiaRegistries.ELEMENTS.get().containsKey(resourcelocation)) {
			throw ERROR_UNKNOWN_ELEMENT.create(resourcelocation);
		}
		return resourcelocation;
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(WuxiaRegistries.ELEMENTS.get().getKeys(), builder);
	}

	public static ElementArgument id() {
		return new ElementArgument();
	}

}
