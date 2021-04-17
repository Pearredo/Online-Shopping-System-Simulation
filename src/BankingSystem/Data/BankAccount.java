package BankingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class BankAccount implements DataObject {
    // Attributes
    private int accountID;
    private String creditCard;
    private final int creditCardLength = 16;
    private float balance;
    private final int recordLength = 72;
    public String dataFile = "FILE_DATA_BANKACCOUNTS";
    // Constructors
    public BankAccount(String creditCard, float balance) {
        accountID = 0;
        this.creditCard = creditCard.substring(0, Math.min(creditCard.length(), creditCardLength));
        this.balance = balance;
    }
    public BankAccount() {}
    // Class-Specific Attribute Methods
    public String getCreditCard() { return creditCard; }
    public float getBalance() { return balance; }
    public void addBalance(float amount) { balance += amount; }
    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        accountID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        creditCard = DataManager.decodeString(Arrays.copyOfRange(record, i, i += creditCardLength * 4));
        balance = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i + 4));
    }
    public int id() { return accountID; }
    public void setID(int id) { accountID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(accountID), 0, 4);
        serial.put(DataManager.encode(creditCard, creditCardLength), 0, creditCardLength * 4);
        serial.put(DataManager.encode(balance), 0, 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    public String stringify() {
        return String.format(
            "Account ID: %d\n" +
            "Credit Card: %s\n" +
            "Balance: %,.2f\n",
            accountID,
            creditCard,
            balance);
    }
    // DataObject-Dependent Methods

    // Static Methods
    public static void showAll() throws Exception {
        BankAccount record = new BankAccount();
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
}
