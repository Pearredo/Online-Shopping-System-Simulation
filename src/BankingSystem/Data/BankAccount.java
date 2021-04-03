package BankingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BankAccount implements DataObject {
    private int accountID;
    private String creditCard;
    private final int creditCardLength = 16;
    private float balance;
    private final int recordLength = 72;
    public String dataFile = "FILE_DATA_BANKACCOUNTS";
    public BankAccount(int accountID, String creditCard, float balance) {
        this.accountID = accountID;
        this.creditCard = creditCard.substring(0, creditCardLength);
        this.balance = balance;
    }
    public BankAccount() {}
    public String getCreditCard() { return creditCard; }
    public double getBalance() { return balance; }
    public void addBalance(double amount) { balance += amount; }
    public void fill(byte[] record) {
        int i = 0;
        accountID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        creditCard = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += creditCardLength * 4) - 1));
        balance = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i + 3));
    }
    public int id() { return accountID; }
    public void setID(int id) { accountID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(accountID));
        serial.put(DataManager.encode(creditCard, creditCardLength));
        serial.put(DataManager.encode(balance));
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
