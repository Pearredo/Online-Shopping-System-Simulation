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
    private float premCost;
    private byte orderDelivery;
    private float deliveryCost;
    private int invoiceID;
    private long purchaseAuth;
    private final int recordLength = 38;
    public String dataFile = "FILE_DATA_ORDERS";
    // Constructors
    public Order(int customerID, byte orderStatus, int orderDate, float orderCost, float premCost, byte orderDelivery, float deliveryCost) {
        orderID = 0;
        this.customerID = customerID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderCost = orderCost;
        this.premCost = premCost;
        this.orderDelivery = orderDelivery;
        this.deliveryCost = deliveryCost;
        invoiceID = 0;
        purchaseAuth = 0;
    }
    public Order(int id) throws Exception {
        byte[] data = DataManager.read(this, id);
        if (data != null) {
            fill(data);
            if (id() != id) {
                fill(new byte[recordLength()]);
            }
        }
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
    public float getPremCost() { return premCost; }
    public float getOrderDelivery() { return orderDelivery; }
    public float getDeliveryCost() { return deliveryCost; }
    public int getInvoiceID() { return invoiceID; }
    public long getPurchaseAuth() { return purchaseAuth; }
    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        orderID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        customerID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        orderStatus = record[i++];
        orderDate = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        orderCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i += 4));
        premCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i += 4));
        orderDelivery = record[i++];
        deliveryCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i += 4));
        invoiceID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        purchaseAuth = DataManager.decodeLong(Arrays.copyOfRange(record, i, i + 8));
    }
    public int id() { return orderID; }
    public void setID(int id) { orderID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(orderID), 0, 4);
        serial.put(DataManager.encode(customerID), 0, 4);
        serial.put(8, orderStatus);
        serial.get();
        serial.put(DataManager.encode(orderDate), 0, 4);
        serial.put(DataManager.encode(orderCost), 0, 4);
        serial.put(DataManager.encode(premCost), 0, 4);
        serial.put(17, orderDelivery);
        serial.get();
        serial.put(DataManager.encode(deliveryCost), 0, 4);
        serial.put(DataManager.encode(invoiceID), 0, 4);
        serial.put(DataManager.encode(purchaseAuth), 0, 8);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    public String stringify() {
        return String.format(
            "Order ID: %d\n" +
            "Customer ID ID: %d\n" +
            "Order Status: %d\n" +
            "Order Date: %d\n" +
            "Order Cost: %,.2f\n" +
            "Prem Cost: %,.2f\n" +
            "Order Delivery: %d\n" +
            "Delivery Cost: %,.2f\n" +
            "Invoice ID: %d\n" +
            "Purchase Auth.: %d\n",
            orderID,
            customerID,
            orderStatus,
            orderDate,
            orderCost,
            premCost,
            orderDelivery,
            deliveryCost,
            invoiceID,
            purchaseAuth);
    }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, orderID); }
    public boolean delete() throws Exception { return DataManager.delete(this, orderID); }
    public boolean purchase(String creditCard) throws Exception {
        BankRequest request = new BankRequest(creditCard, -orderCost);
        return (invoiceID = (purchaseAuth = request.execute()) > 0 ? orderID : 0) > 0;
    }
    // Static Methods
    public static void showAll() throws Exception {
        Order record = new Order();
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
