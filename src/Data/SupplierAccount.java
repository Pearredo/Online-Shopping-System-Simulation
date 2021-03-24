package Data;

public class SupplierAccount extends UserAccount implements DataObject {
    // item(s)
    // requests
    public byte[] serialize() {
        return new byte[0];
    }
    public String getDataFileName() { return "SupplierAccounts.dat"; }
    public String getDataType() { return "Supplier Account"; }
}
