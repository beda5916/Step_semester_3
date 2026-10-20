import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * Problem 1: Bank Account Withdrawal System
 * ============================================================================
 * Design a system for bank accounts with withdrawal functionality.
 * There are two types of accounts: Savings and Current.
 *
 * Rules:
 * - A Savings account must maintain a minimum balance of 1000 after any withdrawal.
 * - A Current account can have an overdraft up to 5000 (balance can go down to -5000).
 * - Withdrawals that violate these rules are rejected with a reason.
 * - If an account is not found, print "Account not found".
 * - Assume initial balances are always positive.
 *
 * OOP Concepts Demonstrated:
 * - Abstract Classes and Methods (BankAccount with abstract withdraw)
 * - Inheritance (SavingsAccount, CurrentAccount extend BankAccount)
 * - Method Overriding (each subclass overrides withdraw with its own rules)
 * - Encapsulation (private fields, public getters)
 * - Polymorphism (HashMap stores BankAccount references for both types)
 *
 * Complexity Analysis:
 * --------------------
 * - Account creation: O(1) per operation (HashMap put).
 * - Withdrawal lookup: O(1) average (HashMap get).
 * - Overall for n operations: O(n).
 * - Space Complexity: O(k) where k = number of accounts stored in the map.
 * ============================================================================
 */
public class Q1_BankAccountWithdrawalSystem {

    // ==================== Abstract Base Class ====================

    /**
     * Abstract base class representing a generic bank account.
     * Encapsulates common fields (accountId, balance) and declares
     * an abstract withdraw method to be implemented by subclasses.
     */
    static abstract class BankAccount {
        private final String accountId;
        private double balance;

        public BankAccount(String accountId, double initialBalance) {
            this.accountId = accountId;
            this.balance = initialBalance;
        }

        public String getAccountId() {
            return accountId;
        }

        public double getBalance() {
            return balance;
        }

        protected void setBalance(double balance) {
            this.balance = balance;
        }

        /**
         * Attempts to withdraw the given amount.
         * Returns a status message indicating success or rejection reason.
         *
         * @param amount The amount to withdraw (must be positive).
         * @return Status string with result of the withdrawal attempt.
         */
        public abstract String withdraw(double amount);
    }

    // ==================== Savings Account ====================

    /**
     * Savings account enforces a minimum balance of 1000 after any withdrawal.
     */
    static class SavingsAccount extends BankAccount {
        private static final double MIN_BALANCE = 1000;

        public SavingsAccount(String accountId, double initialBalance) {
            super(accountId, initialBalance);
        }

        @Override
        public String withdraw(double amount) {
            double balanceAfterWithdrawal = getBalance() - amount;

            if (balanceAfterWithdrawal < MIN_BALANCE) {
                return getAccountId() + " rejected: minimum balance " + (int) MIN_BALANCE;
            }

            setBalance(balanceAfterWithdrawal);
            return getAccountId() + " balance " + (int) getBalance();
        }
    }

    // ==================== Current Account ====================

    /**
     * Current account allows overdraft up to 5000 (balance can go down to -5000).
     */
    static class CurrentAccount extends BankAccount {
        private static final double OVERDRAFT_LIMIT = 5000;

        public CurrentAccount(String accountId, double initialBalance) {
            super(accountId, initialBalance);
        }

        @Override
        public String withdraw(double amount) {
            double balanceAfterWithdrawal = getBalance() - amount;

            if (balanceAfterWithdrawal < -OVERDRAFT_LIMIT) {
                return getAccountId() + " rejected: overdraft limit " + (int) OVERDRAFT_LIMIT;
            }

            setBalance(balanceAfterWithdrawal);
            return getAccountId() + " balance " + (int) getBalance();
        }
    }

    // ==================== System Processor ====================

    /**
     * Processes a series of account operations and returns the output for each
     * WITHDRAW operation.
     *
     * @param operations Array of operation strings to process.
     * @return Array of output strings (one per WITHDRAW operation).
     */
    public static String[] processOperations(String[] operations) {
        Map<String, BankAccount> accounts = new HashMap<>();
        StringBuilder output = new StringBuilder();

        for (String op : operations) {
            String[] parts = op.split(" ");
            String command = parts[0];

            switch (command) {
                case "Savings":
                    // Savings <ID> <InitialBalance>
                    accounts.put(parts[1], new SavingsAccount(parts[1], Double.parseDouble(parts[2])));
                    break;

                case "Current":
                    // Current <ID> <InitialBalance>
                    accounts.put(parts[1], new CurrentAccount(parts[1], Double.parseDouble(parts[2])));
                    break;

                case "WITHDRAW":
                    // WITHDRAW <ID> <Amount>
                    String id = parts[1];
                    double amount = Double.parseDouble(parts[2]);

                    if (!accounts.containsKey(id)) {
                        output.append("Account not found").append("\n");
                    } else {
                        BankAccount account = accounts.get(id);
                        output.append(account.withdraw(amount)).append("\n");
                    }
                    break;
            }
        }

        // Split output into individual lines, removing trailing empty string
        String result = output.toString().trim();
        return result.isEmpty() ? new String[0] : result.split("\n");
    }

    // ==================== Helper Method ====================

    /**
     * Runs a test case and prints formatted results.
     */
    private static void runTestCase(String testName, String[] operations, String[] expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("Operations:");
        for (String op : operations) {
            System.out.println("  > " + op);
        }

        String[] actual = processOperations(operations);

        System.out.println("Expected Output:");
        for (String line : expected) {
            System.out.println("  " + line);
        }
        System.out.println("Actual Output:");
        for (String line : actual) {
            System.out.println("  " + line);
        }

        boolean passed = expected.length == actual.length;
        if (passed) {
            for (int i = 0; i < expected.length; i++) {
                if (!expected[i].equals(actual[i])) {
                    passed = false;
                    break;
                }
            }
        }
        System.out.println("Status: " + (passed ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("        BANK ACCOUNT WITHDRAWAL SYSTEM - TEST SUITE       ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        runTestCase("Sample Problem",
            new String[]{
                "Savings S1 5000",
                "WITHDRAW S1 4500",
                "Current C1 2000",
                "WITHDRAW C1 6000",
                "WITHDRAW S1 3000",
                "WITHDRAW C1 2000"
            },
            new String[]{
                "S1 rejected: minimum balance 1000",
                "C1 balance -4000",
                "S1 balance 2000",
                "C1 rejected: overdraft limit 5000"
            }
        );

        // Test Case 2: Savings account — exact minimum balance withdrawal
        // Balance = 5000, Withdraw 4000 -> 1000 remaining (exactly MIN_BALANCE, allowed)
        runTestCase("Savings - Exact Minimum Balance Boundary",
            new String[]{
                "Savings S2 5000",
                "WITHDRAW S2 4000"
            },
            new String[]{
                "S2 balance 1000"
            }
        );

        // Test Case 3: Current account — exact overdraft limit
        // Balance = 1000, Withdraw 6000 -> -5000 (exactly -OVERDRAFT_LIMIT, allowed)
        runTestCase("Current - Exact Overdraft Limit Boundary",
            new String[]{
                "Current C2 1000",
                "WITHDRAW C2 6000"
            },
            new String[]{
                "C2 balance -5000"
            }
        );

        // Test Case 4: Account not found
        runTestCase("Account Not Found",
            new String[]{
                "WITHDRAW X1 500"
            },
            new String[]{
                "Account not found"
            }
        );

        // Test Case 5: Multiple successful withdrawals
        runTestCase("Multiple Successive Withdrawals",
            new String[]{
                "Savings S3 10000",
                "WITHDRAW S3 2000",
                "WITHDRAW S3 3000",
                "WITHDRAW S3 4500"
            },
            new String[]{
                "S3 balance 8000",
                "S3 balance 5000",
                "S3 rejected: minimum balance 1000"
            }
        );
    }
}
