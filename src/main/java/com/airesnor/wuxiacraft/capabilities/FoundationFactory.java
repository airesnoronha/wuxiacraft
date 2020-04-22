package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.Foundation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;

import java.util.concurrent.Callable;

public class FoundationFactory implements Callable<IFoundation> {

	@Override
	public IFoundation call() {
		return new Foundation();
	}
}
