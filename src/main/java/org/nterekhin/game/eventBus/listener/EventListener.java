package org.nterekhin.game.eventBus.listener;

import org.nterekhin.game.eventBus.event.Event;

public interface EventListener<T extends Event> {
    void onEvent(Event event);
}
