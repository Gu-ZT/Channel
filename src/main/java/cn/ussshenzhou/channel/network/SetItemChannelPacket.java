package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.Item.ModItems;
import cn.ussshenzhou.channel.util.ModConstant;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author USS_Shenzhou
 */
public class SetItemChannelPacket implements CustomPacketPayload {
    public static final Type<SetItemChannelPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstant.SHORT_ID, "setitemchannelpacket"));
    public static final StreamCodec<FriendlyByteBuf, SetItemChannelPacket> CODEC = StreamCodec.ofMember(SetItemChannelPacket::encode, SetItemChannelPacket::new);

    public final InteractionHand hand;
    public final int channel;

    public SetItemChannelPacket(InteractionHand hand, int channel) {
        this.hand = hand;
        this.channel = channel;
    }

    public SetItemChannelPacket(FriendlyByteBuf buf) {
        this.hand = buf.readEnum(InteractionHand.class);
        this.channel = buf.readVarInt();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(hand);
        buf.writeVarInt(channel);
    }

    public void serverHandler(IPayloadContext context) {
        var item = context.player().getItemInHand(hand);
        if (item.has(ModItems.CHANNEL.get())) {
            item.set(ModItems.CHANNEL.get(), channel);
        }
    }
}
