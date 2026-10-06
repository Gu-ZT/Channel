package cn.ussshenzhou.channel.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * 模组网络包注册，双向注册以保持与旧版本客户端互通。
 */
public class ModPackets {

    public static void register() {
        registerBidirectional(TalkPacket2S.TYPE, TalkPacket2S.CODEC);
        registerBidirectional(AudioPacket2C.TYPE, AudioPacket2C.CODEC);
        registerBidirectional(OpMutePacket.TYPE, OpMutePacket.CODEC);
        registerBidirectional(SetBlockChannelPacket.TYPE, SetBlockChannelPacket.CODEC);
        registerBidirectional(SetItemChannelPacket.TYPE, SetItemChannelPacket.CODEC);
        registerBidirectional(SubspaceInitPacket.TYPE, SubspaceInitPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TalkPacket2S.TYPE, (packet, context) ->
                context.server().execute(() -> packet.serverHandler(context.player())));
        ServerPlayNetworking.registerGlobalReceiver(SetBlockChannelPacket.TYPE, (packet, context) ->
                context.server().execute(() -> packet.serverHandler(context.player())));
        ServerPlayNetworking.registerGlobalReceiver(SetItemChannelPacket.TYPE, (packet, context) ->
                context.server().execute(() -> packet.serverHandler(context.player())));
    }

    private static <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.serverboundPlay().register(type, codec);
        PayloadTypeRegistry.clientboundPlay().register(type, codec);
    }
}
