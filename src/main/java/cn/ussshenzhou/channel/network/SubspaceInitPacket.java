package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.subspace.client.SubspaceConnection;
import cn.ussshenzhou.channel.util.ModConstant;
import cn.ussshenzhou.channel.util.Protocol;
import cn.ussshenzhou.channel.util.SecurityLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author USS_Shenzhou
 */
public class SubspaceInitPacket implements CustomPacketPayload {
    public static final Type<SubspaceInitPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstant.SHORT_ID, "subspaceinitpacket"));
    public static final StreamCodec<FriendlyByteBuf, SubspaceInitPacket> CODEC = StreamCodec.ofMember(SubspaceInitPacket::encode, SubspaceInitPacket::new);

    public final byte[] subspaceToken;
    public final Protocol protocol;
    public final String subspaceAddress;
    public final int subspacePort;
    public final SecurityLevel securityLevel;

    public SubspaceInitPacket(byte[] subspaceToken, Protocol protocol, String subspaceAddress, int subspacePort, SecurityLevel securityLevel) {
        this.subspaceToken = subspaceToken;
        this.protocol = protocol;
        this.subspaceAddress = subspaceAddress;
        this.subspacePort = subspacePort;
        this.securityLevel = securityLevel;
    }

    public SubspaceInitPacket(FriendlyByteBuf buf) {
        this.subspaceToken = buf.readByteArray();
        this.protocol = buf.readEnum(Protocol.class);
        this.subspaceAddress = buf.readUtf();
        this.subspacePort = buf.readVarInt();
        this.securityLevel = buf.readEnum(SecurityLevel.class);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(this.subspaceToken);
        buf.writeEnum(this.protocol);
        buf.writeUtf(this.subspaceAddress);
        buf.writeVarInt(this.subspacePort);
        buf.writeEnum(this.securityLevel);
    }

    public void clientHandler(IPayloadContext context) {
        SubspaceConnection.connect(this);
    }
}
