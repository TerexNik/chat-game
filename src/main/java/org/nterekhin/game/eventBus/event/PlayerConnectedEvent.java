package org.nterekhin.game.eventBus.event;

import org.nterekhin.game.eventBus.EventType;

import java.net.Socket;

/**
 * Event will happen when user connected to Server and chosen nickname
 */
public class PlayerConnectedEvent extends Event {

    private final Socket socket;

    public PlayerConnectedEvent(Socket socket) {
        super(EventType.PLAYER_CONNECTED);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
