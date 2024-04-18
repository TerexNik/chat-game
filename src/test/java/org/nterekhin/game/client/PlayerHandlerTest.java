package org.nterekhin.game.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nterekhin.game.IntegrationTest;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.EventType;
import org.nterekhin.game.eventBus.listener.MessageLimitListener;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerHandlerTest extends IntegrationTest {

    private static final BufferedReader mockReader = mock(BufferedReader.class);
    private static final PrintWriter mockWriter = mock(PrintWriter.class);
    private PlayerHandler playerHandler;
    private Socket socket;

    @Before
    public void setUp() throws Exception {
        when(mockReader.readLine()).thenAnswer(invocation -> {
            // Simulate waiting for input
            Thread.sleep(10);
            return "Mocked input";
        });
        PlayerManager.getInstance().clear();
        socket = new Socket("localhost", IntegrationTest.port);
        playerHandler = new PlayerHandler(
                socket,
                mockReader,
                mockWriter,
                new PlayerState("Bob", new AtomicInteger(0))
        );
        PlayerManager.getInstance().createPlayerHandler(playerHandler);
    }

    @After
    public void tearDown() throws Exception {
        PlayerManager.getInstance().clear();
    }

    @Test
    public void shouldTriggerStopCondition() throws Exception {
        MessageLimitListener listener = mock(MessageLimitListener.class);
        EventBus.getInstance().unregisterListener(EventType.MESSAGES_LIMIT_EXCEEDED);
        EventBus.getInstance().registerListener(EventType.MESSAGES_LIMIT_EXCEEDED, listener);
        Socket testSocket = new Socket("localhost", IntegrationTest.port);
        PlayerHandler testHandler = new PlayerHandler(
                testSocket,
                mockReader,
                mockWriter,
                new PlayerState("HanSolo", new AtomicInteger(5))
        );
        PlayerManager.getInstance().createPlayerHandler(testHandler);
        PlayerManager.getInstance().broadcast(testHandler.getPlayerState(), "Test message!");
        verify(listener).onEvent(any());

        EventBus.getInstance().clearUpEventBusFromServerListeners();
        EventBus.getInstance().setUpEventBusForServer();
        EventBus.getInstance().unregisterListener(EventType.PLAYER_CONNECTED);
    }

    @Test
    public void shouldIncreaseMessageCounter() throws Exception {
        Socket testSocket = new Socket("localhost", IntegrationTest.port);
        BufferedReader testReader = new BufferedReader(new StringReader("Test message!"));
        PlayerHandler testHandler = new PlayerHandler(
                testSocket,
                testReader,
                mockWriter,
                new PlayerState("HanSolo", new AtomicInteger(1))
        );
        PlayerManager.getInstance().createPlayerHandler(testHandler);
        int messageCounter = testHandler.getPlayerState().getMessageCounter();
        PlayerManager.getInstance().broadcast(testHandler.getPlayerState(), "Test message!");
        Assert.assertEquals(messageCounter + 1, testHandler.getPlayerState().getMessageCounter());
    }

    @Test
    public void shouldSetPlayerNickname() throws Exception {
        EventBus.getInstance().clearUpEventBusFromServerListeners();
        EventBus.getInstance().setUpEventBusForServer();
        playerHandler.init();
        verify(mockWriter, atLeastOnce()).println("Please choose nickname");
        Assert.assertEquals("Mocked input", playerHandler.getPlayerState().getNickname());
        EventBus.getInstance().unregisterListener(EventType.PLAYER_CONNECTED);
    }

    @Test
    public void shouldSendMessage() throws Exception {
        playerHandler.sendMessage("Hello World");
        verify(mockWriter, atLeastOnce()).println("Hello World");
    }

    @Test
    public void shouldDisconnectPlayerAndCloseHimHandler() throws Exception {
        playerHandler.disconnect();

        verify(mockWriter).println("Server: \"Bob\" disconnected from the chat");
        verify(mockReader, atLeastOnce()).close();
        verify(mockWriter, atLeastOnce()).close();
        Assert.assertTrue(socket.isClosed());
    }

    @Test
    public void shouldClosePlayerHandler() throws Exception {
        playerHandler.close();

        verify(mockReader, atLeastOnce()).close();
        verify(mockWriter, atLeastOnce()).close();
        Assert.assertTrue(socket.isClosed());
    }

    private void wait(int mills) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(latch::countDown).start();
        latch.await(mills, TimeUnit.MILLISECONDS);
    }
}