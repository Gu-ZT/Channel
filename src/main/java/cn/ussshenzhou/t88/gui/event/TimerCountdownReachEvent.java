package cn.ussshenzhou.t88.gui.event;

import cn.ussshenzhou.t88.gui.widegt.TTimer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * @author USS_Shenzhou
 */
public class TimerCountdownReachEvent {
    public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, listeners -> event -> {
        for (Listener listener : listeners) {
            listener.onCountdownReach(event);
        }
    });

    private final TTimer reachedTimer;

    public TimerCountdownReachEvent(TTimer reachedTimer) {
        this.reachedTimer = reachedTimer;
    }

    public TTimer getReachedTimer() {
        return reachedTimer;
    }

    public interface Listener {
        void onCountdownReach(TimerCountdownReachEvent event);
    }
}
