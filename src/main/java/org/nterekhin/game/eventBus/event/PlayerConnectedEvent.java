package org.nterekhin.game.eventBus.event;

import org.nterekhin.game.eventBus.EventType;

/**
 * Event will happen when user connected to Server and chosen nickname
 */
public class PlayerConnectedEvent extends Event {

    private final String nickname;
    private final String remoteAddress;

    public PlayerConnectedEvent(String nickname, String remoteAddress) {
        super(EventType.PLAYER_CONNECTED);
        this.nickname = nickname;
        this.remoteAddress = remoteAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }
}
