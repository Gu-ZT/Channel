package cn.ussshenzhou.channel;

import cn.ussshenzhou.channel.audio.client.Initializer;
import cn.ussshenzhou.channel.audio.client.receive.AudioManager;
import cn.ussshenzhou.channel.audio.client.receive.AudioReceiveHandler;
import cn.ussshenzhou.channel.audio.client.rt.DebugRayTrace;
import cn.ussshenzhou.channel.audio.client.rt.RayTraceManager;
import cn.ussshenzhou.channel.config.ChannelClientConfig;
import cn.ussshenzhou.channel.config.ChannelPlayerConfig;
import cn.ussshenzhou.channel.gui.LoginNotificationHelper;
import cn.ussshenzhou.channel.gui.OutputConfigPanel;
import cn.ussshenzhou.channel.gui.hud.MicrophoneHud;
import cn.ussshenzhou.channel.input.ModKeyMappingRegistry;
import cn.ussshenzhou.channel.network.ModPacketsClient;
import cn.ussshenzhou.channel.subspace.client.ClientEventListener;
import cn.ussshenzhou.t88.config.ConfigHelper;
import cn.ussshenzhou.t88.gui.HudManager;
import cn.ussshenzhou.t88.util.T88Config;
import net.fabricmc.api.ClientModInitializer;

/**
 * 客户端入口：仅注册客户端侧配置、按键、网络接收与事件。
 *
 * @author USS_Shenzhou
 */
public class ChannelClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigHelper.loadConfig(new ChannelClientConfig());
        ConfigHelper.loadConfig(new ChannelPlayerConfig());
        ConfigHelper.loadConfig(new T88Config());

        ModPacketsClient.register();
        ModKeyMappingRegistry.register();
        Initializer.register();
        HudManager.init();
        MicrophoneHud.register();
        LoginNotificationHelper.register();
        AudioManager.register();
        AudioReceiveHandler.register();
        RayTraceManager.register();
        DebugRayTrace.register();
        ClientEventListener.register();
        OutputConfigPanel.PlayerVolumePanel.register();
    }
}
