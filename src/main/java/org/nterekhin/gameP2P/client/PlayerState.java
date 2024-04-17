package org.nterekhin.gameP2P.client;

import org.nterekhin.gameP2P.eventBus.EventBus;
import org.nterekhin.gameP2P.eventBus.event.MessagesLimitEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerState {
    private String nickname;
    private final AtomicInteger messageCounter;

    public PlayerState() {
        messageCounter = new AtomicInteger(0);
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

    public void incrementMessageCounter() {
        if (messageCounter.incrementAndGet() >= 5) {
            EventBus.getInstance().postEvent(new MessagesLimitEvent());
        }
    }
}
