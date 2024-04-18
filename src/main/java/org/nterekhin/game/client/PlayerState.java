package org.nterekhin.game.client;

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

    // For tests
    protected PlayerState(String nickname, AtomicInteger messageCounter) {
        this.nickname = nickname;
        this.messageCounter = messageCounter;
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
