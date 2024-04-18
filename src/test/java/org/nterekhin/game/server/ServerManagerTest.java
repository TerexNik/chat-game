package org.nterekhin.game.server;

import org.junit.Assert;
import org.junit.Test;
import org.nterekhin.game.IntegrationTest;

public class ServerManagerTest {
    private final ServerManager manager = ServerManager.getInstance();

    @Test
    public void shouldStartAndShutdownServer() throws Exception {
        if (manager.isServerRunning()) {
            manager.shutdownServer();
        } else {
            manager.startServer(IntegrationTest.port);
            Assert.assertTrue(manager.isServerRunning());
            manager.shutdownServer();
        }
        Assert.assertFalse(manager.isServerRunning());
    }

}