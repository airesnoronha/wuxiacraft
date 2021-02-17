package wuxiacraft.capabilities;

import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

import java.util.concurrent.Callable;

public class CultivationFactory implements Callable<ICultivation> {

	@Override
	public ICultivation call() throws Exception {
		return new Cultivation();
	}
}
