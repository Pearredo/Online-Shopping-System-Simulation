package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Order implements DataObject {
    // Attributes
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
    // Constructors
    public Order(int customerID, byte orderStatus, int orderDate, float orderCost, byte orderDelivery) {
        orderID = 0;
        this.customerID = customerID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderCost = orderCost;
        this.orderDelivery = orderDelivery;
        invoiceID = 0;
        purchaseAuth = 0;
    }
    public Order(byte[] record) { fill(record); }
    public Order() { }
    // Class-Specific Attribute Methods
    public int getCustomerID() { return customerID; }
    public byte getOrderStatus() { return orderStatus; }
    public void setOrderStatus(byte orderStatus) { this.orderStatus = orderStatus; }
    public int getOrderDate() { return orderDate; }
    public void setOrderDate(int orderDate) { this.orderDate = orderDate; }
    public float getOrderCost() { return orderCost; }
    public void setOrderCost(float orderCost) { this.orderCost = orderCost; }
    public int getInvoiceID() { return invoiceID; }
    public long getPurchaseAuth() { return purchaseAuth; }
    // DataObject Method Overrides
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
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, orderID); }
    public boolean delete() throws Exception { return DataManager.delete(this, orderID); }
    public boolean purchase(String creditCard) throws Exception {
        BankRequest request = new BankRequest(creditCard, orderCost);
        return (invoiceID = (purchaseAuth = request.execute()) > 0 ? orderID : 0) > 0;
    }
    // Static Methods
    public static Order getOrder(int orderID) throws Exception {
        Order order = null,
            temp = new Order();
        ArrayList<byte[]> orders;
        int batchStart = 0,
            batchSize = 100;
        while (order == null && (orders = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : orders) {
                temp.fill(record);
                if (temp.id() == orderID) {
                    order = temp;
                    break;
                }
            }
        }
        return order;
    }
    public static ArrayList<Order> getOrders(int customerID) throws Exception {
        Order temp = new Order();
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<byte[]> records;
        int batchStart = 0,
            batchSize = 100;
        while ((records = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : records) {
                temp.fill(record);
                if (temp.getCustomerID() == customerID) {
                    orders.add(new Order(record));
                    break;
                }
            }
        }
        return orders;
    }
}
