package cn.ussshenzhou.channel.subspace.server;

import cn.ussshenzhou.channel.config.ChannelServerConfig;
import cn.ussshenzhou.channel.subspace.server.send.DataUpdatePacket;
import cn.ussshenzhou.channel.subspace.server.send.PlayerLogoutPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

/**
 * @author USS_Shenzhou
 */
public class ServerEventListener {

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (ChannelServerConfig.get().useSubspace) {
                SubspaceConnection.connect(server);
            }
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (SubspaceConnection.using()) {
                SubspaceConnection.shutdown();
            }
            SubspaceConnection.clearServer();
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> SubspaceConnection.newPlayer(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (SubspaceConnection.using()) {
                SubspaceConnection.send(new PlayerLogoutPacket(handler.player.getUUID()));
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (SubspaceConnection.using() && server.getTickCount() % 10 == 0) {
                SubspaceConnection.send(new DataUpdatePacket());
            }
        });
    }
}
