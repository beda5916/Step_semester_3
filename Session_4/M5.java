class FeeAccount {

    String accountId;

    FeeAccount(String accountId) {
        this.accountId = accountId;
    }

    void processPayment(double amount) {
        System.out.println("Paid in one go (day-scholar account)");
    }
}

class HostelFeeAccount extends FeeAccount {

    HostelFeeAccount(String accountId) {
        super(accountId);
    }

    void processPayment(double amount) {
        System.out.println("Paid in two installments (hostel account)");
    }
}

public class M5 {

    static void processPayment(FeeAccount account, double amount) {

        if (account instanceof HostelFeeAccount) {
            account.processPayment(amount);
        } else {
            account.processPayment(amount);
        }
    }

    public static void main(String[] args) {

        FeeAccount[] accounts = {
            new HostelFeeAccount("H001"),
            new HostelFeeAccount("H002"),
            new FeeAccount("F001"),
            new FeeAccount("F002")
        };

        double amount = 60000;

        int hostelCount = 0;
        int dayScholarCount = 0;

        for (int i = 0; i < accounts.length; i++) {

            processPayment(accounts[i], amount);

            if (accounts[i] instanceof HostelFeeAccount) {
                hostelCount++;
            } else {
                dayScholarCount++;
            }
        }

        System.out.println(
            "Hostel accounts processed: " + hostelCount +
            " | Day-scholar accounts processed: " + dayScholarCount
        );
    }
}