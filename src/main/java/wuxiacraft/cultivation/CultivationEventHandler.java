package wuxiacraft.cultivation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import wuxiacraft.networking.CultivationSyncMessage;
import wuxiacraft.networking.WuxiaPacketHandler;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
public class CultivationEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getPlayer();
        WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new CultivationSyncMessage(Cultivation.get(player)));
    }
}
