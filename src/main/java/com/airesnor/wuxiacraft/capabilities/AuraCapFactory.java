package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.aura.AuraCap;

import java.util.concurrent.Callable;

public class AuraCapFactory implements Callable<AuraCap> {
	@Override
	public AuraCap call() throws Exception {
		return new AuraCap();
	}
}
