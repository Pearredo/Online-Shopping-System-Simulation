package OnlineShoppingSystem.Data;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BankRequest implements DataObject {
    // Attributes
    private byte action;
    private byte idType;
    private int accountID;
    private String creditCard;
    private final int creditCardLength = 16;
    private float balance;
    private final int recordLength = 74;
    public String dataFile = "";
    // Constructors
    public BankRequest(String creditCard, float balance) {
        this.action = 1;
        this.idType = 1;
        this.accountID = 0;
        this.creditCard = creditCard.substring(0, creditCardLength);
        this.balance = balance;
    }
    public BankRequest() { }
    // Class-Specific Attribute Methods

    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        action = record[i++];
        idType = record[i++];
        accountID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        creditCard = DataManager.decodeString(Arrays.copyOfRange(record, i, i += creditCardLength * 4));
        balance = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i + 4));
    }
    public int id() { return accountID; }
    public void setID(int id) { accountID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(0, action);
        serial.get();
        serial.put(1, idType);
        serial.get();
        serial.put(DataManager.encode(accountID), 0, 4);
        serial.put(DataManager.encode(creditCard, creditCardLength), 0, creditCardLength * 4);
        serial.put(DataManager.encode(balance), 0, 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    public String stringify() {
        return String.format(
            "Action: %d\n" +
            "ID Type: %d\n" +
            "Account ID: %d\n" +
            "Credit Card: %s\n" +
            "Balance: %,.2f\n",
            action,
            idType,
            accountID,
            creditCard,
            balance);
    }
    // DataObject-Dependent Methods
    public long execute() {
        return ByteBuffer.wrap(DataManager.getBuffer().send(this.serialize())).getLong();
    }
    // Static Methods

}
