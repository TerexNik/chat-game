package org.nterekhin.game.eventBus.listener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nterekhin.game.IntegrationTest;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.EventType;
import org.nterekhin.game.eventBus.event.PlayerChooseNicknameEvent;

import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PlayerChooseNicknameListenerTest extends IntegrationTest {
    private final static EventBus eventBus = EventBus.getInstance();

    @Before
    public void setUp() throws Exception {
        eventBus.clearUpEventBusFromServerListeners();
    }

    @After
    public void tearDown() {
        eventBus.clearUpEventBusFromServerListeners();
        eventBus.setUpEventBusForServer();
    }


    @Test
    public void shouldSendConnectionMessageServer() throws Exception {
        PrintStream spy = mock(PrintStream.class);
        System.setOut(spy);
        PlayerChooseNicknameListener listener = new PlayerChooseNicknameListener();
        eventBus.registerListener(EventType.PLAYER_CHOOSE_NICKNAME, listener);
        eventBus.postEvent(new PlayerChooseNicknameEvent("Nick", "localhost:1342"));
        verify(spy, atLeastOnce()).printf(anyString(), eq("Nick"), eq("localhost:1342"));
    }
}