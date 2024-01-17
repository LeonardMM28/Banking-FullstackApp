package dto;

public enum TransactionType {
    Deposit("Deposit"),
    Transfer("Transfer"),
    Withdraw("Withdraw"),
    Buy("Buy"),
    Sell("Sell");

    private final String stringValue;

    TransactionType(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
