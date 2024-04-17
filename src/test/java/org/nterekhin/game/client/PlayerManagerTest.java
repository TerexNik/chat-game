package org.nterekhin.game.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerManagerTest {
    private final PlayerManager manager = PlayerManager.getInstance();
    private Socket mockSocket;
    private InputStream mockInputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setUp() throws Exception {
        mockSocket = mock(Socket.class);
        mockInputStream = mock(InputStream.class);
        mockOutputStream = mock(OutputStream.class);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        when(mockSocket.getPort()).thenReturn(1024);
        when(mockSocket.isClosed()).thenReturn(false);
    }

    @After
    public void tearDown() throws Exception {
        mockSocket = null;
        mockInputStream = null;
        mockOutputStream = null;
    }

    @Test
    public void shouldVerifyNickname() throws Exception {
        Assert.assertFalse(manager.verifyNickname(null));
        Assert.assertFalse(manager.verifyNickname(""));
        Assert.assertTrue(manager.verifyNickname("Bob"));
    }

    @Test
    public void shouldCloseAllConnectionsAndStreamsWhenShutdown() throws Exception {
        // When
        manager.createPlayerHandler(mockSocket);
        manager.shutdown();
        // Then
        // Verify interactions with mock objects
        verify(mockInputStream, atLeastOnce()).close();
        verify(mockOutputStream, atLeastOnce()).close();
        verify(mockSocket, atLeastOnce()).close();
    }

    @Test
    public void shouldCloseAllConnectionsAndStreamsWhenClear() throws Exception {
        // When
        manager.createPlayerHandler(mockSocket);
        manager.clear();
        // Then
        // Verify interactions with mock objects
        verify(mockInputStream, atLeastOnce()).close();
        verify(mockOutputStream, atLeastOnce()).close();
        verify(mockSocket, atLeastOnce()).close();
    }

    @Test
    public void shouldCloseAllConnectionsAndStreamsWhenDisconnectedByPort() throws Exception {
        // When
        manager.createPlayerHandler(mockSocket);
        manager.disconnectByPort(1024);
        // Then
        // Verify interactions with mock objects
        verify(mockSocket).getInputStream();
        verify(mockSocket).getOutputStream();
        verify(mockInputStream, atLeastOnce()).close();
        verify(mockOutputStream, atLeastOnce()).close();
    }
}