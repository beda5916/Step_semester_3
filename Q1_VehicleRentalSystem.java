/**
 * Vehicle Rental System
 * 
 * Features:
 * - Abstract Vehicle class with subclasses: Sedan, SUV, Truck
 * - Polymorphic rental charge calculation:
 *     - Sedan: $50.0/day
 *     - SUV: $75.0/day
 *     - Truck: $100.0/day
 * - Customer class tracking customer name
 * - Rental class tracking vehicle, customer, rental days, and active status
 * - Availability management ensuring:
 *     - Cannot rent an already-rented vehicle
 *     - Return operation restores vehicle availability
 * - Main method demonstrating complete workflow and matching sample output
 */

public class Q1_VehicleRentalSystem {

    // ==========================================
    // Abstract Vehicle Class & Subclasses
    // ==========================================
    /**
     * Abstract base class representing a rental vehicle.
     */
    public static abstract class Vehicle {
        private final String name;
        private final double dailyRate;
        private boolean available;

        public Vehicle(String name, double dailyRate) {
            this.name = name;
            this.dailyRate = dailyRate;
            this.available = true; // Vehicles are available by default upon creation
        }

        public String getName() {
            return name;
        }

        public double getDailyRate() {
            return dailyRate;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        /**
         * Calculates rental charge based on number of rental days.
         * Implemented polymorphically by subclasses.
         *
         * @param days number of rental days
         * @return total rental charge
         */
        public abstract double calculateRentalCharge(int days);

        @Override
        public String toString() {
            return name + " (" + getClass().getSimpleName() + ")";
        }
    }

    /**
     * Sedan vehicle subclass ($50/day).
     */
    public static class Sedan extends Vehicle {
        private static final double RATE_PER_DAY = 50.0;

        public Sedan(String name) {
            super(name, RATE_PER_DAY);
        }

        public Sedan() {
            super("Sedan", RATE_PER_DAY);
        }

        @Override
        public double calculateRentalCharge(int days) {
            return days * RATE_PER_DAY;
        }
    }

    /**
     * SUV vehicle subclass ($75/day).
     */
    public static class SUV extends Vehicle {
        private static final double RATE_PER_DAY = 75.0;

        public SUV(String name) {
            super(name, RATE_PER_DAY);
        }

        public SUV() {
            super("SUV", RATE_PER_DAY);
        }

        @Override
        public double calculateRentalCharge(int days) {
            return days * RATE_PER_DAY;
        }
    }

    /**
     * Truck vehicle subclass ($100/day).
     */
    public static class Truck extends Vehicle {
        private static final double RATE_PER_DAY = 100.0;

        public Truck(String name) {
            super(name, RATE_PER_DAY);
        }

        public Truck() {
            super("Truck", RATE_PER_DAY);
        }

        @Override
        public double calculateRentalCharge(int days) {
            return days * RATE_PER_DAY;
        }
    }

    // ==========================================
    // Customer Class
    // ==========================================
    /**
     * Represents a customer who can rent vehicles.
     */
    public static class Customer {
        private final String name;

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * Rents a vehicle for the specified duration.
         *
         * @param vehicle vehicle to rent
         * @param days number of rental days
         * @return Rental instance if successful, null if unavailable
         */
        public Rental rentVehicle(Vehicle vehicle, int days) {
            return Q1_VehicleRentalSystem.rentVehicle(this, vehicle, days);
        }

        /**
         * Returns an active rental.
         *
         * @param rental the rental to complete
         */
        public void returnVehicle(Rental rental) {
            Q1_VehicleRentalSystem.returnVehicle(rental);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // ==========================================
    // Rental Class
    // ==========================================
    /**
     * Tracks vehicle, customer, rental days, and active status.
     */
    public static class Rental {
        private final Vehicle vehicle;
        private final Customer customer;
        private final int rentalDays;
        private boolean active;

        public Rental(Vehicle vehicle, Customer customer, int rentalDays) {
            this.vehicle = vehicle;
            this.customer = customer;
            this.rentalDays = rentalDays;
            this.active = true;
            if (vehicle != null) {
                vehicle.setAvailable(false);
            }
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public Customer getCustomer() {
            return customer;
        }

        public int getRentalDays() {
            return rentalDays;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        /**
         * Calculates rental charge polymorphically through the vehicle reference.
         *
         * @return total rental charge
         */
        public double calculateCharge() {
            return vehicle.calculateRentalCharge(rentalDays);
        }

        /**
         * Completes return of the rented vehicle and restores its availability.
         */
        public void returnVehicle() {
            if (active) {
                this.active = false;
                if (this.vehicle != null) {
                    this.vehicle.setAvailable(true);
                }
                System.out.println(vehicle.getName() + " returned by " + customer.getName() + ".");
            } else {
                System.out.println((vehicle != null ? vehicle.getName() : "Vehicle") + " is already returned or inactive.");
            }
        }

        @Override
        public String toString() {
            return "Rental[vehicle=" + vehicle.getName() + ", customer=" + customer.getName() +
                   ", days=" + rentalDays + ", active=" + active + "]";
        }
    }

    // ==========================================
    // Rental Agency Operations
    // ==========================================
    /**
     * Agency service to manage vehicle rentals and returns.
     */
    public static class RentalAgency {
        /**
         * Attempts to rent a vehicle for a customer.
         * Enforces the rule that an already-rented vehicle cannot be rented.
         *
         * @param customer the customer renting the vehicle
         * @param vehicle the vehicle to be rented
         * @param rentalDays number of rental days
         * @return Rental instance if successful, or null if vehicle is unavailable
         */
        public static Rental rentVehicle(Customer customer, Vehicle vehicle, int rentalDays) {
            if (vehicle == null || customer == null) {
                System.out.println("Invalid rental request.");
                return null;
            }
            if (!vehicle.isAvailable()) {
                System.out.println(vehicle.getName() + " is currently unavailable.");
                return null;
            }
            Rental rental = new Rental(vehicle, customer, rentalDays);
            System.out.println(vehicle.getName() + " rented successfully by " + customer.getName() +
                               ". Rental charge: $" + rental.calculateCharge());
            return rental;
        }

        /**
         * Processes the return of a rented vehicle.
         *
         * @param rental the rental being returned
         */
        public static void returnVehicle(Rental rental) {
            if (rental != null) {
                rental.returnVehicle();
            }
        }
    }

    // ==========================================
    // Convenience Static Helper Methods
    // ==========================================
    public static Rental rentVehicle(Customer customer, Vehicle vehicle, int rentalDays) {
        return RentalAgency.rentVehicle(customer, vehicle, rentalDays);
    }

    public static void returnVehicle(Rental rental) {
        RentalAgency.returnVehicle(rental);
    }

    // ==========================================
    // Main Method Demonstrating Workflow
    // ==========================================
    public static void main(String[] args) {
        // 1. Initialize Vehicles
        Vehicle sedanA = new Sedan("Sedan A");
        Vehicle suvB = new SUV("SUV B");
        Vehicle truckC = new Truck("Truck C");

        // 2. Initialize Customers
        Customer customer1 = new Customer("Customer 1");
        Customer customer2 = new Customer("Customer 2");
        Customer customer3 = new Customer("Customer 3");

        // Step 1: Customer 1 rents Sedan A for 3 days ($50/day * 3 = $150.0)
        Rental rental1 = rentVehicle(customer1, sedanA, 3);

        // Step 2: Attempt to rent Sedan A while it is currently rented (should fail)
        Rental rental2 = rentVehicle(customer2, sedanA, 2);

        // Step 3: Customer 1 returns Sedan A (restores availability)
        returnVehicle(rental1);

        // Step 4: Customer 3 rents SUV B for 5 days ($75/day * 5 = $375.0)
        Rental rental3 = rentVehicle(customer3, suvB, 5);
    }
}
