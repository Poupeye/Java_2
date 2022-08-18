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
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss: ");
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
                Thread.currentThread().getName() + ": " + msg;
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
        dropAllClients();
    }

    public void dropAllClients() {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            clients.remove(client);
        }
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
        ClientThread client = (ClientThread) thread;
        clients.remove(thread);
        if (client.isAuthorized())
            sendToAllAuthorizedClients(Library.getUserList(getUsers()));
        sendToAllAuthorizedClients(Library.getTypeBroadcast("Server",
                client.getNickname() + "Disconnected"));
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
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.TYPE_CLIENT_BCAST:
                sendToAllAuthorizedClients(
                        Library.getTypeBroadcast(thread.getNickname(), arr[1]));
                break;
            default:
                thread.msgFormatError(msg);
        }
    }

    private void sendToAllAuthorizedClients(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            client.sendMessage(msg);
        }
    }

    private void handleNonAuthorizedMsg(ClientThread newClient, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        if (arr.length != 3 || !arr[0].equals(Library.AUTH_REQUEST)) {
            newClient.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SqlClient.getNickName(login, password);
        if (nickname == null) {
            putLog("invalid password for login: " + login);
            newClient.autFail();
        } else {
            ClientThread client = findUserByNickname(nickname);
            newClient.authAccept(nickname);
            if (client == null) {
                sendToAllAuthorizedClients(
                        Library.getTypeBroadcast("Sever: ",
                                nickname + " connected"));
            } else {
                client.close();
                clients.remove(client);
            }
        }
        sendToAllAuthorizedClients(Library.getUserList(getUsers()));
    }

    private String getUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            sb.append(client.getNickname()).append(Library.DELIMITER);
        }
        return sb.toString();
    }

    private ClientThread findUserByNickname(String nickname) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            if (client.getNickname().equals(nickname))
                return client;
        }
        return null;
    }

}
