package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class SupplierAccount implements DataObject {
    // Attributes
    private int supplierID;
    private String username;
    private final int usernameLength = 20;
    private String password;
    private final int passwordLength = 20;
    private String name;
    private final int nameLength = 40;
    private final int recordLength = 324;
    public String dataFile = "FILE_DATA_SUPPLIERACCOUNTS";
    // Constructors
    public SupplierAccount(String username, String password, String name) {
        supplierID = 0;
        this.username = username.substring(0, Math.min(username.length(), usernameLength));
        this.password = password.substring(0, Math.min(password.length(), passwordLength));
        this.name = name.substring(0, Math.min(name.length(), nameLength));
    }
    public SupplierAccount(int id) throws Exception {
        byte[] data = DataManager.read(this, id);
        if (data != null) {
            fill(data);
            if (id() != id) {
                fill(new byte[recordLength()]);
            }
        }
    }
    public SupplierAccount(byte[] record) { fill(record); }
    public SupplierAccount() { }
    // Class-Specific Attribute Methods
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name.substring(0, Math.min(name.length(), nameLength)); }
    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        supplierID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        username = DataManager.decodeString(Arrays.copyOfRange(record, i, i += usernameLength * 4));
        password = DataManager.decodeString(Arrays.copyOfRange(record, i, i += passwordLength * 4));
        name = DataManager.decodeString(Arrays.copyOfRange(record, i, nameLength * 4));
    }
    public int id() { return supplierID; }
    public void setID(int id) { supplierID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(supplierID), 0, 4);
        serial.put(DataManager.encode(username, usernameLength), 0, usernameLength * 4);
        serial.put(DataManager.encode(password, passwordLength), 0, passwordLength * 4);
        serial.put(DataManager.encode(name, nameLength), 0, nameLength * 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    public String stringify() {
        return String.format(
            "Supplier ID: %d\n" +
            "Username: %s\n" +
            "Password: %s\n" +
            "Name: %s\n",
            supplierID,
            username,
            password,
            name);
    }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, supplierID); }
    public boolean delete() throws Exception { return DataManager.delete(this, supplierID); }
    // Static Methods
    public static void showAll() throws Exception {
        SupplierAccount record = new SupplierAccount();
        ArrayList<byte[]> accounts;
        int batchStart = 0,
                batchSize = 100;
        while ((accounts = DataManager.read(record, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] account : accounts) {
                record.fill(account);
                System.out.println(record.stringify());
            }
        }
    }
    public static SupplierAccount Login(String username, String password) throws Exception {
        boolean exists = false;
        SupplierAccount account = null,
            temp = new SupplierAccount();
        ArrayList<byte[]> accounts;
        int batchStart = 0,
            batchSize = 100;
        while (!exists && (accounts = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : accounts) {
                temp.fill(record);
                if (exists = temp.getUsername().equals(username)) {
                    if (temp.getPassword().equals(password)) {
                        account = temp;
                    } else {
                        account = new SupplierAccount(username, password, "");
                    }
                    break;
                }
            }
        }
        return account;
    }
    public static SupplierAccount getSupplier(int supplierID) throws Exception {
        SupplierAccount supplier = null,
            temp = new SupplierAccount();
        ArrayList<byte[]> suppliers;
        int batchStart = 0,
            batchSize = 100;
        while (supplier == null && (suppliers = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : suppliers) {
                temp.fill(record);
                if (temp.id() == supplierID) {
                    supplier = temp;
                    break;
                }
            }
        }
        return supplier;
    }
    public static ArrayList<SupplierAccount> getSuppliers() throws Exception {
        SupplierAccount temp = new SupplierAccount();
        ArrayList<SupplierAccount> suppliers = new ArrayList<>();
        ArrayList<byte[]> records;
        int batchStart = 0,
            batchSize = 100;
        while ((records = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : records) {
                if (ByteBuffer.wrap(record).getInt() > 0) {
                    suppliers.add(new SupplierAccount(record));
                }
            }
        }
        return suppliers;
    }
}
