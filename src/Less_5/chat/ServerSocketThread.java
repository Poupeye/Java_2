package Less_5.chat;

public class ServerSocketThread extends Thread {

    private final int PORT;
    public ServerSocketThread(String name, int port) {
        super(name);
        this.PORT = port;
        start();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            System.out.println("SST is working");
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}
