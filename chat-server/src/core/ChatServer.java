package core;

import common.Library;
import network.ServerSocketThread;
import network.ServerSocketThreadListener;
import network.SocketThread;
import network.SocketTreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketTreadListener {

    private ServerSocketThread sst;
    private final DateFormat dateFormat = new SimpleDateFormat("HH.mm.ss; ");
    private final ChatServerListener listener;
    private final Vector<SocketThread> clients = new Vector<>();

    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
    }

    public void start(int port) {
        if (sst != null && sst.isAlive()) {
            putLog("Server is already running!");
        } else {
            putLog("Server starts at port:" + port);
            sst = new ServerSocketThread(this, "server", port, 2000);
        }
    }

    public void stop() {
        if (sst == null || !sst.isAlive()) {
            putLog("Server not running");
        } else {
            sst.interrupt();
            putLog("Server stopped");
        }
    }

    private void putLog(String msg) {
        msg = dateFormat.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ":" + msg;
//        System.out.println(msg);
        listener.onChatServerMessage(this, msg);
    }

    /**
     * Server Socket Thread Events
     **/

    @Override
    public void onTreadStart(ServerSocketThread thread) {
        putLog("Thread start");

    }

    @Override
    public void onServerStart(ServerSocketThread thread, ServerSocket server) {
        putLog("Server start");
        SqlClient.connect();

    }

    @Override
    public void onServerAcceptTimeout(ServerSocketThread thread, ServerSocket server) {
    }

    @Override
    public void onSocketAccepted(ServerSocket server, Socket socket) {
        putLog("Socket accept");
        String name = "socketThread " +
                socket.getInetAddress() +
                ":" + socket.getPort();
        new ClientThread(this, name, socket);
    }

    @Override
    public void onServerException(ServerSocketThread thread, Exception e) {
        putLog("exception: " +
                e.getClass().getName() +
                ": " + e.getMessage());
    }

    @Override
    public void onThreadStop(ServerSocketThread thread) {
        putLog("Tread stop");
        SqlClient.disconnect();
    }

    /**
     * Socket Thread Events
     */

    @Override
    public synchronized void onSocketThreadStart(SocketThread thread, Socket socket) {
        putLog("Socket thread start");
    }

    @Override
    public synchronized void onSocketThreadStop(SocketThread thread) {
        putLog("Socket thread stop");
        clients.remove(thread);
    }

    @Override
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized()) {
            handleAuthorizedMsg(client, msg);
        } else {
            handleNonAuthorizedMsg(client, msg);
        }
    }

    @Override
    public synchronized void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");
        clients.add(thread);
    }

    @Override
    public synchronized void onSocketTreadException(SocketThread thread, Exception e) {
        putLog("exception: " +
                e.getClass().getName() +
                ": " + e.getMessage());
    }

    private void handleAuthorizedMsg(ClientThread thread, String msg) {
       sendToAllAuthorizedClients(msg);
    }

    private void sendToAllAuthorizedClients(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            client.sendMessage(msg);
        }
    }

    private void handleNonAuthorizedMsg(ClientThread newClient, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        if(arr.length != 3 || !arr[0].equals(Library.AUTH_REQUEST)) {
            newClient.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SqlClient.getNickName(login,password);
        if (nickname == null) {
            putLog("invalid password for login: " + login);
            newClient.autFail();
        }else {
            newClient.authAccept(nickname);
            sendToAllAuthorizedClients(
                    Library.getTypeBroadcast("sever",
                    nickname + "connected"));
        }
    }

}
