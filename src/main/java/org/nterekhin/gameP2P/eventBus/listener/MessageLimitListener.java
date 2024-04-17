package org.nterekhin.gameP2P.eventBus.listener;

import org.nterekhin.gameP2P.client.PlayerManager;
import org.nterekhin.gameP2P.client.PlayerUIManager;
import org.nterekhin.gameP2P.eventBus.event.Event;
import org.nterekhin.gameP2P.eventBus.event.MessagesLimitEvent;
import org.nterekhin.gameP2P.server.ServerManager;

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
