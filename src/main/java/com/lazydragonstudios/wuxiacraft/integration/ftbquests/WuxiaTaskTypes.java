package com.lazydragonstudios.wuxiacraft.integration.ftbquests;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import net.minecraft.resources.ResourceLocation;

public interface WuxiaTaskTypes extends TaskTypes {

	TaskType CULTIVATION_STAGE = TaskTypes.register(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_stage_task"), CultivationStageTask::new, () -> {
		return Icon.getIcon("minecraft:item/netherite_sword");
	});

}
