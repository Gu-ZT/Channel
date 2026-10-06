package cn.ussshenzhou.t88.gui.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * @author USS_Shenzhou
 */
public class ResizeHudEvent {
    public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, listeners -> event -> {
        for (Listener listener : listeners) {
            listener.onResize(event);
        }
    });

    public interface Listener {
        void onResize(ResizeHudEvent event);
    }
}
