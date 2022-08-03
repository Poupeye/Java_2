package Less_5.chat;

public class ChatServer {

    private ServerSocketThread sst;
    public void start(int port) {
        if (sst != null && sst.isAlive()) {
            System.out.println("Server is already running!");
        }else {
            System.out.printf("Server starts at port; %d\n", port);
            sst = new  ServerSocketThread("server", 8189);
        }
    }

    public void stop() {
        if (sst == null || !sst.isAlive()) {
            System.out.println("Server not running");
        }else {
            sst.interrupt();
            System.out.println("Server stopped");
        }
    }
}
