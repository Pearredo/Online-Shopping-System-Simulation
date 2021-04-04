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
    // DataObject-Dependent Methods
    public long execute() throws Exception {
        boolean answered = false;
        long result = 0,
            fileID;
        String requestPath = ClassLoader.getSystemResource(DataManager.getDir("DIR_BANKREQUESTS")).getPath().substring(1);
        File requestDir = new File(requestPath),
            requestFile,
            replyFile;
        while (!(requestFile = new File(String.format("%s/%d.dat", requestPath, fileID = System.nanoTime()))).createNewFile()) {
            Thread.sleep(5); // Let the time stamp increment a bit and try again with a new file ID
        }
        RandomAccessFile requestWriter = new RandomAccessFile(requestFile, "rw");
        requestWriter.write(serialize());
        requestWriter.close();
        Thread.sleep(1000);
        while (!answered) {
            File[] files = requestDir.listFiles();
            if (files != null && files.length > 0)
            for (File file : files) {
                if (answered = file.getName().equalsIgnoreCase(String.format("ANSWER_%d.dat", fileID))) {
                    RandomAccessFile requestReader = new RandomAccessFile(file, "r");
                    result = requestReader.readLong();
                    requestReader.close();
                    file.delete();
                    break;
                }
            }
        }
        return result;
    }
    // Static Methods

}
