package BankingSystem.RequestManager;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

class Request {
    public Request(String data) {
        // do shit...
    }
}

public class Main {
    private static boolean _DEBUG = true;
    public static void main(HashMap<String, String> config) throws Exception {
        File[] requests = new File(ClassLoader.getSystemResource(config.get("DIR_BANKREQUESTS")).getPath().substring(1)).listFiles();
        if (requests != null && requests.length > 1) {
            Scanner reader = new Scanner(requests[0].getAbsoluteFile());
            StringBuilder requestData = new StringBuilder();
            while (reader.hasNext()) {
                requestData.append(reader.next());
            }
            Request request = new Request(requestData.toString());
        } else {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                if (_DEBUG) System.out.println("An error occurred while forcing the Bank System to sleep.");
            }
        }
    }
}
