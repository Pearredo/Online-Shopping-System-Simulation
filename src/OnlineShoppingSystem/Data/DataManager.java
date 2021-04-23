package OnlineShoppingSystem.Data;

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
        // get {data type | data file name} {id | all}
        // put {data type | data file name} {id | new} {data attributes} *See data class constructor for needed attributes
        // del {data type | data file name} {id}
        // Setup bulk request files in a notepad and just them paste into the CMD window for data population...
        // There is no error handling here, so use with caution
        _CONFIG = Core.loadConfig("oss");
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
            switch (args.length == 1 ? "ALL" : args[1].toUpperCase()) {
                case "CUSTOMER":
                case "CUSTOMERS":
                case "CUSTOMERACCOUNT":
                case "CUSTOMERACCOUNTS":
                case "FILE_DATA_CUSTOMERACCOUNTS": CustomerAccount.showAll(); break;
                case "ITEM":
                case "ITEMS":
                case "FILE_DATA_ITEMS": Item.showAll(); break;
                case "ORDER":
                case "ORDERS":
                case "FILE_DATA_ORDERS": Order.showAll(); break;
                case "ORDERITEM":
                case "ORDERITEMS":
                case "FILE_DATA_ORDERITEMS": OrderItem.showAll(); break;
                case "SUPPLIER":
                case "SUPPLIERS":
                case "SUPPLIERACCOUNT":
                case "SUPPLIERACCOUNTS":
                case "FILE_DATA_SUPPLIERACCOUNTS": SupplierAccount.showAll(); break;
                case "ALL":
                    CustomerAccount.showAll();
                    Item.showAll();
                    Order.showAll();
                    OrderItem.showAll();
                    SupplierAccount.showAll();
            }
        } else if (args.length > 2) {
            switch (args[1].toUpperCase()) {
                case "CUSTOMER":
                case "CUSTOMERS":
                case "CUSTOMERACCOUNT":
                case "CUSTOMERACCOUNTS":
                case "FILE_DATA_CUSTOMERACCOUNTS":
                    switch (args[0].toLowerCase()) {
                        case "g":
                        case "get":
                            if (args[2].equalsIgnoreCase("all")) {
                                CustomerAccount.showAll();
                            } else if (!args[2].matches("[^0-9]")) {
                                System.out.println(new CustomerAccount(Integer.parseInt(args[2])).stringify());
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "p":
                        case "put":
                            if (args.length == 11) {
                                String isPrem = args[9].toLowerCase(),
                                    premPaid = args[10].toLowerCase();
                                CustomerAccount record = new CustomerAccount(
                                    args[3],
                                    args[4],
                                    args[5],
                                    args[6],
                                    args[7],
                                    args[8],
                                    isPrem.equals("1") || isPrem.equals("y") || isPrem.equals("true") || isPrem.equals("yes"),
                                    premPaid.equals("1") || premPaid.equals("y") || premPaid.equals("true") || premPaid.equals("yes"));
                                if (args[2].equalsIgnoreCase("new")) {
                                    record.create();
                                } else if (!args[2].matches("[^0-9]")) {
                                    record.setID(Integer.parseInt(args[2]));
                                    record.update();
                                } else {
                                    System.out.println("Malformed command");
                                }
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "d":
                        case "del":
                            if (!args[2].matches("[^0-9]")) {
                                new CustomerAccount(Integer.parseInt(args[2])).delete();
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                    }
                case "ITEM":
                case "ITEMS":
                case "FILE_DATA_ITEMS":
                    switch (args[0].toLowerCase()) {
                        case "g":
                        case "get":
                            if (args[2].equalsIgnoreCase("all")) {
                                Item.showAll();
                            } else if (!args[2].matches("[^0-9]")) {
                                System.out.println(new Item(Integer.parseInt(args[2])).stringify());
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "p":
                        case "put":
                            if (args.length == 9) {
                                Item record = new Item(
                                    Integer.parseInt(args[3]),
                                    args[4],
                                    args[5],
                                    Float.parseFloat(args[6]),
                                    Float.parseFloat(args[7]),
                                    Integer.parseInt(args[8]));
                                if (args[2].equalsIgnoreCase("new")) {
                                    record.create();
                                } else if (!args[2].matches("[^0-9]")) {
                                    record.setID(Integer.parseInt(args[2]));
                                    record.update();
                                } else {
                                    System.out.println("Malformed command");
                                }
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "d":
                        case "del":
                            if (!args[2].matches("[^0-9]")) {
                                new Item(Integer.parseInt(args[2])).delete();
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                    }
                case "ORDER":
                case "ORDERS":
                case "FILE_DATA_ORDERS":
                    switch (args[0].toLowerCase()) {
                        case "g":
                        case "get":
                            if (args[2].equalsIgnoreCase("all")) {
                                Order.showAll();
                            } else if (!args[2].matches("[^0-9]")) {
                                System.out.println(new Order(Integer.parseInt(args[2])).stringify());
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "p":
                        case "put":
                            if (args.length == 10) {
                                Order record = new Order(
                                    Integer.parseInt(args[3]),
                                    Byte.parseByte(args[4]),
                                    Integer.parseInt(args[5]),
                                    Float.parseFloat(args[6]),
                                    Float.parseFloat(args[7]),
                                    Byte.parseByte(args[8]),
                                    Float.parseFloat(args[9]));
                                if (args[2].equalsIgnoreCase("new")) {
                                    record.create();
                                } else if (!args[2].matches("[^0-9]")) {
                                    record.setID(Integer.parseInt(args[2]));
                                    record.update();
                                } else {
                                    System.out.println("Malformed command");
                                }
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "d":
                        case "del":
                            if (!args[2].matches("[^0-9]")) {
                                new Order(Integer.parseInt(args[2])).delete();
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                    }
                case "ORDERITEM":
                case "ORDERITEMS":
                case "FILE_DATA_ORDERITEMS":
                    switch (args[0].toLowerCase()) {
                        case "g":
                        case "get":
                            if (args[2].equalsIgnoreCase("all")) {
                                OrderItem.showAll();
                            } else if (!args[2].matches("[^0-9]")) {
                                System.out.println(new OrderItem(Integer.parseInt(args[2])).stringify());
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "p":
                        case "put":
                            if (args.length == 8) {
                                OrderItem record = new OrderItem(
                                    Integer.parseInt(args[3]),
                                    Integer.parseInt(args[4]),
                                    Integer.parseInt(args[5]),
                                    Float.parseFloat(args[6]),
                                    Byte.parseByte(args[7]));
                                if (args[2].equalsIgnoreCase("new")) {
                                    record.create();
                                } else if (!args[2].matches("[^0-9]")) {
                                    record.setID(Integer.parseInt(args[2]));
                                    record.update();
                                } else {
                                    System.out.println("Malformed command");
                                }
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "d":
                        case "del":
                            if (!args[2].matches("[^0-9]")) {
                                new OrderItem(Integer.parseInt(args[2])).delete();
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                    }
                case "SUPPLIER":
                case "SUPPLIERS":
                case "SUPPLIERACCOUNT":
                case "SUPPLIERACCOUNTS":
                case "FILE_DATA_SUPPLIERACCOUNTS":
                    switch (args[0].toLowerCase()) {
                        case "g":
                        case "get":
                            if (args[2].equalsIgnoreCase("all")) {
                                SupplierAccount.showAll();
                            } else if (!args[2].matches("[^0-9]")) {
                                System.out.println(new SupplierAccount(Integer.parseInt(args[2])).stringify());
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "p":
                        case "put":
                            if (args.length == 6) {
                                SupplierAccount record = new SupplierAccount(args[3], args[4], args[5]);
                                if (args[2].equalsIgnoreCase("new")) {
                                    record.create();
                                } else if (!args[2].matches("[^0-9]")) {
                                    record.setID(Integer.parseInt(args[2]));
                                    record.update();
                                } else {
                                    System.out.println("Malformed command");
                                }
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                        case "d":
                        case "del":
                            if (!args[2].matches("[^0-9]")) {
                                new SupplierAccount(Integer.parseInt(args[2])).delete();
                            } else {
                                System.out.println("Malformed command");
                            }
                            break;
                    }
            }
        }
    }
    public static void initialize(HashMap<String, String> config, Buffer buffer) {
        _CONFIG = config;
        _BUFFER = buffer;
        _DEBUG = config.get("DEBUG").equals("Y");
    }
    static Buffer getBuffer() { return _BUFFER; }
    static String getDir(String id) { return _CONFIG.get(id); }
    static String getDBDir(String id) { return "OnlineShoppingSystem/" + getDir("DIR_DATABASE") + getDir(id); }
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
