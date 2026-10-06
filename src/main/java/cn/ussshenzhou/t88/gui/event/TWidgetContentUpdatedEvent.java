package cn.ussshenzhou.t88.gui.event;

import cn.ussshenzhou.t88.gui.widegt.TWidget;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * @author Tony Yu
 */
public class TWidgetContentUpdatedEvent {
    public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, listeners -> event -> {
        for (Listener listener : listeners) {
            listener.onContentUpdated(event);
        }
    });

    private final TWidget updated;

    public TWidgetContentUpdatedEvent(TWidget updated) {
        this.updated = updated;
    }

    public TWidget getUpdated() {
        return updated;
    }

    public interface Listener {
        void onContentUpdated(TWidgetContentUpdatedEvent event);
    }
}
