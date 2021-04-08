package Core;

public class Buffer {
    private byte[] messageBuffer;
    private byte[] responseBuffer;
    private boolean messageBufferFull;
    private boolean responseBufferFull;
    public synchronized byte[] send(byte[] message) {
        messageBuffer = message;
        messageBufferFull = true;
        notify();
        while(!responseBufferFull) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("A communication between the OSS and BS was interrupted.");
            }
        }
        byte[] response = responseBuffer;
        responseBufferFull = false;
        return response;
    }
    public synchronized byte[] receive() {
        while (!messageBufferFull) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("A communication between the OSS and BS was interrupted.");
            }
        }
        byte[] message = messageBuffer;
        messageBufferFull = false;
        return message;
    }
    public synchronized void reply(byte[] response) {
        responseBuffer = response;
        responseBufferFull = true;
        notify();
    }
}
