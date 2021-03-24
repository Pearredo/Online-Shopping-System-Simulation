package Data;

public class Order implements DataObject {
    // item(s)
    public byte[] serialize() {
        return new byte[0];
    }
    public String getDataFileName() { return "Orders.dat"; }
    public String getDataType() { return "Order"; }
}
