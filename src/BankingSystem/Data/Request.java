package BankingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Request implements DataObject {
    private byte action;
    private byte idType;
    private int accountID;
    private String creditCard;
    private final int creditCardLength = 16;
    private float balance;
    private final int recordLength = 74;
    public String dataFile = "";
    public Request(String action, String idType, int accountID, String creditCard, float balance) {
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
    public Request() { }
    public void fill(byte[] record) {
        int i = 0;
        action = record[i++];
        idType = record[i++];
        accountID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        creditCard = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += creditCardLength * 4) - 1));
        balance = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i + 3));
    }
    public int id() { return accountID; }
    public void setID(int id) { accountID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(i = 0, action);
        serial.put(++i, idType);
        serial.put(DataManager.encode(accountID), ++i, l = 4);
        serial.put(DataManager.encode(creditCard, creditCardLength), i += l, l = creditCardLength * 4);
        serial.put(DataManager.encode(balance), i + l, 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    public byte[] execute() throws Exception {
        byte[] result = new byte[8],
            transID = ByteBuffer.allocate(8).putLong(System.nanoTime()).array();
        BankAccount bankAccount = new BankAccount();
        if (idType == 1) {
            accountID = 0;
            BankAccount temp = new BankAccount();
            ArrayList<byte[]> accounts;
            int batchStart = 0,
                batchSize = 100;
            while (accountID < 1 && (accounts = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
                batchStart += batchSize;
                for (byte[] account : accounts) {
                    temp.fill(account);
                    if (temp.getCreditCard().equals(creditCard)) {
                        accountID = temp.id();
                        break;
                    }
                }
            }
        }
        if (accountID > 0) {
            switch (action) {
                case 0: { // Get
                    byte[] data = DataManager.read(bankAccount, accountID);
                    if (data != null) {
                        bankAccount.fill(data);
                        if (bankAccount.id() == accountID) {
                            for (int i = 0; i < 8; i++) result[i] |= transID[i];
                        }
                    }
                } break;
                case 1: { // Put
                    byte[] data = DataManager.read(bankAccount, accountID);
                    if (data == null && balance > 0 && DataManager.create(new BankAccount(-1, creditCard, balance))) {
                        for (int i = 0; i < 8; i++) result[i] |= transID[i];
                    } else if (data != null) {
                        bankAccount.fill(data);
                        if (balance > 0 || bankAccount.getBalance() > balance) {
                            bankAccount.addBalance(balance);
                            if (DataManager.update(bankAccount, accountID)) {
                                for (int i = 0; i < 8; i++) result[i] |= transID[i];
                            }
                        }
                    }
                } break;
                case 2: { // Del
                    if (DataManager.delete(bankAccount, accountID)) {
                        for (int i = 0; i < 8; i++) result[i] |= transID[i];
                    }
                } break;
                case 3: { // Chk
                    byte[] data = DataManager.read(bankAccount, accountID);
                    if (data != null) {
                        bankAccount.fill(data);
                        if (bankAccount.id() == accountID && bankAccount.getBalance() > -balance) {
                            for (int i = 0; i < 8; i++) result[i] |= transID[i];
                        }
                    }
                } break;
            }
        }
        return result;
    }
}
