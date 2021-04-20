package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
//item class
public class Item implements DataObject {
    // Attributes
    private int itemID;
    private int supplierID;
    private String itemName;
    private final int itemNameLength = 40;
    private String itemDesc;
    private final int itemDescLength = 100;
    private float itemRegCost;
    private float itemPremCost;
    private int itemQty;
    private int reservedQty;
    private final int recordLength = 584;
    public String dataFile = "FILE_DATA_ITEMS";
    // Constructors
    public Item(int supplierID, String itemName, String itemDesc, float itemRegCost, float itemPremCost, int itemQty) {
        itemID = 0;
        this.supplierID = supplierID;
        this.itemName = itemName.substring(0, Math.min(itemName.length(), itemNameLength));
        this.itemDesc = itemDesc.substring(0, Math.min(itemDesc.length(), itemDescLength));
        this.itemRegCost = itemRegCost;
        this.itemPremCost = itemPremCost;
        this.itemQty = itemQty;
        reservedQty = 0;
    }
    public Item(int id) throws Exception {
        byte[] data = DataManager.read(this, id);
        if (data != null) {
            fill(data);
            if (id() != id) {
                fill(new byte[recordLength()]);
            }
        }
    }
    public Item(byte[] record) { fill(record); }
    public Item() { }
    // Class-Specific Attribute Methods
    public int getSupplierID() { return supplierID; }
    public String getItemName() { return itemName; }
    public String getItemDesc() { return itemDesc; }
    public void setItemDesc(String itemDesc) { this.itemDesc = itemDesc.substring(0, Math.min(itemDesc.length(), itemDescLength)); }
    public float getItemRegCost() { return itemRegCost; }
    public void setItemRegCost(float itemRegCost) { this.itemRegCost = itemRegCost; }
    public float getItemPremCost() { return itemPremCost; }
    public void setItemPremCost(float itemPremCost) { this.itemPremCost = itemPremCost; }
    public int getItemQty() { return itemQty; }
    public void setItemQty(int itemQty) { this.itemQty = itemQty; }
    public int getReservedQty() { return reservedQty; }
    public void setReservedQty(int reservedQty) { this.reservedQty = reservedQty; }
    // DataObject Method Overrides
    public void fill(byte[] record) {
        int i = 0;
        itemID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        supplierID = DataManager.decodeInt(Arrays.copyOfRange(record, i, i += 4));
        itemName = DataManager.decodeString(Arrays.copyOfRange(record, i, i += itemNameLength * 4));
        itemDesc = DataManager.decodeString(Arrays.copyOfRange(record, i, i += itemDescLength * 4));
        itemRegCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i += 4));
        itemPremCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, i += 4));
        itemQty = DataManager.decodeInt(Arrays.copyOfRange(record, i, i + 4));
    }
    public int id() { return itemID; }
    public void setID(int id) { itemID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(itemID), 0, 4);
        serial.put(DataManager.encode(supplierID), 0, 4);
        serial.put(DataManager.encode(itemName, itemNameLength), 0, itemNameLength * 4);
        serial.put(DataManager.encode(itemDesc, itemDescLength), 0, itemDescLength * 4);
        serial.put(DataManager.encode(itemRegCost), 0, 4);
        serial.put(DataManager.encode(itemPremCost), 0, 4);
        serial.put(DataManager.encode(itemQty), 0, 4);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
    // DataObject-Dependent Methods
    public boolean create() throws Exception { return DataManager.create(this); }
    public boolean update() throws Exception { return DataManager.update(this, itemID); }
    public boolean delete() throws Exception { return DataManager.delete(this, itemID); }
    public String stringify() {
        return String.format(
            "Item ID: %d\n" +
            "Supplier ID ID: %d\n" +
            "Item Name: %s\n" +
            "Item Desc: %s\n" +
            "Item Reg Cost: %,.2f\n" +
            "Item Prem Cost: %,.2f\n" +
            "Item Qty: %d\n" +
            "Reserved Qty: %d\n",
            itemID,
            supplierID,
            itemName,
            itemDesc,
            itemRegCost,
            itemPremCost,
            itemQty,
            reservedQty);
    }
    // Static Methods
    public static void showAll() throws Exception {
        Item record = new Item();
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
    public static Item getItem(int itemID) throws Exception {
        Item item = null,
            temp = new Item();
        ArrayList<byte[]> items;
        int batchStart = 0,
            batchSize = 100;
        while (item == null && (items = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : items) {
                temp.fill(record);
                if (temp.id() == itemID) {
                    item = temp;
                    break;
                }
            }
        }
        return item;
    }
    public static ArrayList<Item> getItems(int supplierID) throws Exception {
        Item temp = new Item();
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<byte[]> records;
        int batchStart = 0,
            batchSize = 100;
        while ((records = DataManager.read(temp, batchStart, batchSize)).size() > 0) {
            batchStart += batchSize;
            for (byte[] record : records) {
                temp.fill(record);
                if (temp.getSupplierID() == supplierID) {
                    items.add(new Item(record));
                    break;
                }
            }
        }
        return items;
    }
}
