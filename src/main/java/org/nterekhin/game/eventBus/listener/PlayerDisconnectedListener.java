package org.nterekhin.game.eventBus.listener;

import org.nterekhin.game.client.PlayerManager;
import org.nterekhin.game.eventBus.event.Event;
import org.nterekhin.game.eventBus.event.PlayerChooseNicknameEvent;
import org.nterekhin.game.eventBus.event.PlayerDisconnectedEvent;

/**
 * Listener for PlayerDisconnectedEvent event
 * Will send Server message "User disconnected" and log to console
 * <p>
 * May be good idea to make 1 event connect/disconnect
 * but this will be easier to add functional to when needed
 */
public class PlayerDisconnectedListener implements EventListener<PlayerChooseNicknameEvent> {

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
