package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.Cultivation;

import java.util.concurrent.Callable;

public class CultivationFactory implements Callable<Cultivation> {
	@Override
	public Cultivation call() throws Exception {
		return new Cultivation();
	}
}
