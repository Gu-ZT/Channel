package cn.ussshenzhou.t88.gui.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 编辑器失焦事件。TEditBox 需要在移除时反注册监听，故不使用全局 Event 总线。
 *
 * @author USS_Shenzhou
 */
public class ClearEditBoxFocusEvent {
    private static final List<Consumer<ClearEditBoxFocusEvent>> LISTENERS = new CopyOnWriteArrayList<>();

    public final double mouseX;
    public final double mouseY;

    public ClearEditBoxFocusEvent(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public static void register(Consumer<ClearEditBoxFocusEvent> listener) {
        LISTENERS.add(listener);
    }

    public static void unregister(Consumer<ClearEditBoxFocusEvent> listener) {
        LISTENERS.remove(listener);
    }

    public void post() {
        LISTENERS.forEach(listener -> listener.accept(this));
    }
}
