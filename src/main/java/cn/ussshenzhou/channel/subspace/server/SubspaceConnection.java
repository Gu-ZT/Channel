package cn.ussshenzhou.channel.subspace.server;

import cn.ussshenzhou.channel.config.ChannelServerConfig;
import cn.ussshenzhou.channel.network.SubspaceInitPacket;
import cn.ussshenzhou.channel.subspace.AesGcmEncoder;
import cn.ussshenzhou.channel.subspace.SubspacePacket;
import cn.ussshenzhou.channel.subspace.server.send.DataUpdatePacket;
import cn.ussshenzhou.channel.subspace.server.send.InitPacket;
import cn.ussshenzhou.channel.subspace.server.send.PlayerLoginPacket;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.Varint21FrameDecoder;
import net.minecraft.network.Varint21LengthFieldPrepender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author USS_Shenzhou
 */
public class SubspaceConnection {
    private static EventLoopGroup eventLoopGroup = new MultiThreadIoEventLoopGroup(1, new DefaultThreadFactory("Channel-Server-Subspace", true), NioIoHandler.newFactory());
    private static volatile Channel channel;
    private static volatile boolean activelyDisconnect;
    private static volatile @Nullable MinecraftServer server;

    /**
     * 连接子空间服务器并记录当前服务端实例，供无上下文场景（如 UDP 编码时）取用。
     */
    public static void connect(MinecraftServer minecraftServer) {
        server = minecraftServer;
        connect();
    }

    public static @Nullable MinecraftServer getServer() {
        return server;
    }

    public static void clearServer() {
        server = null;
    }

    /**
     * <pre>
     * ┌---┬-------┬----┬---------┬---┐
     * │ H │ Nonce │ Id | Content │TAG│
     * └---┴-------┴----┴---------┴---┘
     *             └----Packet----┘
     *             └------AES-GCM-----┘
     *
     * H       = header, varint, packet length
     * Nonce   = Nonce, varint, for AES-GCM
     * Id      = packet id, varint, by {@link SubspacePacket#getId()}
     * Content = packet payload, by {@link SubspacePacket#encode(FriendlyByteBuf)}
     * TAG     = tag, byte[16], for AES-GCM
     * </pre>
     */
    public static void connect() {
        var cfg = ChannelServerConfig.get();
        new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new Varint21FrameDecoder(null),

                                new Varint21LengthFieldPrepender(),
                                new AesGcmEncoder(cfg.getSubspaceFrequency())
                        );
                    }
                })
                .connect(cfg.subspaceAddress, cfg.subspaceServerPort)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        channel = future.channel();
                        channel.closeFuture().addListener((ChannelFutureListener) f -> {
                            if (activelyDisconnect) {
                                LogUtils.getLogger().info("Disconnected from subspace.");
                                activelyDisconnect = false;
                            } else {
                                LogUtils.getLogger().warn("Disconnected from subspace. Reconnecting in 30s...");
                                eventLoopGroup.schedule((Runnable) SubspaceConnection::connect, 30, TimeUnit.SECONDS);
                            }
                            channel = null;
                        });
                        send(new InitPacket());
                        LogUtils.getLogger().info("Subspace connected. Sending InitPacket...");
                        if (server != null) {
                            server.getPlayerList().getPlayers().forEach(SubspaceConnection::newPlayer);
                        }
                    } else {
                        LogUtils.getLogger().error("Failed to connect to subspace server. Try again in 30s...");
                        eventLoopGroup.schedule((Runnable) SubspaceConnection::connect, 30, TimeUnit.SECONDS);
                        channel = null;
                    }
                });
    }

    public static void send(SubspacePacket packet) {
        if (channel == null) {
            LogUtils.getLogger().warn("channel is null. This should not happen. Skip send {}.", packet.getClass().getSimpleName());
            return;
        }
        if (!channel.isActive()) {
            LogUtils.getLogger().warn("channel is inactive. This should not happen. Skip send {}.", packet.getClass().getSimpleName());
            return;
        }
        var buf = new FriendlyByteBuf(channel.alloc().buffer());
        buf.writeVarInt(packet.getId());
        packet.encode(buf);
        channel.writeAndFlush(buf)
                .addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        LogUtils.getLogger().error("Failed to send {}.", packet.getClass().getSimpleName(), future.cause());
                    }
                });
    }

    public static void shutdown() {
        activelyDisconnect = true;
        if (channel != null) {
            channel.close();
        }
    }

    public static void newPlayer(Player player) {
        if (!using()) {
            return;
        }
        byte[] token = new SecureRandom().generateSeed(32);
        SubspaceConnection.send(new PlayerLoginPacket(token, player.getUUID(), player.getId()));
        SubspaceConnection.send(new DataUpdatePacket());
        var cfg = ChannelServerConfig.get();
        eventLoopGroup.schedule(() -> ServerPlayNetworking.send((ServerPlayer) player, new SubspaceInitPacket(token, cfg.subspaceProtocol, cfg.subspaceAddress, cfg.subspaceClientPort, cfg.subspaceSecurityLevel)), 3, TimeUnit.SECONDS);
    }

    public static boolean using() {
        return channel != null && channel.isActive();
    }

    public static void reset() {
        LogUtils.getLogger().info("Resetting subspace connection...");
        shutdown();
        eventLoopGroup.shutdownGracefully();
        eventLoopGroup = new MultiThreadIoEventLoopGroup(1, new DefaultThreadFactory("Channel-Server-Subspace", true), NioIoHandler.newFactory());
        connect();
    }
}
