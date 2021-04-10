package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomerAccount implements DataObject {
    // Attributes
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
    // Constructors
    public CustomerAccount(String username, String password, String name, String address, String creditCard, boolean isPremium, boolean premPaid) {
        customerID = 0;
        this.username = username.substring(0, usernameLength);
        this.password = password.substring(0, passwordLength);
        this.name = name.substring(0, nameLength);
        this.address = address.substring(0, addressLength);
        this.creditCard = creditCard.substring(0, creditCardLength);
        this.isPremium = isPremium;
        this.premPaid = premPaid;
    }
    public CustomerAccount() { }
    // Class-Specific Attribute Methods
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCreditCard() { return creditCard; }
    public void setCreditCard(String creditCard) { this.creditCard = creditCard; }
    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean isPremium) { this.isPremium = isPremium; }
    public boolean premPaid() { return premPaid; }
    public void setPremPaid(boolean premPaid) { this.premPaid = premPaid; }
    // DataObject Method Overrides
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
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(customerID), i = 0, l = 4);
        serial.put(DataManager.encode(username, usernameLength), i += l, l = usernameLength * 4);
        serial.put(DataManager.encode(password, passwordLength), i += l, l = passwordLength * 4);
        serial.put(DataManager.encode(name, nameLength), i += l, l = nameLength * 4);
        serial.put(DataManager.encode(address, addressLength), i += l, l = addressLength * 4);
        serial.put(DataManager.encode(creditCard, creditCardLength), i += l, l = creditCardLength * 4);
        serial.put(i += l, isPremium ? (byte)1 : 0);
        serial.put(++i, premPaid ? (byte)1 : 0);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, customerID); }
    public boolean delete() throws Exception { return DataManager.delete(this, customerID); }
    // Static Methods
    public static CustomerAccount Login(String username, String password) throws Exception {
        boolean exists = false;
        CustomerAccount account = null,
            temp = new CustomerAccount();
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
                        account = new CustomerAccount(username, password, "", "", "", false, false);
                    }
                    break;
                }
            }
        }
        return account;
    }
}
