package Data;

public class Item implements DataObject {
    // itemID
    // itemName
    // itemDesc
    // itemQty
    public byte[] serialize() {
        return new byte[0];
    }
    public String getDataFileName() { return "Items.dat"; }
    public String getDataType() { return "Item"; }
}
