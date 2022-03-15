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
public class StageArgument extends ResourceLocationArgument {

	private static final Collection<String> EXAMPLES = Arrays.asList("wuxiacraft:essence_mortal_stage", "wuxiacraft:essence_qi_gathering_stage");

	private static final DynamicCommandExceptionType ERROR_UNKNOWN_STAGE= new DynamicCommandExceptionType(
			(aspectLocation) -> new TranslatableComponent("wuxiacraft.stage_not_found", aspectLocation)
	);

	public static ResourceLocation getStageLocation(CommandContext<CommandSourceStack> ctx, String argName) throws CommandSyntaxException {
		ResourceLocation resourcelocation = ctx.getArgument(argName, ResourceLocation.class);
		if (!WuxiaRegistries.CULTIVATION_STAGES.containsKey(resourcelocation)) {
			throw ERROR_UNKNOWN_STAGE.create(resourcelocation);
		}
		return resourcelocation;
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(WuxiaRegistries.CULTIVATION_STAGES.getKeys(), builder);
	}

	public static StageArgument id() {
		return new StageArgument();
	}

}
