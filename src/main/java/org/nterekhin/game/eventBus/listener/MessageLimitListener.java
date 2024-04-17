package org.nterekhin.game.eventBus.listener;

import org.nterekhin.game.client.PlayerManager;
import org.nterekhin.game.eventBus.event.Event;
import org.nterekhin.game.eventBus.event.MessagesLimitEvent;
import org.nterekhin.game.server.ServerManager;

/**
 * Listener for MessageLimit event
 * Will shut down server gracefully when event happens
 */
public class MessageLimitListener implements EventListener<MessagesLimitEvent> {

    public MessageLimitListener() {
    }

    @Override
    public void onEvent(Event event) {
        PlayerManager.getInstance().broadcastServerMessage(
                "Message limit reached, server is shutting down"
        );
        ServerManager.getInstance().shutdownApplication();
    }
}
