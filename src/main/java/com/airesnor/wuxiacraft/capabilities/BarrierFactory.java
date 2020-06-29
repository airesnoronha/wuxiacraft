package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.IBarrier;
import com.airesnor.wuxiacraft.cultivation.Barrier;

import java.util.concurrent.Callable;

public class BarrierFactory implements Callable<IBarrier> {
    @Override
    public IBarrier call() throws Exception{
        return new Barrier();
    }
}
