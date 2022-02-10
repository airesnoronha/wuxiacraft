package com.lazydragonstudios.wuxiacraft.integration.ftbquests;

import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.task.CustomTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;

public class CultivationStageTask extends CustomTask {

	public CultivationStageTask(Quest q) {
		super(q);
	}

	@Override
	public TaskType getType() {
		return WuxiaTaskTypes.CULTIVATION_STAGE;
	}
}
