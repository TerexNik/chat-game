package org.nterekhin.gameP2P.eventBus.listener;

import org.nterekhin.gameP2P.client.PlayerManager;
import org.nterekhin.gameP2P.eventBus.event.Event;
import org.nterekhin.gameP2P.eventBus.event.PlayerConnectedEvent;
import org.nterekhin.gameP2P.eventBus.event.PlayerDisconnectedEvent;

public class PlayerDisconnectedListener implements EventListener<PlayerConnectedEvent> {

    public PlayerDisconnectedListener() {
    }

    @Override
    public void onEvent(Event event) {
        PlayerDisconnectedEvent disconnectedEvent = (PlayerDisconnectedEvent) event;
        PlayerManager.getInstance().broadcastServerMessage(
                String.format("\"%s\" disconnected from the chat", disconnectedEvent.getNickname())
        );
        PlayerManager.getInstance().disconnectByNickname(disconnectedEvent.getNickname());
        System.out.printf("Client with nickname: %s is disconnected from %s%n",
                disconnectedEvent.getNickname(),
                disconnectedEvent.getRemoteAddress());
    }
}
