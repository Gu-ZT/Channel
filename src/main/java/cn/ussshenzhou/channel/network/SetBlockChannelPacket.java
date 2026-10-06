package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.blockentity.ChanneledBlockEntity;
import cn.ussshenzhou.channel.util.ModConstant;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author USS_Shenzhou
 */
public class SetBlockChannelPacket implements CustomPacketPayload {
    public static final Type<SetBlockChannelPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstant.SHORT_ID, "setblockchannelpacket"));
    public static final StreamCodec<FriendlyByteBuf, SetBlockChannelPacket> CODEC = StreamCodec.ofMember(SetBlockChannelPacket::encode, SetBlockChannelPacket::new);

    public final BlockPos pos;
    public final int channel;

    public SetBlockChannelPacket(BlockPos pos, int channel) {
        this.pos = pos;
        this.channel = channel;
    }

    public SetBlockChannelPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.channel = buf.readVarInt();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeVarInt(channel);
    }

    public void serverHandler(IPayloadContext context) {
        var level = context.player().level();
        if (level.isLoaded(pos) && level.getBlockEntity(pos) instanceof ChanneledBlockEntity channeledBlockEntity) {
            channeledBlockEntity.setChannel(channel);
        }
    }
}
