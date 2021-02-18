package wuxiacraft.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import wuxiacraft.capabilities.CultivationProvider;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

import javax.annotation.Nonnull;

public class CultivationUtils {

	@Nonnull
	public static ICultivation getCultivationFromEntity(LivingEntity entity) {
		ICultivation cultivation = null;
		if(entity instanceof PlayerEntity) {
			cultivation = (ICultivation) entity.getCapability(CultivationProvider.CULTIVATION_PROVIDER, null);
		}
		if(cultivation == null) {
			cultivation = new Cultivation();
		}
		return cultivation;
	}

}
