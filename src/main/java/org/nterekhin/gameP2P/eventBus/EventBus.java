package org.nterekhin.gameP2P.eventBus;

import org.nterekhin.gameP2P.eventBus.event.Event;
import org.nterekhin.gameP2P.eventBus.listener.EventListener;
import org.nterekhin.gameP2P.eventBus.listener.MessageLimitListener;
import org.nterekhin.gameP2P.eventBus.listener.PlayerConnectedListener;
import org.nterekhin.gameP2P.eventBus.listener.PlayerDisconnectedListener;

import java.util.HashMap;
import java.util.Map;

public final class EventBus {
    private final Map<EventType, EventListener<? extends Event>> listenersMap = new HashMap<>();
    private static final EventBus eventBus = new EventBus();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return eventBus;
    }

    public void registerListener(EventType eventType, EventListener<? extends Event> listener) {
        listenersMap.computeIfAbsent(eventType, k -> listener);
    }

    public void unregisterListener(EventType eventType) {
        listenersMap.remove(eventType);
    }

    public void postEvent(Event event) {
        EventListener<? extends Event> listener = listenersMap.get(event.getEventType());
        if (listener != null) {
            listener.onEvent(event);
        }
    }

    public void setUpEventBusForServer() {
        eventBus.registerListener(
                EventType.MESSAGES_LIMIT,
                new MessageLimitListener()
        );
        eventBus.registerListener(
                EventType.PLAYER_CONNECTED,
                new PlayerConnectedListener()
        );
        eventBus.registerListener(
                EventType.PLAYER_DISCONNECTED,
                new PlayerDisconnectedListener()
        );
    }

    public void clearUpEventBusFromServerListeners() {
        unregisterListener(EventType.MESSAGES_LIMIT);
        unregisterListener(EventType.PLAYER_CONNECTED);
        unregisterListener(EventType.PLAYER_DISCONNECTED);
    }
}