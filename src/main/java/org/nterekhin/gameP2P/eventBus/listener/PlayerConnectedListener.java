package org.nterekhin.gameP2P.eventBus.listener;

import org.nterekhin.gameP2P.client.PlayerManager;
import org.nterekhin.gameP2P.eventBus.event.Event;
import org.nterekhin.gameP2P.eventBus.event.PlayerConnectedEvent;

public class PlayerConnectedListener implements EventListener<PlayerConnectedEvent> {

    public PlayerConnectedListener() {
    }

    @Override
    public void onEvent(Event event) {
        PlayerConnectedEvent connectedEvent = (PlayerConnectedEvent) event;
        PlayerManager.getInstance().broadcastServerMessage(
                connectedEvent.getNickname() + " connected to chat"
        );
        System.out.printf("Client with nickname: %s is connected to %s%n",
                connectedEvent.getNickname(),
                connectedEvent.getRemoteAddress());
    }
}
