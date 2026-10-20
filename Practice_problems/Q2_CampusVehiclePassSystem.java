import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * Problem 2: Campus Vehicle Pass System
 * ============================================================================
 * Implement a system to manage campus vehicle passes and charging bay bookings.
 *
 * Rules:
 * - Every vehicle has a pass number and an owner.
 * - Yearly pass fee varies by vehicle type:
 *     Bike: 300, Car: 1000, E-Bike: 300, E-Car: 1000
 * - Only electric vehicles (E-Bike, E-Car) are eligible to book a charging bay.
 * - Design classes for vehicles and their types, incorporating an interface
 *   for chargeable vehicles.
 *
 * OOP Concepts Demonstrated:
 * - Abstract Class (Vehicle) with concrete and abstract members
 * - Interface (Chargeable) for charging bay eligibility
 * - Inheritance (Bike, Car, EBike, ECar extend Vehicle)
 * - Interface Implementation (EBike, ECar implement Chargeable)
 * - Polymorphism (Vehicle references in HashMap, instanceof for Chargeable check)
 * - Encapsulation (private fields with getters)
 *
 * Complexity Analysis:
 * --------------------
 * - PASS operation: O(1) — HashMap put + object creation.
 * - CHARGE operation: O(1) — HashMap get + instanceof check.
 * - Overall for n operations: O(n).
 * - Space Complexity: O(k) where k = number of registered vehicles.
 * ============================================================================
 */
public class Q2_CampusVehiclePassSystem {

    // ==================== Chargeable Interface ====================

    /**
     * Interface for vehicles that support charging bay booking.
     * Only electric vehicles implement this interface.
     */
    interface Chargeable {
        /**
         * Books a charging bay for this vehicle.
         * @return Confirmation message for charging bay allotment.
         */
        String bookChargingBay();
    }

    // ==================== Abstract Vehicle Class ====================

    /**
     * Abstract base class for all campus vehicles.
     * Encapsulates common fields: passNumber, ownerName.
     */
    static abstract class Vehicle {
        private final String passNumber;
        private final String ownerName;

        public Vehicle(String passNumber, String ownerName) {
            this.passNumber = passNumber;
            this.ownerName = ownerName;
        }

        public String getPassNumber() {
            return passNumber;
        }

        public String getOwnerName() {
            return ownerName;
        }

        /**
         * Returns the type name of the vehicle (e.g., "Bike", "Car", "EBike", "ECar").
         */
        public abstract String getTypeName();

        /**
         * Returns the yearly pass fee for this vehicle type.
         */
        public abstract int getPassFee();
    }

    // ==================== Concrete Vehicle Types ====================

    /** Non-electric Bike: pass fee 300, not chargeable. */
    static class Bike extends Vehicle {
        public Bike(String passNumber, String ownerName) {
            super(passNumber, ownerName);
        }

        @Override
        public String getTypeName() {
            return "Bike";
        }

        @Override
        public int getPassFee() {
            return 300;
        }
    }

    /** Non-electric Car: pass fee 1000, not chargeable. */
    static class Car extends Vehicle {
        public Car(String passNumber, String ownerName) {
            super(passNumber, ownerName);
        }

        @Override
        public String getTypeName() {
            return "Car";
        }

        @Override
        public int getPassFee() {
            return 1000;
        }
    }

    /** Electric Bike: pass fee 300, chargeable. */
    static class EBike extends Vehicle implements Chargeable {
        public EBike(String passNumber, String ownerName) {
            super(passNumber, ownerName);
        }

        @Override
        public String getTypeName() {
            return "EBike";
        }

        @Override
        public int getPassFee() {
            return 300;
        }

        @Override
        public String bookChargingBay() {
            return getPassNumber() + " charging bay allotted";
        }
    }

    /** Electric Car: pass fee 1000, chargeable. */
    static class ECar extends Vehicle implements Chargeable {
        public ECar(String passNumber, String ownerName) {
            super(passNumber, ownerName);
        }

        @Override
        public String getTypeName() {
            return "ECar";
        }

        @Override
        public int getPassFee() {
            return 1000;
        }

        @Override
        public String bookChargingBay() {
            return getPassNumber() + " charging bay allotted";
        }
    }

    // ==================== Factory Method ====================

    /**
     * Creates a Vehicle object based on the given type string.
     *
     * @param type       Vehicle type ("Bike", "Car", "EBike", "ECar").
     * @param passNumber The pass number for the vehicle.
     * @param ownerName  The name of the vehicle owner.
     * @return A Vehicle subclass instance, or null if type is unknown.
     */
    public static Vehicle createVehicle(String type, String passNumber, String ownerName) {
        switch (type) {
            case "Bike":  return new Bike(passNumber, ownerName);
            case "Car":   return new Car(passNumber, ownerName);
            case "EBike": return new EBike(passNumber, ownerName);
            case "ECar":  return new ECar(passNumber, ownerName);
            default:      return null;
        }
    }

    // ==================== System Processor ====================

    /**
     * Processes a series of PASS and CHARGE operations.
     *
     * @param operations Array of operation strings.
     * @return Array of output strings.
     */
    public static String[] processOperations(String[] operations) {
        Map<String, Vehicle> registry = new HashMap<>();
        StringBuilder output = new StringBuilder();

        for (String op : operations) {
            String[] parts = op.split(" ");
            String command = parts[0];

            switch (command) {
                case "PASS":
                    // PASS <VehicleType> <PassNumber> <OwnerName>
                    String type = parts[1];
                    String passNum = parts[2];
                    String owner = parts[3];
                    Vehicle vehicle = createVehicle(type, passNum, owner);
                    if (vehicle != null) {
                        registry.put(passNum, vehicle);
                        output.append(passNum + " (" + vehicle.getTypeName() + ") pass fee " + vehicle.getPassFee())
                              .append("\n");
                    }
                    break;

                case "CHARGE":
                    // CHARGE <PassNumber>
                    String chargePass = parts[1];
                    Vehicle v = registry.get(chargePass);
                    if (v == null) {
                        output.append(chargePass + " not found").append("\n");
                    } else if (v instanceof Chargeable) {
                        output.append(((Chargeable) v).bookChargingBay()).append("\n");
                    } else {
                        output.append(chargePass + " rejected: charging unsupported").append("\n");
                    }
                    break;
            }
        }

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
        System.out.println("       CAMPUS VEHICLE PASS SYSTEM - TEST SUITE            ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        runTestCase("Sample Problem",
            new String[]{
                "PASS Bike KA01 Asha",
                "PASS ECar KA02 Ravi",
                "CHARGE KA02",
                "CHARGE KA01"
            },
            new String[]{
                "KA01 (Bike) pass fee 300",
                "KA02 (ECar) pass fee 1000",
                "KA02 charging bay allotted",
                "KA01 rejected: charging unsupported"
            }
        );

        // Test Case 2: All vehicle types registered
        runTestCase("All Vehicle Types",
            new String[]{
                "PASS Bike B1 Amit",
                "PASS Car C1 Priya",
                "PASS EBike E1 Sara",
                "PASS ECar E2 Kiran",
                "CHARGE B1",
                "CHARGE C1",
                "CHARGE E1",
                "CHARGE E2"
            },
            new String[]{
                "B1 (Bike) pass fee 300",
                "C1 (Car) pass fee 1000",
                "E1 (EBike) pass fee 300",
                "E2 (ECar) pass fee 1000",
                "B1 rejected: charging unsupported",
                "C1 rejected: charging unsupported",
                "E1 charging bay allotted",
                "E2 charging bay allotted"
            }
        );

        // Test Case 3: Charge for non-existent vehicle
        runTestCase("Vehicle Not Found",
            new String[]{
                "CHARGE ZZ99"
            },
            new String[]{
                "ZZ99 not found"
            }
        );

        // Test Case 4: Only electric vehicles
        runTestCase("Only Electric Vehicles",
            new String[]{
                "PASS EBike EB01 Riya",
                "PASS ECar EC01 Dev",
                "CHARGE EB01",
                "CHARGE EC01"
            },
            new String[]{
                "EB01 (EBike) pass fee 300",
                "EC01 (ECar) pass fee 1000",
                "EB01 charging bay allotted",
                "EC01 charging bay allotted"
            }
        );
    }
}
