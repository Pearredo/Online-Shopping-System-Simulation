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
        this.username = username.substring(0, usernameLength);
        this.password = password.substring(0, passwordLength);
        this.name = name.substring(0, nameLength);
    }
    public SupplierAccount(byte[] record) { fill(record); }
    public SupplierAccount() { }
    // Class-Specific Attribute Methods
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        supplierID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        username = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += usernameLength * 4) - 1));
        password = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += passwordLength * 4) - 1));
        name = DataManager.decodeString(Arrays.copyOfRange(record, i, i + nameLength * 4 - 1));
    }
    public int id() { return supplierID; }
    public void setID(int id) { supplierID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(supplierID), i = 0, l = 4);
        serial.put(DataManager.encode(username, usernameLength), i += l, l = usernameLength * 4);
        serial.put(DataManager.encode(password, passwordLength), i += l, l = passwordLength * 4);
        serial.put(DataManager.encode(name, nameLength), i + l, nameLength * 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, supplierID); }
    public boolean delete() throws Exception { return DataManager.delete(this, supplierID); }
    // Static Methods
    public static SupplierAccount Login(String username, String password) throws Exception {
        int accountID = 0;
        SupplierAccount account = null,
                temp = new SupplierAccount();
        ArrayList<byte[]> accounts;
        int batchStart = 0,
                batchSize = 100;
        while (accountID < 1 && (accounts = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : accounts) {
                temp.fill(record);
                if (temp.getUsername().equals(username)) {
                    accountID = temp.id();
                    break;
                }
            }
        }
        if (accountID > 0 && temp.getPassword().equals(password)) {
            account = new SupplierAccount();
            account.fill(DataManager.read(account, account.id()));
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
