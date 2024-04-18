package org.nterekhin.game.eventBus.listener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.EventType;
import org.nterekhin.game.eventBus.event.PlayerConnectedEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerConnectedListenerTest {
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
    public void shouldConnectPlayer() throws Exception {
        PlayerConnectedListener listener = new PlayerConnectedListener();
        eventBus.registerListener(EventType.PLAYER_CONNECTED, listener);
        Socket mock = mock(Socket.class);
        when(mock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mock.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        eventBus.postEvent(new PlayerConnectedEvent(mock));

        verify(mock, atLeastOnce()).getInputStream();
        verify(mock, atLeastOnce()).getOutputStream();
    }

}