package BankingSystem.RequestManager;

import BankingSystem.Data.*;
import Core.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Main {
    private static boolean _DEBUG = false;
    public static void main(HashMap<String, String> config, Buffer buffer) throws Exception {
        _DEBUG = config.get("DEBUG").equals("Y");
        byte[] request = buffer.receive();
        if (request.length == 1 && request[0] == -1) {
            buffer.reply(new byte[] { 0 });
        } else {
            DataManager.initialize(config, buffer);
            buffer.reply(ByteBuffer.wrap(new byte[8]).putLong(new Request(request).execute()).array());
        }
    }
}
