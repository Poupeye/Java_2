package network;

import java.net.Socket;

public interface SocketTreadListener {
    void onSocketThreadStart (SocketThread thread, Socket socket);
    void onSocketThreadStop (SocketThread thread);

    void onReceiveString (SocketThread thread, Socket socket, String msg);
    void onSocketReady(SocketThread thread, Socket socket);

    void onSocketTreadException (SocketThread thread, Exception e);

}
