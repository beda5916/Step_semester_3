/**
 * Hotel Booking System
 * 
 * Features:
 * - Abstract Room class with subclasses: StandardRoom ($100/night), DeluxeRoom ($200/night), Suite ($350/night)
 * - Customer class with name
 * - Reservation class with start date, end date, room, customer, cancellation deadline, price calculation
 * - Check room availability for a date range (no overlapping reservations)
 * - Create reservation, calculate price
 * - Cancel reservation (only before cancellation deadline)
 * - Uses java.time.LocalDate for dates
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Q4_HotelBookingSystem {

    // ==========================================
    // Date Formatter and Helpers
    // ==========================================
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);

    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatRange(LocalDate startDate, LocalDate endDate) {
        return formatDate(startDate) + " - " + formatDate(endDate);
    }

    // ==========================================
    // Customer Class
    // ==========================================
    public static class Customer {
        private final String name;

        public Customer(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be null or empty.");
            }
            this.name = name.trim();
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Customer customer = (Customer) o;
            return Objects.equals(name, customer.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    // ==========================================
    // Abstract Room Class & Subclasses
    // ==========================================
    public static abstract class Room {
        private final String roomNumber;

        public Room(String roomNumber) {
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Room number cannot be null or empty.");
            }
            this.roomNumber = roomNumber.trim();
        }

        public Room(int roomNumber) {
            this(String.valueOf(roomNumber));
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        /**
         * Returns the room type name (e.g., "Standard Room", "Deluxe Room", "Suite").
         */
        public abstract String getRoomType();

        /**
         * Returns the price per night for the room type.
         */
        public abstract double getPricePerNight();

        @Override
        public String toString() {
            return getRoomType() + " " + roomNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Room room = (Room) o;
            return Objects.equals(roomNumber, room.roomNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roomNumber);
        }
    }

    /**
     * Standard Room ($100/night)
     */
    public static class StandardRoom extends Room {
        public StandardRoom(String roomNumber) {
            super(roomNumber);
        }

        public StandardRoom(int roomNumber) {
            super(roomNumber);
        }

        @Override
        public String getRoomType() {
            return "Standard Room";
        }

        @Override
        public double getPricePerNight() {
            return 100.0;
        }
    }

    /**
     * Deluxe Room ($200/night)
     */
    public static class DeluxeRoom extends Room {
        public DeluxeRoom(String roomNumber) {
            super(roomNumber);
        }

        public DeluxeRoom(int roomNumber) {
            super(roomNumber);
        }

        @Override
        public String getRoomType() {
            return "Deluxe Room";
        }

        @Override
        public double getPricePerNight() {
            return 200.0;
        }
    }

    /**
     * Suite ($350/night)
     */
    public static class Suite extends Room {
        public Suite(String roomNumber) {
            super(roomNumber);
        }

        public Suite(int roomNumber) {
            super(roomNumber);
        }

        @Override
        public String getRoomType() {
            return "Suite";
        }

        @Override
        public double getPricePerNight() {
            return 350.0;
        }
    }

    // ==========================================
    // Reservation Class
    // ==========================================
    public static class Reservation {
        private final Customer customer;
        private final Room room;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final LocalDate cancellationDeadline;
        private final double totalPrice;
        private boolean cancelled;

        public Reservation(Customer customer, Room room, LocalDate startDate, LocalDate endDate, LocalDate cancellationDeadline) {
            if (customer == null) throw new IllegalArgumentException("Customer cannot be null.");
            if (room == null) throw new IllegalArgumentException("Room cannot be null.");
            if (startDate == null) throw new IllegalArgumentException("Start date cannot be null.");
            if (endDate == null) throw new IllegalArgumentException("End date cannot be null.");
            if (!endDate.isAfter(startDate)) {
                throw new IllegalArgumentException("End date must be strictly after start date.");
            }

            this.customer = customer;
            this.room = room;
            this.startDate = startDate;
            this.endDate = endDate;
            // Default cancellation deadline: 1 day before start date if not explicitly provided
            this.cancellationDeadline = (cancellationDeadline != null) ? cancellationDeadline : startDate.minusDays(1);
            this.cancelled = false;

            long nights = ChronoUnit.DAYS.between(startDate, endDate);
            this.totalPrice = nights * room.getPricePerNight();
        }

        public Reservation(Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
            this(customer, room, startDate, endDate, startDate != null ? startDate.minusDays(1) : null);
        }

        public Customer getCustomer() {
            return customer;
        }

        public Room getRoom() {
            return room;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public LocalDate getCancellationDeadline() {
            return cancellationDeadline;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        /**
         * Checks if the reservation can be cancelled on the given request date.
         * Only allows cancellation before or on the cancellation deadline.
         */
        public boolean canCancel(LocalDate requestDate) {
            if (cancelled || requestDate == null) {
                return false;
            }
            return !requestDate.isAfter(cancellationDeadline);
        }

        /**
         * Cancels the reservation if the request date is within the deadline.
         */
        public boolean cancel(LocalDate requestDate) {
            if (!canCancel(requestDate)) {
                return false;
            }
            this.cancelled = true;
            return true;
        }

        public boolean cancel() {
            return cancel(LocalDate.now());
        }

        /**
         * Checks if this reservation overlaps with a specified date range.
         * Stays use the half-open interval [startDate, endDate) since check-in on the check-out date is allowed.
         */
        public boolean overlaps(LocalDate rangeStart, LocalDate rangeEnd) {
            if (cancelled) {
                return false;
            }
            return startDate.isBefore(rangeEnd) && endDate.isAfter(rangeStart);
        }

        @Override
        public String toString() {
            return "Reservation[" + customer.getName() + ", " + room + ", " 
                + formatRange(startDate, endDate) + ", Price: $" + totalPrice 
                + ", Cancelled: " + cancelled + "]";
        }
    }

    // ==========================================
    // Hotel Booking System Class
    // ==========================================
    public static class HotelBookingSystem {
        private final List<Room> rooms;
        private final List<Reservation> reservations;

        public HotelBookingSystem() {
            this.rooms = new ArrayList<>();
            this.reservations = new ArrayList<>();
        }

        public void addRoom(Room room) {
            if (room != null && !rooms.contains(room)) {
                rooms.add(room);
            }
        }

        public List<Room> getRooms() {
            return Collections.unmodifiableList(rooms);
        }

        public List<Reservation> getReservations() {
            return Collections.unmodifiableList(reservations);
        }

        /**
         * Checks if a room is available for the given date range (no overlapping reservations).
         */
        public boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
            if (room == null || startDate == null || endDate == null || !endDate.isAfter(startDate)) {
                return false;
            }
            for (Reservation res : reservations) {
                if (!res.isCancelled() && res.getRoom().equals(room)) {
                    if (res.overlaps(startDate, endDate)) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Checks availability and prints the status message.
         */
        public boolean checkAvailability(Room room, LocalDate startDate, LocalDate endDate) {
            boolean available = isRoomAvailable(room, startDate, endDate);
            if (available) {
                System.out.println(room + " is available from " + formatDate(startDate) + " to " + formatDate(endDate) + ".");
            } else {
                System.out.println(room + " is not available from " + formatDate(startDate) + " to " + formatDate(endDate) + ".");
            }
            return available;
        }

        /**
         * Creates a reservation if available, calculates price, and prints confirmation.
         */
        public Reservation createReservation(Customer customer, Room room, LocalDate startDate, LocalDate endDate, LocalDate cancellationDeadline) {
            if (!isRoomAvailable(room, startDate, endDate)) {
                System.out.println(room + " is not available from " + formatDate(startDate) + " to " + formatDate(endDate) + ".");
                return null;
            }

            Reservation reservation = new Reservation(customer, room, startDate, endDate, cancellationDeadline);
            reservations.add(reservation);
            System.out.println("Reservation confirmed for " + customer.getName() + ", " + room 
                + " (" + formatRange(startDate, endDate) + "). Price: $" + reservation.getTotalPrice());
            return reservation;
        }

        public Reservation createReservation(Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
            return createReservation(customer, room, startDate, endDate, startDate != null ? startDate.minusDays(1) : null);
        }

        /**
         * Cancels a reservation if request date is on or before cancellation deadline.
         */
        public boolean cancelReservation(Reservation reservation, LocalDate cancellationDate) {
            if (reservation == null) {
                System.out.println("Invalid reservation.");
                return false;
            }
            if (reservation.isCancelled()) {
                System.out.println("Reservation for " + reservation.getCustomer().getName() + " is already cancelled.");
                return false;
            }
            if (cancellationDate.isAfter(reservation.getCancellationDeadline())) {
                System.out.println("Cannot cancel reservation: cancellation deadline (" 
                    + formatDate(reservation.getCancellationDeadline()) + ") has passed.");
                return false;
            }

            reservation.cancel(cancellationDate);
            System.out.println("Reservation for " + reservation.getCustomer().getName() + ", " 
                + reservation.getRoom() + " (" + formatRange(reservation.getStartDate(), reservation.getEndDate()) 
                + ") cancelled successfully.");
            return true;
        }

        public boolean cancelReservation(Reservation reservation) {
            return cancelReservation(reservation, LocalDate.now());
        }
    }

    // ==========================================
    // Main Method Demonstrating Requirements
    // ==========================================
    public static void main(String[] args) {
        HotelBookingSystem system = new HotelBookingSystem();

        // 1. Initialize Rooms of various types
        Room standard101 = new StandardRoom("101");
        Room deluxe201 = new DeluxeRoom("201");
        Room suite301 = new Suite("301");

        system.addRoom(standard101);
        system.addRoom(deluxe201);
        system.addRoom(suite301);

        // 2. Initialize Customers
        Customer customerA = new Customer("Customer A");
        Customer customerC = new Customer("Customer C");

        // Define test dates using a dynamic future year so the demonstration
        // and cancellation deadlines remain valid at any execution date/year.
        int year = LocalDate.now().getYear() + 1;
        LocalDate jan1 = LocalDate.of(year, 1, 1);
        LocalDate jan5 = LocalDate.of(year, 1, 5);
        LocalDate jan3 = LocalDate.of(year, 1, 3);
        LocalDate jan7 = LocalDate.of(year, 1, 7);
        LocalDate feb10 = LocalDate.of(year, 2, 10);
        LocalDate feb12 = LocalDate.of(year, 2, 12);

        // Step 1: Check room availability for Standard Room 101 from Jan 1 to Jan 5
        system.checkAvailability(standard101, jan1, jan5);

        // Step 2: Create reservation for Customer A (Jan 1 - Jan 5) -> 4 nights * $100 = $400.0
        Reservation resA = system.createReservation(customerA, standard101, jan1, jan5);

        // Step 3: Check availability for overlapping dates (Jan 3 to Jan 7) -> Not available
        system.checkAvailability(standard101, jan3, jan7);

        // Step 4: Cancel reservation for Customer A before cancellation deadline
        system.cancelReservation(resA);

        // Step 5: Create reservation for Customer C for Deluxe Room 201 (Feb 10 - Feb 12) -> 2 nights * $200 = $400.0
        system.createReservation(customerC, deluxe201, feb10, feb12);
    }
}
