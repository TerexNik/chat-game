package org.nterekhin.gameP2P.eventBus.event;

import org.nterekhin.gameP2P.eventBus.EventType;

/**
 * Event will happen when user disconnected from server
 */
public class PlayerDisconnectedEvent extends Event {
    private final String nickname;
    private final String remoteAddress;

    public PlayerDisconnectedEvent(String nickname, String remoteAddress) {
        super(EventType.PLAYER_DISCONNECTED);
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
