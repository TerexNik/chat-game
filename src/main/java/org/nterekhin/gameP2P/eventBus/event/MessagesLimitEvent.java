package org.nterekhin.gameP2P.eventBus.event;

import org.nterekhin.gameP2P.eventBus.EventType;

/**
 * Event that will happen when message limit is reached or exceeded
 */
public class MessagesLimitEvent extends Event {
    public MessagesLimitEvent() {
        super(EventType.MESSAGES_LIMIT);
    }
}
