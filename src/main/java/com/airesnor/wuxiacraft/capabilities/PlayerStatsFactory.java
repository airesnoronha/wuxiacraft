package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.combat.IPlayerStats;
import com.airesnor.wuxiacraft.combat.PlayerStats;

import java.util.concurrent.Callable;

public class PlayerStatsFactory implements Callable<IPlayerStats> {

    @Override
    public IPlayerStats call() throws Exception {
        return new PlayerStats();
    }
}
