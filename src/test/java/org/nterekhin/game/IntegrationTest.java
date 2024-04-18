package org.nterekhin.game;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.EventType;
import org.nterekhin.game.server.ServerManager;

public abstract class IntegrationTest {
    public static final int port = 34242;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ServerManager.getInstance().startServer(port);
        EventBus.getInstance().unregisterListener(EventType.PLAYER_CONNECTED);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ServerManager.getInstance().shutdownServer();
    }

}
