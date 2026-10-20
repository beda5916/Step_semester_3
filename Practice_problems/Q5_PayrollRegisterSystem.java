/**
 * ============================================================================
 * Problem 5: Payroll Register System
 * ============================================================================
 * Develop a payroll system for different staff types: FullTime, PartTime, and Intern.
 *
 * Pay Calculation Rules:
 * - FullTime: A fixed monthly salary.
 * - PartTime: Hours worked multiplied by an hourly rate.
 * - Intern: A fixed stipend.
 *
 * Requirements:
 * - Store all staff members' payslips in an array.
 * - Each payslip should have a readable text representation showing name, type, pay.
 * - After processing, print each payslip, the total pay, and the top earner.
 *
 * OOP Concepts Demonstrated:
 * - Abstract Class (Staff) with abstract calculatePay() method
 * - Inheritance (FullTime, PartTime, Intern extend Staff)
 * - Method Overriding (each subclass overrides calculatePay and getType)
 * - Polymorphism (Staff[] array stores all types, toString() for payslip)
 * - Encapsulation (private fields, computed pay via methods)
 *
 * Complexity Analysis:
 * --------------------
 * - Building staff array: O(n) where n = number of staff members.
 * - Printing payslips: O(n) — iterate through array, O(1) per payslip.
 * - Calculating total and finding top earner: O(n) — single pass.
 * - Overall Time Complexity: O(n).
 * - Space Complexity: O(n) for storing the staff array.
 * ============================================================================
 */
public class Q5_PayrollRegisterSystem {

    // ==================== Abstract Base Class ====================

    /**
     * Abstract base class representing a staff member.
     * Each subclass defines how pay is calculated via calculatePay().
     */
    static abstract class Staff {
        private final String name;

        public Staff(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * Returns the type name of the staff (e.g., "FullTime", "PartTime", "Intern").
         */
        public abstract String getType();

        /**
         * Calculates and returns the pay for this staff member.
         */
        public abstract int calculatePay();

        /**
         * Returns a payslip string in the format:
         * Payslip[name=<Name>, type=<Type>, pay=<CalculatedPay>]
         */
        @Override
        public String toString() {
            return "Payslip[name=" + name + ", type=" + getType() + ", pay=" + calculatePay() + "]";
        }
    }

    // ==================== FullTime Staff ====================

    /**
     * Full-time staff member with a fixed monthly salary.
     */
    static class FullTime extends Staff {
        private final int salary;

        public FullTime(String name, int salary) {
            super(name);
            this.salary = salary;
        }

        @Override
        public String getType() {
            return "FullTime";
        }

        @Override
        public int calculatePay() {
            return salary;
        }
    }

    // ==================== PartTime Staff ====================

    /**
     * Part-time staff member paid by hours worked * hourly rate.
     */
    static class PartTime extends Staff {
        private final int hoursWorked;
        private final int hourlyRate;

        public PartTime(String name, int hoursWorked, int hourlyRate) {
            super(name);
            this.hoursWorked = hoursWorked;
            this.hourlyRate = hourlyRate;
        }

        @Override
        public String getType() {
            return "PartTime";
        }

        @Override
        public int calculatePay() {
            return hoursWorked * hourlyRate;
        }
    }

    // ==================== Intern Staff ====================

    /**
     * Intern with a fixed stipend.
     */
    static class Intern extends Staff {
        private final int stipend;

        public Intern(String name, int stipend) {
            super(name);
            this.stipend = stipend;
        }

        @Override
        public String getType() {
            return "Intern";
        }

        @Override
        public int calculatePay() {
            return stipend;
        }
    }

    // ==================== System Processor ====================

    /**
     * Processes a series of staff definitions and produces payslip output.
     *
     * @param operations Array of staff definition strings.
     * @return Array of output strings (payslips + total + top earner).
     */
    public static String[] processOperations(String[] operations) {
        Staff[] staffArray = new Staff[operations.length];
        int count = 0;

        // Parse operations and create staff objects
        for (String op : operations) {
            String[] parts = op.split(" ");
            String type = parts[0];

            switch (type) {
                case "FullTime":
                    // FullTime <Name> <Salary>
                    staffArray[count++] = new FullTime(parts[1], Integer.parseInt(parts[2]));
                    break;

                case "PartTime":
                    // PartTime <Name> <Hours> <Rate>
                    staffArray[count++] = new PartTime(parts[1],
                        Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                    break;

                case "Intern":
                    // Intern <Name> <Stipend>
                    staffArray[count++] = new Intern(parts[1], Integer.parseInt(parts[2]));
                    break;
            }
        }

        StringBuilder output = new StringBuilder();

        // Print all payslips
        for (int i = 0; i < count; i++) {
            output.append(staffArray[i].toString()).append("\n");
        }

        // Calculate total pay and find top earner
        int totalPay = 0;
        int topEarnerIndex = 0;
        int topPay = 0;

        for (int i = 0; i < count; i++) {
            int pay = staffArray[i].calculatePay();
            totalPay += pay;

            if (pay > topPay) {
                topPay = pay;
                topEarnerIndex = i;
            }
        }

        output.append("Total " + totalPay).append("\n");
        output.append("top earner " + staffArray[topEarnerIndex].getName());

        String result = output.toString().trim();
        return result.isEmpty() ? new String[0] : result.split("\n");
    }

    // ==================== Helper Method ====================

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
        System.out.println("         PAYROLL REGISTER SYSTEM - TEST SUITE             ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        // FullTime Asha: 50000
        // PartTime Ravi: 80 * 300 = 24000
        // Intern Neha: 15000
        // Total = 89000, Top earner = Asha
        runTestCase("Sample Problem",
            new String[]{
                "FullTime Asha 50000",
                "PartTime Ravi 80 300",
                "Intern Neha 15000"
            },
            new String[]{
                "Payslip[name=Asha, type=FullTime, pay=50000]",
                "Payslip[name=Ravi, type=PartTime, pay=24000]",
                "Payslip[name=Neha, type=Intern, pay=15000]",
                "Total 89000",
                "top earner Asha"
            }
        );

        // Test Case 2: Part-time earns the most
        // FullTime Dev: 30000
        // PartTime Sara: 160 * 500 = 80000 (top earner)
        // Intern Kiran: 10000
        // Total = 120000
        runTestCase("PartTime Earns Most",
            new String[]{
                "FullTime Dev 30000",
                "PartTime Sara 160 500",
                "Intern Kiran 10000"
            },
            new String[]{
                "Payslip[name=Dev, type=FullTime, pay=30000]",
                "Payslip[name=Sara, type=PartTime, pay=80000]",
                "Payslip[name=Kiran, type=Intern, pay=10000]",
                "Total 120000",
                "top earner Sara"
            }
        );

        // Test Case 3: Single staff member
        runTestCase("Single Staff Member",
            new String[]{
                "Intern Amit 12000"
            },
            new String[]{
                "Payslip[name=Amit, type=Intern, pay=12000]",
                "Total 12000",
                "top earner Amit"
            }
        );

        // Test Case 4: All same type (FullTime)
        runTestCase("All FullTime Staff",
            new String[]{
                "FullTime A 40000",
                "FullTime B 55000",
                "FullTime C 45000"
            },
            new String[]{
                "Payslip[name=A, type=FullTime, pay=40000]",
                "Payslip[name=B, type=FullTime, pay=55000]",
                "Payslip[name=C, type=FullTime, pay=45000]",
                "Total 140000",
                "top earner B"
            }
        );

        // Test Case 5: PartTime with minimal hours
        runTestCase("PartTime Minimal Hours",
            new String[]{
                "PartTime X 1 100",
                "Intern Y 500"
            },
            new String[]{
                "Payslip[name=X, type=PartTime, pay=100]",
                "Payslip[name=Y, type=Intern, pay=500]",
                "Total 600",
                "top earner Y"
            }
        );
    }
}
