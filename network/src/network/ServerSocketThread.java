package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerSocketThread extends Thread {

    private final int PORT;
    private final int TIMEOUT;
    private final ServerSocketThreadListener listener;

    public ServerSocketThread(ServerSocketThreadListener listener, String name, int port, int timeout) {
        super(name);
        this.PORT = port;
        this.TIMEOUT = timeout;
        this.listener = listener;
        start();
    }

    @Override
    public void run() {
        listener.onTreadStart(this);
        try (ServerSocket server = new ServerSocket(8189)) {
            server.setSoTimeout(TIMEOUT);
            listener.onServerStart(this,server);
            Socket socket;
            while (!isInterrupted()) {
                try {
                    socket = server.accept();
                } catch (SocketTimeoutException e) {
                    listener.onServerAcceptTimeout(this,server);
                    continue;
                }
                listener.onSocketAccepted(server,socket);
            }
        } catch (IOException e) {
            listener.onServerException(this,e);
        } finally {
            listener.onThreadStop(this);
        }
    }
}
