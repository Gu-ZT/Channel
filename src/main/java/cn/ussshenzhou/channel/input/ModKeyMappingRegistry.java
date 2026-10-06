package cn.ussshenzhou.channel.input;

import cn.ussshenzhou.channel.Channel;
import cn.ussshenzhou.channel.audio.Trigger;
import cn.ussshenzhou.channel.audio.client.Initializer;
import cn.ussshenzhou.channel.config.ChannelClientConfig;
import cn.ussshenzhou.channel.gui.ConfigScreen;
import cn.ussshenzhou.t88.gui.TestScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

/**
 * @author USS_Shenzhou
 */
@Environment(EnvType.CLIENT)
public class ModKeyMappingRegistry {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Channel.MODID, "channel"));
    public static final KeyMapping CONFIG = new KeyMapping(
            "key.channel.config_screen",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY
    );
    public static final KeyMapping PTT = new KeyMapping(
            "key.channel.ptt",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, CATEGORY
    );
    /**
     * 仅开发环境注册，用于打开 T88 TestScreen 检查布局。
     */
    public static KeyMapping testScreen;
    private static long lastSwitch = 0;

    public static void register() {
        KeyMappingHelper.registerKeyMapping(CONFIG);
        KeyMappingHelper.registerKeyMapping(PTT);
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            testScreen = new KeyMapping("key.channel.test_screen", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F8, CATEGORY);
            KeyMappingHelper.registerKeyMapping(testScreen);
        }
        ClientTickEvents.START_CLIENT_TICK.register(ModKeyMappingRegistry::onClientTick);
        // 界面打开时键位点击不计数，Alt+配置键需在界面键盘事件里单独判定
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenKeyboardEvents.beforeKeyPress(screen).register((scr, event) -> {
                    if (isConfigKey(event)) {
                        client.setScreenAndShow(new ConfigScreen());
                    }
                }));
    }

    private static boolean isConfigKey(KeyEvent event) {
        return CONFIG.key.getType() == InputConstants.Type.KEYSYM
                && CONFIG.key.getValue() == event.key()
                && event.hasAltDown();
    }

    private static void onClientTick(Minecraft minecraft) {
        if (CONFIG.consumeClick() && altDown()) {
            minecraft.setScreenAndShow(new ConfigScreen());
        }
        if (testScreen != null && testScreen.consumeClick()) {
            minecraft.setScreenAndShow(new TestScreen());
        }
        switchMute();
        pushTalk();
    }

    private static boolean altDown() {
        long window = Minecraft.getInstance().getWindow().handle();
        return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;
    }

    private static void switchMute() {
        var screen = Minecraft.getInstance().gui.screen();
        if (screen != null) {
            return;
        }
        if (ChannelClientConfig.get().trigger != Trigger.SWITCH) {
            return;
        }
        if (PTT.consumeClick()) {
            if (Util.getMillis() - lastSwitch >= 100) {
                if (!ChannelClientConfig.get().onAir) {
                    Initializer.init();
                }
                ChannelClientConfig.write(c -> c.onAir = !c.onAir);
                lastSwitch = Util.getMillis();
            }
        }
    }

    private static void pushTalk() {
        var cfg = ChannelClientConfig.get();
        if (cfg.trigger != Trigger.PUSH_TO_TALK) {
            return;
        }
        boolean toWrite = PTT.isDown();
        if (toWrite) {
            Initializer.init();
        }
        if (toWrite != cfg.onAir) {
            ChannelClientConfig.write(c -> c.onAir = toWrite);
        }
    }
}
