package org.nterekhin.game.eventBus.listener;


import org.nterekhin.game.client.PlayerHandler;
import org.nterekhin.game.client.PlayerManager;
import org.nterekhin.game.client.PlayerState;
import org.nterekhin.game.eventBus.event.Event;
import org.nterekhin.game.eventBus.event.PlayerConnectedEvent;
import org.nterekhin.game.util.IOFunction;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Listener for PlayerConnectedEvent event
 * Will create PlayerHandler for user and ask him for nickname
 */
public class PlayerConnectedListener implements EventListener<PlayerConnectedEvent> {
    private final ExecutorService pool;
    private final PlayerManager playerManager;

    public PlayerConnectedListener() {
        playerManager = PlayerManager.getInstance();
        pool = Executors.newCachedThreadPool();
    }

    @Override
    public void onEvent(Event event) {
        System.out.println("eventBus: new player connected to the server");
        PlayerConnectedEvent connectedEvent = (PlayerConnectedEvent) event;
        IOFunction.executeWithLogOnIOException(() -> {
            PlayerHandler handler = new PlayerHandler(connectedEvent.getSocket(), new PlayerState());
            pool.submit(() -> {
                try {
                    handler.init();
                    playerManager.createPlayerHandler(handler);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.close();
                }
            });
        });
    }

    public void shutdown() {
        pool.shutdown();
    }
}
