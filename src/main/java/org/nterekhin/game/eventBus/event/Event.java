package org.nterekhin.game.eventBus.event;

import org.nterekhin.game.eventBus.EventType;

/**
 * Base for events
 */
public abstract class Event {
    public EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
