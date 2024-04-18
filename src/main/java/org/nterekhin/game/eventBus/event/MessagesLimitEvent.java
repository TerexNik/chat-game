package org.nterekhin.game.eventBus.event;

import org.nterekhin.game.eventBus.EventType;

/**
 * Event that will happen when message limit is reached or exceeded
 */
public class MessagesLimitEvent extends Event {
    public MessagesLimitEvent() {
        super(EventType.MESSAGES_LIMIT_EXCEEDED);
    }
}
