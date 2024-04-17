package org.nterekhin.game.eventBus.listener;

import org.nterekhin.game.client.PlayerManager;
import org.nterekhin.game.eventBus.event.Event;
import org.nterekhin.game.eventBus.event.PlayerConnectedEvent;

/**
 * Listener for PlayerConnectedEvent event
 * Will send Server message "User connected" and log to console
 * <p>
 * May be good idea to make 1 event connect/disconnect
 * but this will be easier to add functional to when needed
 */
public class PlayerConnectedListener implements EventListener<PlayerConnectedEvent> {

    public PlayerConnectedListener() {
    }

    @Override
    public void onEvent(Event event) {
        PlayerConnectedEvent connectedEvent = (PlayerConnectedEvent) event;
        PlayerManager.getInstance().broadcastServerMessage(
                String.format("\"%s\" connected to chat", connectedEvent.getNickname())
        );
        System.out.printf("Client with nickname: %s is connected to %s%n",
                connectedEvent.getNickname(),
                connectedEvent.getRemoteAddress());
    }
}
