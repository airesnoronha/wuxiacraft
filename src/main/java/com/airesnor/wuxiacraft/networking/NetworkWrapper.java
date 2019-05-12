package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkWrapper {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WuxiaCraft.MODID);
}
