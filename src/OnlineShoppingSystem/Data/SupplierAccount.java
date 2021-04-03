package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SupplierAccount implements DataObject {
    private int supplierID;
    private String username;
    private final int usernameLength = 20;
    private String password;
    private final int passwordLength = 20;
    private String name;
    private final int nameLength = 40;
    private final int recordLength = 324;
    public String dataFile = "FILE_DATA_SUPPLIERACCOUNTS";
    public SupplierAccount(int supplierID, String username, String password, String name) {
        this.supplierID = supplierID;
        this.username = username.substring(0, usernameLength);
        this.password = password.substring(0, passwordLength);
        this.name = name.substring(0, nameLength);
    }
    public SupplierAccount() { }
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
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(supplierID));
        serial.put(DataManager.encode(username, usernameLength));
        serial.put(DataManager.encode(password, passwordLength));
        serial.put(DataManager.encode(name, nameLength));
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
