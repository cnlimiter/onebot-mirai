package server.impl;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import server.AbstractWsEchoServer;

import java.net.InetSocketAddress;

public class SimpleWsEchoServer extends AbstractWsEchoServer {
    private final String msg;

    public SimpleWsEchoServer(InetSocketAddress address) {
        this(address, "");
    }

    public SimpleWsEchoServer(InetSocketAddress address, String msg) {
        super(address);
        this.msg = msg;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        super.onOpen(conn, handshake);
    }

    @Override
    protected String handleMessage(String inputMsg) {
        return inputMsg + " " + msg;
    }
}
