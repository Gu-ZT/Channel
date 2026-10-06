package cn.ussshenzhou.channel.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * 客户端侧网络包接收注册。除语音包外，处理逻辑需切回客户端主线程执行。
 */
@Environment(EnvType.CLIENT)
public class ModPacketsClient {

    public static void register() {
        // 解码与分发按 netty 线程编写（内部已同步），不做线程切换
        ClientPlayNetworking.registerGlobalReceiver(AudioPacket2C.TYPE, (packet, context) -> packet.clientHandler());
        ClientPlayNetworking.registerGlobalReceiver(OpMutePacket.TYPE, (packet, context) ->
                context.client().execute(packet::clientHandler));
        ClientPlayNetworking.registerGlobalReceiver(SubspaceInitPacket.TYPE, (packet, context) ->
                context.client().execute(packet::clientHandler));
    }
}
