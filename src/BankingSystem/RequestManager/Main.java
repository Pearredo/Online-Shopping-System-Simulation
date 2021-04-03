package BankingSystem.RequestManager;

import BankingSystem.Data.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class Main {
    private static boolean _DEBUG = false;
    public static void main(HashMap<String, String> config) throws Exception {
        _DEBUG = config.get("DEBUG").equals("Y");
        File[] requestFiles = new File(ClassLoader.getSystemResource(config.get("DIR_BANKREQUESTS")).getPath().substring(1)).listFiles();
        if (requestFiles != null && requestFiles.length > 1) {
            DataManager.initialize(config);
            for (File requestFile : requestFiles) {
                if (requestFile.getName().startsWith("RESOLVED") || requestFile.getName().startsWith("ANSWER")) continue;
                RandomAccessFile file = new RandomAccessFile(requestFile, "r");
                Request request = new Request();
                byte[] bytes = new byte[request.recordLength()];
                if (file.read(bytes) > 0) {
                    request.fill(bytes);
                    try {
                        RandomAccessFile answer = new RandomAccessFile(requestFile.getAbsolutePath().replace(requestFile.getName(), "ANSWER_" + requestFile.getName()), "rw");
                        answer.setLength(8);
                        answer.write(request.execute());
                        answer.close();
                    } catch (Exception ex) {
                        throw new Exception(String.format("Request result %s already existed or could not be created.", "ANSWER_" + requestFile.getName()));
                    }
                }
                // Errors beyond this points should crash the thread to try and mitigate i/o issues
                requestFile.renameTo(new File(requestFile.getAbsolutePath().replace(requestFile.getName(), "RESOLVED_" + requestFile.getName())));
                file.close();
                requestFile.delete();
            }
        } else {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                if (_DEBUG) System.out.println("An error occurred while forcing the Bank System to sleep.");
            }
        }
    }
}
