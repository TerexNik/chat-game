package org.nterekhin.game.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nterekhin.game.IntegrationTest;
import org.nterekhin.game.server.ServerManager;
import org.nterekhin.game.ui.PlayerUI;

import java.lang.reflect.Field;
import java.util.List;

public class PlayerUIManagerTest extends IntegrationTest {
    private static final PlayerUIManager manager = PlayerUIManager.getInstance();

    @Before
    public void setUp() throws Exception {
        if (!ServerManager.getInstance().isServerRunning()) {
            ServerManager.getInstance().startServer(IntegrationTest.port);
        }
    }

    @Test
    public void shouldStartPlayerUI() throws Exception {
        List<PlayerUI> uis = getPlayerUIThroughReflection();
        Assert.assertEquals(0, uis.size());
        manager.startClient(IntegrationTest.port);
        Assert.assertEquals(1, uis.size());
    }

    @Test
    public void shouldClearPlayerUI() throws Exception {
        List<PlayerUI> uis = getPlayerUIThroughReflection();
        Assert.assertEquals(0, uis.size());
        manager.startClient(IntegrationTest.port);
        Assert.assertEquals(1, uis.size());
        manager.clear();
        Assert.assertEquals(0, uis.size());
    }

    private List<PlayerUI> getPlayerUIThroughReflection() throws Exception {
        Field field = PlayerUIManager.class.getDeclaredField("playerUIs");
        field.setAccessible(true);
        return (List<PlayerUI>) field.get(manager);
    }
}