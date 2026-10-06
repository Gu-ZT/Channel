package cn.ussshenzhou.channel.gui;

import cn.ussshenzhou.channel.audio.client.Initializer;
import cn.ussshenzhou.channel.config.ChannelClientConfig;
import cn.ussshenzhou.channel.input.ModKeyMappingRegistry;
import cn.ussshenzhou.channel.util.CompatHelper;
import cn.ussshenzhou.t88.gui.notification.TSimpleNotification;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.concurrent.locks.LockSupport;

/**
 * @author USS_Shenzhou
 */
@Environment(EnvType.CLIENT)
public class LoginNotificationHelper {

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> showNotification());
    }

    public static void showNotification() {
        if (!CompatHelper.isClientLevelValid()) {
            return;
        }
        TSimpleNotification.fire(
                Component.translatable("channel.welcome",
                        Component.translatable("key.keyboard.left.alt").getString() + " + " + ModKeyMappingRegistry.CONFIG.getTranslatedKeyMessage().getString(),
                        ModKeyMappingRegistry.PTT.getTranslatedKeyMessage().getString()
                ),
                12,
                TSimpleNotification.Severity.TIP
        );
        if (ChannelClientConfig.get().onAir) {
            Thread.startVirtualThread(() -> {
                while (Minecraft.getInstance().gui.screen() != null) {
                    LockSupport.parkNanos(1000_000_000);
                }
                Minecraft.getInstance().execute(Initializer::init);
            });
        }
    }
}
