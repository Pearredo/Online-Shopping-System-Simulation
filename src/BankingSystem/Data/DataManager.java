package BankingSystem.Data;

import Core.Buffer;
import Core.Core;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataManager {
    private static boolean _DEBUG = false;
    private static final boolean _CLEANUP = true;
    private static HashMap<String, String> _CONFIG;
    private static Buffer _BUFFER;
    static byte[] encode(int v) { return ByteBuffer.allocate(4).putInt(v).array(); }
    static byte[] encode(long v) { return ByteBuffer.allocate(8).putLong(v).array(); }
    static byte[] encode(float v) { return ByteBuffer.allocate(4).putFloat(v).array(); }
    static byte[] encode(String v, int l) {
        int i = 0;
        byte[] d = new byte[l * 4]; // UTF-8 can use at most 4 bytes per character
        for (Byte b : v.getBytes(StandardCharsets.UTF_8)) d[i++] = b;
        return d;
    }
    static float decodeFloat(byte[] b) { return ByteBuffer.wrap(b).getFloat(); }
    static long decodeLong(byte[] b) { return ByteBuffer.wrap(b).getLong(); }
    static int decodeInt(byte[] b) { return ByteBuffer.wrap(b).getInt(); }
    static String decodeString(byte[] b) { return new String(b, StandardCharsets.UTF_8); }
    public static void main(String[] args) throws Exception {
        // add c creditCard balance
        // get a accountID
        // get c creditCard
        // put a accountID balance
        // put c creditCard balance
        // del a accountID
        // del c creditCard
        // chk a accountID balance
        // chk c creditCard balance
        // Setup bulk request files in a notepad and just them paste into the CMD window for data population...
        // There is no error handling here, so use with caution
        _CONFIG = Core.loadConfig("bs");
        if (args.length == 0) {
            Scanner inReader = new Scanner(System.in);
            String input = "";
            while (!input.equalsIgnoreCase("exit")) {
                System.out.print("Enter a command line, or exit: ");
                if (!(input = inReader.nextLine().trim()).equalsIgnoreCase("exit")) {
                    execute(input.split(" "));
                }
            }
        }
    }
    static void execute(String[] args) throws Exception {
        if (args.length > 0 && args[0].equalsIgnoreCase("showme")) {
            BankAccount.showAll();
        } else if (args.length > 2) {
            System.out.printf("Auth Number: %d\n", new Request(
                args[0],
                args[1],
                args[1].equalsIgnoreCase("a") ? Integer.parseInt(args[2]) : 0,
                args[1].equalsIgnoreCase("c") ? args[2] : "",
                args.length > 3 ? Float.parseFloat(args[3]) : 0).execute());
        } else {
            System.out.println("Malformed command");
        }
    }
    public static void initialize(HashMap<String, String> config, Buffer buffer) {
        _CONFIG = config;
        _BUFFER = buffer;
        _DEBUG = config.get("DEBUG").equals("Y");
    }
    static Buffer getBuffer() { return _BUFFER; }
    static String getDir(String id) { return _CONFIG.get(id); }
    static String getDBDir(String id) { return "BankingSystem/" + getDir("DIR_DATABASE") + getDir(id); }
    static boolean create(DataObject data) throws Exception {
        String filePath = ClassLoader.getSystemResource(getDBDir(data.dataFile())).getPath().substring(1);
        RandomAccessFile file = new RandomAccessFile(new File(filePath), "rw");
        data.setID((int)file.length() / data.recordLength() + 1);
        file.setLength(file.length() + data.recordLength());
        file.seek((long)(data.id() - 1) * data.recordLength());
        file.write(data.serialize(), 0, data.recordLength());
        file.close();
        return true;
    }
    static byte[] read(DataObject data, int id) throws Exception {
        String filePath = ClassLoader.getSystemResource(getDBDir(data.dataFile())).getPath().substring(1);
        RandomAccessFile file = new RandomAccessFile(new File(filePath), "rw");
        int offset = (id - 1) * data.recordLength();
        byte[] record = null;
        if (file.length() >= (long)offset + data.recordLength()) {
            record = new byte[data.recordLength()];
            file.seek(offset);
            if (file.read(record, 0, data.recordLength()) < 1) {
                record = null;
                if (_CLEANUP && file.length() > (long)offset)
                    file.setLength(offset); // Really shitty truncation hack...
                if (_DEBUG) System.out.printf("A malformed data record was found in %s.", data.dataFile());
            }
        }
        file.close();
        return record;
    }
    static ArrayList<byte[]> read(DataObject data, int s, int n) throws Exception {
        String filePath = ClassLoader.getSystemResource(getDBDir(data.dataFile())).getPath().substring(1);
        RandomAccessFile file = new RandomAccessFile(new File(filePath), "rw");
        ArrayList<byte[]> records = new ArrayList<>();
        int sIndex = s * data.recordLength(),
            eIndex = sIndex + n * data.recordLength();
        for (int i = sIndex; i < file.length() && i < eIndex; i += data.recordLength()) {
            byte[] record = new byte[data.recordLength()];
            if (file.read(record, 0, data.recordLength()) > 0) {
                records.add(record);
            } else {
                if (_CLEANUP && file.length() > (long)i) file.setLength(i); // Really shitty truncation hack...
                if (_DEBUG) System.out.printf("A malformed data record was found in %s.", data.dataFile());
            }
        }
        file.close();
        return records;
    }
    static boolean update(DataObject data, int id) throws Exception {
        boolean updated;
        String filePath = ClassLoader.getSystemResource(getDBDir(data.dataFile())).getPath().substring(1);
        RandomAccessFile file = new RandomAccessFile(new File(filePath), "rw");
        int offset = (id - 1) * data.recordLength();
        if (updated = (file.length() > offset)) {
            file.seek(offset);
            file.write(data.serialize(), 0, data.recordLength());
        }
        file.close();
        return updated;
    }
    static boolean delete(DataObject data, int id) throws Exception {
        // Probably shouldn't utilize this for sanity's sake...
        // This just 0s out a record if it exists and is definitely a bad idea for a real DB
        // While 0ing out a record works, a cleanup process should run that re-indexes all the records and drops the 0 lines
        String filePath = ClassLoader.getSystemResource(getDBDir(data.dataFile())).getPath().substring(1);
        RandomAccessFile file = new RandomAccessFile(new File(filePath), "rw");
        int offset = (id - 1) * data.recordLength();
        if (file.length() > offset) {
            file.seek(offset);
            file.write(new byte[data.recordLength()], 0, data.recordLength());
        }
        file.close();
        return true;
    }
}
