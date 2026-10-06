package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.audio.DebugManager;
import cn.ussshenzhou.channel.audio.server.RelayHandler;
import cn.ussshenzhou.channel.subspace.SubspacePacket;
import cn.ussshenzhou.channel.util.ModConstant;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author USS_Shenzhou
 */
public class TalkPacket2S extends SubspacePacket implements CustomPacketPayload {
    public static final Type<TalkPacket2S> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstant.SHORT_ID, "tsp"));
    public static final StreamCodec<FriendlyByteBuf, TalkPacket2S> CODEC = StreamCodec.ofMember(TalkPacket2S::encode, TalkPacket2S::new);

    //empty array as heartbeat
    private final byte[] opus;

    public TalkPacket2S(byte[] opus) {
        this.opus = opus;
    }

    public TalkPacket2S(FriendlyByteBuf buf) {
        this.opus = buf.readByteArray();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(this.opus);
        if (opus.length > 0) {
            DebugManager.MIC_SEND_COUNTER.update();
            DebugManager.sending(opus);
        }
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException();
    }

    public void serverHandler(IPayloadContext context) {
        RelayHandler.process((ServerPlayer) context.player(), opus);
    }
}
