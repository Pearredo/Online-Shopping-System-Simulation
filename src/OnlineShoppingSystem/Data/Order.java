package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Order implements DataObject {
    private int orderID;
    private int customerID;
    private byte orderStatus;
    private int orderDate; // yyyyMMdd
    private float orderCost;
    private byte orderDelivery;
    private int invoiceID;
    private long purchaseAuth;
    private final int recordLength = 30;
    public String dataFile = "FILE_DATA_ORDERS";
    public Order(int orderID, int customerID, byte orderStatus, int orderDate, float orderCost, byte orderDelivery, int invoiceID, long purchaseAuth) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderCost = orderCost;
        this.orderDelivery = orderDelivery;
        this.invoiceID = invoiceID;
        this.purchaseAuth = purchaseAuth;
    }
    public Order() { }
    public void fill(byte[] record) {
        int i = 0;
        orderID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        customerID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        orderStatus = record[i++];
        orderDate = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        orderCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, (i += 4) - 1));
        orderDelivery = record[i++];
        invoiceID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        purchaseAuth = DataManager.decodeLong(Arrays.copyOfRange(record, i, i + 7));
    }
    public int id() { return orderID; }
    public void setID(int id) { orderID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(orderID), i = 0, l = 4);
        serial.put(DataManager.encode(customerID), i += l, l);
        serial.put(i += l, orderStatus);
        serial.put(DataManager.encode(orderDate), ++i, l);
        serial.put(DataManager.encode(orderCost), i += l, l);
        serial.put(i += l, orderDelivery);
        serial.put(DataManager.encode(invoiceID), ++i, l);
        serial.put(DataManager.encode(purchaseAuth), i + l, 8);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
