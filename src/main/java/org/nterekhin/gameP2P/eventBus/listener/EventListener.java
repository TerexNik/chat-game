package org.nterekhin.gameP2P.eventBus.listener;

import org.nterekhin.gameP2P.eventBus.event.Event;

public interface EventListener<T extends Event> {
    void onEvent(Event event);
}
