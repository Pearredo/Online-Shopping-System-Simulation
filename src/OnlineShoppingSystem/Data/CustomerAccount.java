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
        this.username = username.substring(0, Math.min(username.length(), usernameLength));
        this.password = password.substring(0, Math.min(password.length(), passwordLength));
        this.name = name.substring(0, Math.min(name.length(), nameLength));
        this.address = address.substring(0, Math.min(address.length(), addressLength));
        this.creditCard = creditCard.substring(0, Math.min(creditCard.length(), creditCardLength));
        this.isPremium = isPremium;
        this.premPaid = premPaid;
    }
    public CustomerAccount(int id) throws Exception {
        byte[] data = DataManager.read(this, id);
        if (data != null) {
            fill(data);
            if (id() != id) {
                fill(new byte[recordLength()]);
            }
        }
    }
    public CustomerAccount() { }
    // Class-Specific Attribute Methods
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password.substring(0, Math.min(password.length(), passwordLength)); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name.substring(0, Math.min(name.length(), nameLength)); }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address.substring(0, Math.min(address.length(), addressLength)); }
    public String getCreditCard() { return creditCard; }
    public void setCreditCard(String creditCard) { this.creditCard = creditCard.substring(0, Math.min(creditCard.length(), creditCardLength)); }
    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean isPremium) { this.isPremium = isPremium; }
    public boolean premPaid() { return premPaid; }
    public void setPremPaid(boolean premPaid) { this.premPaid = premPaid; }
    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        customerID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        username = DataManager.decodeString(Arrays.copyOfRange(record, i, i += usernameLength * 4));
        password = DataManager.decodeString(Arrays.copyOfRange(record, i, i += passwordLength * 4));
        name = DataManager.decodeString(Arrays.copyOfRange(record, i, i += nameLength * 4));
        address = DataManager.decodeString(Arrays.copyOfRange(record, i, i += addressLength * 4));
        creditCard = DataManager.decodeString(Arrays.copyOfRange(record, i, i += creditCardLength * 4));
        isPremium = record[i++] != 0;
        premPaid = record[i] != 0;
    }
    public int id() { return customerID; }
    public void setID(int id) { customerID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(customerID), 0, 4);
        serial.put(DataManager.encode(username, usernameLength), 0, usernameLength * 4);
        serial.put(DataManager.encode(password, passwordLength), 0, passwordLength * 4);
        serial.put(DataManager.encode(name, nameLength), 0, nameLength * 4);
        serial.put(DataManager.encode(address, addressLength), 0, addressLength * 4);
        serial.put(DataManager.encode(creditCard, creditCardLength), 0, creditCardLength * 4);
        serial.put(recordLength - 2, isPremium ? (byte)1 : 0);
        serial.put(recordLength - 1, premPaid ? (byte)1 : 0);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    public String stringify() {
        return String.format(
            "Customer ID: %d\n" +
            "Username: %s\n" +
            "Password: %s\n" +
            "Name: %s\n" +
            "Address: %s\n" +
            "Credit Card: %s\n" +
            "Is Premium: %s\n" +
            "Premium Paid?: %s\n",
            customerID,
            username,
            password,
            name,
            address,
            creditCard,
            isPremium ? "Yes" : "No",
            premPaid ? "Yes" : "No");
    }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, customerID); }
    public boolean delete() throws Exception { return DataManager.delete(this, customerID); }
    // Static Methods
    public static void showAll() throws Exception {
        CustomerAccount record = new CustomerAccount();
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
