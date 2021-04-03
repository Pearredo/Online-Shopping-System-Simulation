package OnlineShoppingSystem.Data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Item implements DataObject {
    private int itemID;
    private int supplierID;
    private String itemName;
    private final int itemNameLength = 40;
    private String itemDesc;
    private final int itemDescLength = 100;
    private float itemRegCost;
    private float itemPremCost;
    private int itemQty;
    private final int recordLength = 580;
    public String dataFile = "FILE_DATA_ITEMS";
    public Item(int itemID, int supplierID, String itemName, String itemDesc, float itemRegCost, float itemPremCost, int itemQty) {
        this.itemID = itemID;
        this.supplierID = supplierID;
        this.itemName = itemName.substring(0, itemNameLength);
        this.itemDesc = itemDesc.substring(0, itemDescLength);
        this.itemRegCost = itemRegCost;
        this.itemPremCost = itemPremCost;
        this.itemQty = itemQty;
    }
    public Item() { }
    public void fill(byte[] record) {
        int i = 0;
        itemID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        supplierID = DataManager.decodeInt(Arrays.copyOfRange(record, i, (i += 4) - 1));
        itemName = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += itemNameLength * 4) - 1));
        itemDesc = DataManager.decodeString(Arrays.copyOfRange(record, i, (i += itemDescLength * 4) - 1));
        itemRegCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, (i += 4) - 1));
        itemPremCost = DataManager.decodeFloat(Arrays.copyOfRange(record, i, (i += 4) - 1));
        itemQty = DataManager.decodeInt(Arrays.copyOfRange(record, i, i + 3));
    }
    public int id() { return itemID; }
    public void setID(int id) { itemID = id; }
    public int recordLength() { return recordLength; }
    public byte[] serialize() {
        int i, l;
        ByteBuffer serial = ByteBuffer.allocate(recordLength);
        serial.put(DataManager.encode(itemID), i = 0, l = 4);
        serial.put(DataManager.encode(supplierID), i += l, l);
        serial.put(DataManager.encode(itemName, itemNameLength), i += l, l = itemNameLength * 4);
        serial.put(DataManager.encode(itemDesc, itemDescLength), i += l, l = itemDescLength * 4);
        serial.put(DataManager.encode(itemRegCost), i += l, l = 4);
        serial.put(DataManager.encode(itemPremCost), i += l, l);
        serial.put(DataManager.encode(itemQty), i + l, l);
        return serial.array();
    }
    public String dataFile() { return dataFile; }
}
