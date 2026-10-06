package cn.ussshenzhou.channel.audio.client;

import cn.ussshenzhou.channel.audio.client.receive.AudioManager;
import cn.ussshenzhou.channel.audio.client.send.MicManager;
import cn.ussshenzhou.channel.audio.client.send.MicReader;
import cn.ussshenzhou.channel.audio.client.send.WebRTCHelper;
import cn.ussshenzhou.channel.audio.nativ.NvidiaHelper;
import cn.ussshenzhou.channel.config.ChannelClientConfig;
import cn.ussshenzhou.channel.gui.MicConfirmScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;

/**
 * @author USS_Shenzhou
 */
@Environment(EnvType.CLIENT)
public class Initializer {
    private static boolean initialized = false;

    public static void register() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> initNative());
    }

    public static void initNative(){
        MicReader.init();
        WebRTCHelper.init();
        NvidiaHelper.init();
        AudioManager.init();
        if (!ChannelClientConfig.get().cautiousMic) {
            MicManager.init();
            initialized = true;
        }
    }

    public static void init() {
        if (!initialized) {
            if (ChannelClientConfig.get().cautiousMic) {
                Minecraft.getInstance().setScreenAndShow(new MicConfirmScreen());
            } else {
                realInit();
            }
        }
    }

    public static void realInit() {
        if (!initialized) {
            MicManager.init();
            initialized = true;
        }
    }
}
