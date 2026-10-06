package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.audio.client.receive.AudioReceiveHandler;
import cn.ussshenzhou.channel.util.ModConstant;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author USS_Shenzhou
 */
public class AudioPacket2C implements CustomPacketPayload {
    public static final Type<AudioPacket2C> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstant.SHORT_ID, "au"));
    public static final StreamCodec<FriendlyByteBuf, AudioPacket2C> CODEC = StreamCodec.ofMember(AudioPacket2C::encode, AudioPacket2C::new);

    public final UUID from;
    public final byte[] opus;
    public final int[] channels;

    public AudioPacket2C(UUID from, byte[] opus, @Nullable IntArraySet channels) {
        this.from = from;
        this.opus = opus;
        if (channels == null) {
            this.channels = new int[0];
        } else {
            this.channels = channels.toIntArray();
        }
    }

    public AudioPacket2C(FriendlyByteBuf buf) {
        this.from = buf.readUUID();
        this.opus = buf.readByteArray();
        int channelCount = buf.readVarInt();
        this.channels = new int[channelCount];
        for (int i = 0; i < channelCount; i++) {
            this.channels[i] = buf.readVarInt();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.from);
        buf.writeByteArray(this.opus);
        buf.writeVarInt(this.channels.length);
        for (int channel : this.channels) {
            buf.writeVarInt(channel);
        }
    }

    public void clientHandler(IPayloadContext context) {
        AudioReceiveHandler.handle(this);
    }
}
