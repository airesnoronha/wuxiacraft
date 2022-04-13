package com.lazydragonstudios.wuxiacraft.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class AspectArgument extends ResourceLocationArgument {

	private static final Collection<String> EXAMPLES = Arrays.asList("wuxiacraft:start", "wuxiacraft:body_gathering");

	private static final DynamicCommandExceptionType ERROR_UNKNOWN_ASPECT = new DynamicCommandExceptionType(
			(aspectLocation) -> new TranslatableComponent("wuxiacraft.aspect_not_found", aspectLocation)
	);

	public static ResourceLocation getAspectLocation(CommandContext<CommandSourceStack> ctx, String argName) throws CommandSyntaxException {
		ResourceLocation resourcelocation = ctx.getArgument(argName, ResourceLocation.class);
		if (!WuxiaRegistries.TECHNIQUE_ASPECT.get().containsKey(resourcelocation)) {
			throw ERROR_UNKNOWN_ASPECT.create(resourcelocation);
		}
		return resourcelocation;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(WuxiaRegistries.TECHNIQUE_ASPECT.get().getKeys(), builder);
	}

	public static AspectArgument id() {
		return new AspectArgument();
	}

}
