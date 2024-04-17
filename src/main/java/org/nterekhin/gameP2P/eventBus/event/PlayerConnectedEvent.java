package org.nterekhin.gameP2P.eventBus.event;

import org.nterekhin.gameP2P.eventBus.EventType;

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
