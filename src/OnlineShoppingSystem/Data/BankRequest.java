package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BankRequest implements DataObject {
    private byte action;
    private byte idType;
    private int accountID;
    private String creditCard;
    private final int creditCardLength = 16;
    private float balance;
    private final int recordLength = 74;
    public String dataFile = "";
    public BankRequest(String action, String idType, int accountID, String creditCard, float balance) {
        switch (action.toLowerCase()) {
            case "g":
            case "get":
                this.action = 0;
                break;
            case "p":
            case "put":
                this.action = 1;
                break;
            case "d":
            case "del":
                this.action = 2;
                break;
        }
        switch (idType.toLowerCase()) {
            case "a":
            case "account":
            case "accountid":
                this.idType = 0;
                break;
            case "c":
            case "cc":
            case "creditcard":
                this.idType = 1;
        }
        this.accountID = accountID;
        this.creditCard = creditCard.substring(0, creditCardLength);
        this.balance = balance;
    }
    public BankRequest() { }
    public void fill(byte[] record) {
        int i = 0;
        action = record[i++];
        idType = record[i++];
        accountID = BankingSystem.Data.DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        creditCard = BankingSystem.Data.DataManager.decodeString(Arrays.copyOfRange(record, i, (i += creditCardLength * 4) - 1));
        balance = BankingSystem.Data.DataManager.decodeFloat(Arrays.copyOfRange(record, i, i + 3));
    }
    public int id() { return accountID; }
    public void setID(int id) { accountID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(i = 0, action);
        serial.put(++i, idType);
        serial.put(BankingSystem.Data.DataManager.encode(accountID), ++i, l = 4);
        serial.put(BankingSystem.Data.DataManager.encode(creditCard, creditCardLength), i += l, l = creditCardLength * 4);
        serial.put(BankingSystem.Data.DataManager.encode(balance), i + l, 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
