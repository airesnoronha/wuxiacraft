package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;

import java.util.concurrent.Callable;

public class CultTechFactory implements Callable<CultTech> {
	@Override
	public CultTech call() throws Exception {
		return new CultTech();
	}
}
