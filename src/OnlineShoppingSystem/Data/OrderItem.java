package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderItem implements DataObject {
    // Attributes
    private int orderItemID;
    private int orderID;
    private int itemID;
    private int itemQty;
    private byte orderItemStatus;
    private final int recordLength = 17;
    public String dataFile = "FILE_DATA_ORDERITEMS";
    // Constructors
    public OrderItem(int orderID, int itemID, int itemQty, byte orderItemStatus) {
        orderItemID = 0;
        this.orderID = orderID;
        this.itemID = itemID;
        this.itemQty = itemQty;
        this.orderItemStatus = orderItemStatus;
    }
    public OrderItem(byte[] record) { fill(record); }
    public OrderItem() { }
    // Class-Specific Attribute Methods
    public int getOrderID() { return orderID; }
    public int getItemID() { return itemID; }
    public void setOrderItemStatus(byte orderItemStatus) { this.orderItemStatus = orderItemStatus; }
    // DataObject Method Overrides
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
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(orderItemID), i = 0, l = 4);
        serial.put(DataManager.encode(orderID), i += l, l);
        serial.put(DataManager.encode(itemID), i += l, l);
        serial.put(DataManager.encode(itemQty), i += l, l);
        serial.put(i += l, orderItemStatus);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, orderItemID); }
    public boolean delete() throws Exception { return DataManager.delete(this, orderItemID); }
    // Static Methods
    public static OrderItem getOrderItem(int orderItemID) throws Exception {
        OrderItem orderItem = null,
                temp = new OrderItem();
        ArrayList<byte[]> orderItems;
        int batchStart = 0,
            batchSize = 100;
        while (orderItem == null && (orderItems = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : orderItems) {
                temp.fill(record);
                if (temp.id() == orderItemID) {
                    orderItem = temp;
                    break;
                }
            }
        }
        return orderItem;
    }
    public static ArrayList<OrderItem> getOrderItems(int id, char mode) throws Exception {
        // modes: i (itemID), o (orderID)
        OrderItem temp = new OrderItem();
        ArrayList<OrderItem> items = new ArrayList<>();
        ArrayList<byte[]> records;
        int batchStart = 0,
            batchSize = 100;
        while ((records = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : records) {
                temp.fill(record);
                if (id == (mode == 'i' ? temp.getItemID() : temp.getOrderID())) {
                    items.add(new OrderItem(record));
                    break;
                }
            }
        }
        return items;
    }
}
