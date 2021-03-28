package BankingSystem.Data;

public class BankAccount extends UserAccount implements DataObject {
    // creditCardNumber
    // balance
    public byte[] serialize() {
        return new byte[0];
    }
    public String getDataFileName() { return "BankAccounts.dat"; }
    public String getDataType() { return "Bank Account"; }
}
