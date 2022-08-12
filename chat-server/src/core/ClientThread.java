package core;

import common.Library;
import network.SocketThread;
import network.SocketTreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {
    private  String nickname;
    private boolean isAuthorized;

    public ClientThread(SocketTreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void  authAccept (String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Library.getAuthAccept(nickname));
    }

    void autFail() {
        sendMessage(Library.getAuthDenied());
        close();
    }
    void msgFormatError (String msg) {
        sendMessage(Library.getMsgFormatError(msg));
        close();
    }
}
