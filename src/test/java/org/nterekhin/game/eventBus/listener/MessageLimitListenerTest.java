package org.nterekhin.game.eventBus.listener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.EventType;
import org.nterekhin.game.eventBus.event.MessagesLimitEvent;
import org.nterekhin.game.server.ServerManager;

import java.lang.reflect.Field;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessageLimitListenerTest {
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
    public void shouldShutShutdownServer() throws Exception {
        ServerManager manager = mock(ServerManager.class);
        MessageLimitListener messageLimitListener = new MessageLimitListener();
        setMockThroughReflect(messageLimitListener, manager);
        eventBus.registerListener(EventType.MESSAGES_LIMIT_EXCEEDED, messageLimitListener);
        eventBus.postEvent(new MessagesLimitEvent());
        verify(manager, atLeastOnce()).shutdownApplication();
    }

    private void setMockThroughReflect(MessageLimitListener listener, ServerManager manager) throws Exception {
        Field field = MessageLimitListener.class.getDeclaredField("serverManager");
        field.setAccessible(true);
        field.set(listener, manager);
    }
}