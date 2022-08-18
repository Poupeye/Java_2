package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SocketThread extends Thread {
    private Socket socket;
    private SocketTreadListener listener;
    private DataOutputStream out;

    public SocketThread(SocketTreadListener listener, String name, Socket socket) {
        super(name);
        this.socket = socket;
        this.listener = listener;
        start();
    }

    @Override
    public void run() {
        try {
            listener.onSocketThreadStart(this, socket);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener.onSocketReady(this,socket);
            while (!isInterrupted()) {
                String msg = in.readUTF();
                listener.onReceiveString(this,socket,msg);
            }
        } catch (IOException e) {
            listener.onSocketTreadException(this,e);
        }finally {
            try {
                socket.close();
                listener.onSocketThreadStop(this);
            } catch (IOException e) {
                listener.onSocketTreadException(this,e);
            }
        }
    }

    public synchronized boolean sendMessage (String msg) {
        try {
            out.writeUTF(msg);
            return true;
        } catch (IOException e) {
            listener.onSocketTreadException(this, e);
//            close();
            return false;
        }
    }

    public  synchronized void close() {
        interrupt();
        try {
            socket.close();
            listener.onSocketThreadStop(this);
        } catch (IOException e) {
            listener.onSocketTreadException(this,e);
        }
    }
}
