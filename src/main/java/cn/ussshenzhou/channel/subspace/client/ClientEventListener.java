package cn.ussshenzhou.channel.subspace.client;

import cn.ussshenzhou.channel.network.TalkPacket2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public class ClientEventListener {
    private static long lastHeartbeat = 0;

    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> onExit());
        ClientTickEvents.START_CLIENT_TICK.register(client -> updatePlayerData());
    }

    public static void onExit() {
        SubspaceConnection.terminate();
    }

    public static void updatePlayerData() {
        if (SubspaceConnection.using()) {
            var now = Util.getMillis();
            if (now - lastHeartbeat > 1000) {
                lastHeartbeat = now;
                SubspaceConnection.send(new TalkPacket2S(new byte[0]));
            }
        }
    }
}
