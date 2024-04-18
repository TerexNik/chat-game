package org.nterekhin.game.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.nterekhin.game.IntegrationTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerManagerTest extends IntegrationTest {

    private static final BufferedReader mockReader = mock(BufferedReader.class);
    private static final PrintWriter mockWriter = mock(PrintWriter.class);
    private final PlayerManager manager = PlayerManager.getInstance();
    private Socket socket;
    private PlayerHandler playerHandler;

    @Before
    public void setUp() throws Exception {
        when(mockReader.readLine()).thenAnswer(invocation -> {
            // Simulate waiting for input
            Thread.sleep(100);
            return "Mocked input";
        });
        manager.clear();
        socket = new Socket("localhost", IntegrationTest.port);
        playerHandler = new PlayerHandler(
                socket,
                mockReader,
                mockWriter,
                new PlayerState("Bob", new AtomicInteger(0))
        );
        manager.createPlayerHandler(playerHandler);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(mockReader, mockWriter);
    }

    @Test
    public void shouldCreatePlayerHandler() throws Exception {
        List<PlayerHandler> handler = getPlayerHandlersThroughReflection();
        Assert.assertEquals("Number of Player handlers should be 1", 1, handler.size());
        PlayerState hanSolo = new PlayerState("HanSolo", new AtomicInteger(0));
        addNewPlayer(hanSolo);
        Assert.assertEquals("Number of Player handlers should be 2", 2, handler.size());
    }

    @Test
    public void shouldBroadcastServerMessage() throws Exception {
        PlayerState hanSolo = new PlayerState("HanSolo", new AtomicInteger(0));
        addNewPlayer(hanSolo);

        manager.broadcastServerMessage("Hello world!");

        verify(mockReader, atLeastOnce()).readLine();
        verify(mockWriter, atLeastOnce()).println("Server: Hello world!");
    }

    @Test
    public void shouldVerifyNickname() throws Exception {
        Assert.assertFalse(manager.verifyNickname(null));
        Assert.assertFalse(manager.verifyNickname(""));
        Assert.assertFalse(manager.verifyNickname("Bob"));
        Assert.assertTrue(manager.verifyNickname("HanSolo"));
    }

    @Test
    public void shouldReturnTrueWhen2OrMore() throws Exception {
        Assert.assertFalse(manager.isEnoughPlayersOnline());
        addNewPlayer(new PlayerState("HanSolo", new AtomicInteger(0)));
        Assert.assertTrue(manager.isEnoughPlayersOnline());
    }

    @Test
    public void shouldCloseAllConnectionsAndStreamsWhenClear() throws Exception {
        List<PlayerHandler> handlers = getPlayerHandlersThroughReflection();
        Assert.assertFalse(handlers.isEmpty());

        manager.clear();

        Assert.assertTrue(handlers.isEmpty());
        verify(mockReader, atLeastOnce()).close();
        verify(mockWriter, atLeastOnce()).close();
        Assert.assertTrue(socket.isClosed());
    }

    @Test
    public void shouldDisconnectByNickname() throws Exception {
        String nickname = "HanSolo";
        addNewPlayer(new PlayerState(nickname, new AtomicInteger(0)));
        List<PlayerHandler> handlers = getPlayerHandlersThroughReflection();
        Assert.assertTrue(containsNickname(handlers, nickname));
        Assert.assertTrue(containsNickname(handlers, "Bob"));

        manager.disconnectByNickname(nickname);
        Assert.assertFalse(containsNickname(handlers, nickname));
        Assert.assertTrue(containsNickname(handlers, "Bob"));
    }

    @Test
    public void shouldCloseAllConnectionsAndStreamsWhenDisconnectedByPort() throws Exception {
        List<PlayerHandler> handlersThroughReflection = getPlayerHandlersThroughReflection();
        Assert.assertTrue(handlersThroughReflection.contains(playerHandler));

        manager.disconnectByPort(port);
        Assert.assertFalse(handlersThroughReflection.contains(playerHandler));

        // Verify interactions with mock objects
        verify(mockReader, atLeastOnce()).close();
        verify(mockWriter, atLeastOnce()).close();
    }

    private void addNewPlayer(PlayerState state) throws IOException {
        PlayerHandler handler = new PlayerHandler(
                new Socket("localhost", port),
                mockReader,
                mockWriter,
                state
        );
        manager.createPlayerHandler(handler);
    }

    private boolean containsNickname(List<PlayerHandler> playerHandlers, String nickname) {
        return playerHandlers.stream().map(PlayerHandler::getPlayerState)
                .map(PlayerState::getNickname)
                .anyMatch(nickname::equals);
    }

    private List<PlayerHandler> getPlayerHandlersThroughReflection()
            throws NoSuchFieldException, IllegalAccessException {
        Field playerHandlers = PlayerManager.class.getDeclaredField("playerHandlers");
        playerHandlers.setAccessible(true);
        return (List<PlayerHandler>) playerHandlers.get(manager);
    }
}