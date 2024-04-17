package org.nterekhin.gameP2P.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class stores player state and emit MessageLimit event
 * when messages sent by Player exceed 10
 */
public class PlayerState {
    private String nickname;
    private final AtomicInteger messageCounter;

    public PlayerState() {
        messageCounter = new AtomicInteger(1);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMessageCounter() {
        return messageCounter.get();
    }

    public int incrementMessageCounter() {
        return messageCounter.incrementAndGet();
    }
}
