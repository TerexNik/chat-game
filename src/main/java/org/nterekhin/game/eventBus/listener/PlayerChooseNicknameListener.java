package org.nterekhin.game.eventBus.listener;

import org.nterekhin.game.client.PlayerManager;
import org.nterekhin.game.eventBus.event.Event;
import org.nterekhin.game.eventBus.event.PlayerChooseNicknameEvent;

/**
 * Listener for PlayerChooseNickname event
 * Will send Server message "%Nickname% connected" and log to console
 * <p>
 * May be good idea to make 1 event connect/disconnect
 * but this will be easier to add functional to when needed
 */
public class PlayerChooseNicknameListener implements EventListener<PlayerChooseNicknameEvent> {
    @Override
    public void onEvent(Event event) {
        PlayerChooseNicknameEvent connectedEvent = (PlayerChooseNicknameEvent) event;
        PlayerManager.getInstance().broadcastServerMessage(
                String.format("\"%s\" connected to chat", connectedEvent.getNickname())
        );
        System.out.printf("Client with nickname: %s is connected to %s%n",
                connectedEvent.getNickname(),
                connectedEvent.getRemoteAddress());
    }
}
