package org.nterekhin.game.client;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.nterekhin.game.IntegrationTest;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Kills server so moved to separate test
 * <p>
 * Can be fixed by implementing Runner that will start new Java process for each class
 * or by move to JUnit5
 */
@Ignore
public class PlayerManagerShutdownTest extends IntegrationTest {

    private static final BufferedReader mockReader = mock(BufferedReader.class);
    private static final PrintWriter mockWriter = mock(PrintWriter.class);
    private final PlayerManager manager = PlayerManager.getInstance();

    @Test
    public void shouldCloseAllConnectionsAndStreamsWhenShutdown() throws Exception {
        when(mockReader.readLine()).thenAnswer(invocation -> {
            // Simulate waiting for input
            Thread.sleep(10);
            return "Mocked input";
        });
        manager.clear();
        Socket socket = new Socket("localhost", IntegrationTest.port);
        PlayerHandler playerHandler = new PlayerHandler(
                socket,
                mockReader,
                mockWriter,
                new PlayerState("Bob", new AtomicInteger(0))
        );
        manager.createPlayerHandler(playerHandler);

        manager.shutdown();

        verify(mockReader, atLeastOnce()).close();
        verify(mockWriter, atLeastOnce()).close();
        Assert.assertTrue(socket.isClosed());
    }
}