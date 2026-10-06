package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.util.ModConstant;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 模组网络包注册，双向注册以保持与旧版本客户端互通。
 */
@EventBusSubscriber
public class ModPackets {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ModConstant.SHORT_ID);
        registrar.playBidirectional(TalkPacket2S.TYPE, TalkPacket2S.CODEC,
                (packet, context) -> packet.serverHandler(context), (packet, context) -> {
                });
        registrar.playBidirectional(AudioPacket2C.TYPE, AudioPacket2C.CODEC,
                (packet, context) -> {
                }, (packet, context) -> packet.clientHandler(context));
        registrar.playBidirectional(OpMutePacket.TYPE, OpMutePacket.CODEC,
                (packet, context) -> {
                }, (packet, context) -> packet.clientHandler(context));
        registrar.playBidirectional(SetBlockChannelPacket.TYPE, SetBlockChannelPacket.CODEC,
                (packet, context) -> packet.serverHandler(context), (packet, context) -> {
                });
        registrar.playBidirectional(SetItemChannelPacket.TYPE, SetItemChannelPacket.CODEC,
                (packet, context) -> packet.serverHandler(context), (packet, context) -> {
                });
        registrar.playBidirectional(SubspaceInitPacket.TYPE, SubspaceInitPacket.CODEC,
                (packet, context) -> {
                }, (packet, context) -> packet.clientHandler(context));
    }
}
