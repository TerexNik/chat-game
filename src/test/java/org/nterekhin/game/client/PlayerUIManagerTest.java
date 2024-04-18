package org.nterekhin.game.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nterekhin.game.IntegrationTest;
import org.nterekhin.game.ui.PlayerUI;

import java.lang.reflect.Field;
import java.util.List;

@Ignore
public class PlayerUIManagerTest extends IntegrationTest {
    private static final PlayerUIManager manager = PlayerUIManager.getInstance();

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void shouldStartPlayerUI() throws Exception {
        List<PlayerUI> uis = getPlayerUIThroughReflection();
        Assert.assertEquals(0, uis.size());
        manager.startClient(IntegrationTest.port);
        Assert.assertEquals(1, uis.size());
    }

    private List<PlayerUI> getPlayerUIThroughReflection() throws Exception {
        Field field = PlayerUIManager.class.getDeclaredField("playerUIs");
        field.setAccessible(true);
        return (List<PlayerUI>) field.get(manager);
    }
}