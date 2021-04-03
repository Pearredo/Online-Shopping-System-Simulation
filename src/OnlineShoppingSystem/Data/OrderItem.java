package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class OrderItem implements DataObject {
    private int orderItemID;
    private int orderID;
    private int itemID;
    private int itemQty;
    private byte orderItemStatus;
    private int recordLength = 17;
    public String dataFile = "FILE_DATA_ORDERITEMS";
    public OrderItem(int orderItemID, int orderID, int itemID, int itemQty, byte orderItemStatus) {
        this.orderItemID = orderItemID;
        this.orderID = orderID;
        this.itemID = itemID;
        this.itemQty =itemQty;
        this.orderItemStatus = orderItemStatus;
    }
    public OrderItem() { }
    public void fill(byte[] record) {
        int i = 0;
        orderItemID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        orderID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        itemID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        itemQty = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        orderItemStatus = record[i];
    }
    public int id() { return orderItemID; }
    public void setID(int id) { orderItemID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(orderItemID));
        serial.put(DataManager.encode(orderID));
        serial.put(DataManager.encode(itemID));
        serial.put(DataManager.encode(itemQty));
        serial.put(new byte[] { orderItemStatus });
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
