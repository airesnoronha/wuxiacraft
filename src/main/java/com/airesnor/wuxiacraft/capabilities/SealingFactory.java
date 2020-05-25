package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.ISealing;
import com.airesnor.wuxiacraft.cultivation.Sealing;

import java.util.concurrent.Callable;

public class SealingFactory implements Callable<ISealing> {

    @Override
    public ISealing call() {
        return new Sealing();
    }
}
