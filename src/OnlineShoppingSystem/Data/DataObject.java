package OnlineShoppingSystem.Data;

public interface DataObject {
    byte[] serialize();
    String getDataFileName();
    String getDataType();
}
