package org.nterekhin.gameP2P.eventBus.event;

import org.nterekhin.gameP2P.eventBus.EventType;

public class MessagesLimitEvent extends Event {

    public MessagesLimitEvent() {
        super(EventType.MESSAGES_LIMIT);
    }
}
