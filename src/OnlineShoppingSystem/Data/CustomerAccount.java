package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CustomerAccount implements DataObject {
    private int customerID;
    private String username;
    private final int usernameLength = 20;
    private String password;
    private final int passwordLength = 20;
    private String name;
    private final int nameLength = 40;
    private String address;
    private final int addressLength = 100;
    private String creditCard;
    private final int creditCardLength = 16;
    private boolean isPremium;
    private boolean premPaid;
    private final int recordLength = 790;
    public String dataFile = "FILE_DATA_CUSTOMERACCOUNTS";
    public CustomerAccount(int customerID, String username, String password, String name, String address, String creditCard, boolean isPremium, boolean premPaid) {
        this.customerID = customerID;
        this.username = username.substring(0, usernameLength);
        this.password = password.substring(0, passwordLength);
        this.name = name.substring(0, nameLength);
        this.address = address.substring(0, addressLength);
        this.creditCard = creditCard.substring(0, creditCardLength);
        this.isPremium = isPremium;
        this.premPaid = premPaid;
    }
    public CustomerAccount() { }
    public void fill(byte[] record) {
        int i = 0;
        customerID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        username = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += usernameLength * 4) - 1));
        password = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += passwordLength * 4) - 1));
        name = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += nameLength * 4) - 1));
        address = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += addressLength * 4) - 1));
        creditCard = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += creditCardLength * 4) - 1));
        isPremium = record[i++] != 0;
        premPaid = record[i] != 0;
    }
    public int id() { return customerID; }
    public void setID(int id) { customerID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(customerID));
        serial.put(DataManager.encode(username, usernameLength));
        serial.put(DataManager.encode(password, passwordLength));
        serial.put(DataManager.encode(name, nameLength));
        serial.put(DataManager.encode(address, addressLength));
        serial.put(DataManager.encode(creditCard, creditCardLength));
        serial.put(new byte[] { isPremium ? (byte)1 : 0 });
        serial.put(new byte[] { premPaid ? (byte)1 : 0 });
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
