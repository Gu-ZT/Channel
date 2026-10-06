package cn.ussshenzhou.channel.network;

import cn.ussshenzhou.channel.gui.hud.MicrophoneHud;
import cn.ussshenzhou.channel.util.ModConstant;
import cn.ussshenzhou.t88.gui.notification.TSimpleNotification;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.client.Minecraft;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.network.chat.Component;

/**
 * @author USS_Shenzhou
 */
public class OpMutePacket implements CustomPacketPayload {
    public static final Type<OpMutePacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModConstant.SHORT_ID, "opmutepacket"));
    public static final StreamCodec<FriendlyByteBuf, OpMutePacket> CODEC = StreamCodec.ofMember(OpMutePacket::encode, OpMutePacket::new);

    public final boolean mute;

    public OpMutePacket(boolean mute) {
        this.mute = mute;
    }

    public OpMutePacket(FriendlyByteBuf buf) {
        mute = buf.readBoolean();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(mute);
    }

    public void clientHandler() {
        var player = Minecraft.getInstance().player;
        if (mute) {
            if (player == null) {
                LogUtils.getLogger().debug("收到禁言包时本地玩家为空，跳过权限检查与提示。");
            } else if (!player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
                MicrophoneHud.setStatus(MicrophoneHud.Status.OP);
                TSimpleNotification.fire(Component.translatable("notice.channel.mute_true"), 10, TSimpleNotification.Severity.WARN);
            }
        }
        if (!mute) {
            MicrophoneHud.resumeStatus();
            TSimpleNotification.fire(Component.translatable("notice.channel.mute_false"), 10, TSimpleNotification.Severity.TIP);
        }
    }
}
