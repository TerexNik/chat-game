package org.nterekhin.game.eventBus;

import org.nterekhin.game.eventBus.event.Event;
import org.nterekhin.game.eventBus.listener.EventListener;
import org.nterekhin.game.eventBus.listener.MessageLimitListener;
import org.nterekhin.game.eventBus.listener.PlayerChooseNicknameListener;
import org.nterekhin.game.eventBus.listener.PlayerConnectedListener;
import org.nterekhin.game.eventBus.listener.PlayerDisconnectedListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Event bus for project
 * Helps with regular actions such as connection/disconnection
 * <p>
 * Works on Publisher/Subscriber pattern
 */
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
                EventType.MESSAGES_LIMIT_EXCEEDED,
                new MessageLimitListener()
        );
        eventBus.registerListener(
                EventType.PLAYER_CONNECTED,
                new PlayerConnectedListener()
        );
        eventBus.registerListener(
                EventType.PLAYER_CHOOSE_NICKNAME,
                new PlayerChooseNicknameListener()
        );
        eventBus.registerListener(
                EventType.PLAYER_DISCONNECTED,
                new PlayerDisconnectedListener()
        );
    }

    public void clearUpEventBusFromServerListeners() {
        unregisterListener(EventType.MESSAGES_LIMIT_EXCEEDED);
        unregisterListener(EventType.PLAYER_CONNECTED);
        unregisterListener(EventType.PLAYER_DISCONNECTED);
    }
}