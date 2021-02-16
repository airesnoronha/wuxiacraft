package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.FoundationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.ProgressMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;

public class SkillBreakthrough extends Skill{

	public SkillBreakthrough(String name, Cultivation.System system) {
		super(name, true, false, 0, 0, 300, 0);
		setWhenCasting(actor -> {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
			if(cultivation.getSystemProgress(system) < cultivation.getSystemLevel(system).getProgressBySubLevel(cultivation.getSystemSubLevel(system))) {
				double convertSpeed = 15.0; //this is per tick
				if(cultivation.getSystemFoundation(system) > convertSpeed) {
					cultivation.addSystemProgress(convertSpeed, system);
					cultivation.addSystemFoundation(-convertSpeed, system);
					NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(2, system, cultivation.getSystemProgress(system), false, actor.getUniqueID()));
					NetworkWrapper.INSTANCE.sendToServer(new FoundationMessage(1, system, convertSpeed, actor.getUniqueID()));
				}
			} else if(cultivation.getSystemProgress(system) == cultivation.getSystemLevel(system).getProgressBySubLevel(cultivation.getSystemSubLevel(system))) {
				//this is the actual breakthrough
				double foundationOverCultBase = cultivation.getSystemFoundation(system) / cultivation.getSystemLevel(system).getProgressBySubLevel(cultivation.getSystemSubLevel(system));

			}
			return true;
		});
	}
}
