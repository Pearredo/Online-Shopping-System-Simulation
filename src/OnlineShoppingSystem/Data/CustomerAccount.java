package OnlineShoppingSystem.Data;

public class CustomerAccount extends UserAccount implements DataObject {
    // orders
    // invoices
    // creditCardNumber? Maybe we cna have autocomplete
    public byte[] serialize() {
        byte[] userAccountSerial = super.serialize();
        byte[] customerAccountSerial = new byte[0];
        byte[] newSerial = new byte[userAccountSerial.length + customerAccountSerial.length];
        System.arraycopy(userAccountSerial, 0, newSerial, 0, userAccountSerial.length);
        System.arraycopy(customerAccountSerial, 0, newSerial, userAccountSerial.length, customerAccountSerial.length);
        return newSerial;
    }
    public String getDataFileName() { return "CustomerAccounts.dat"; }
    public String getDataType() { return "Customer Account"; }
}
