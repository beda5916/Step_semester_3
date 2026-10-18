import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Campus Premiere Ticket Counter System.
 * 
 * Features:
 * - Customer representation with name.
 * - Abstract Seat hierarchy: RegularSeat (₹150), PremiumSeat (₹250), ReclinerSeat (₹400).
 * - Show tracking with show time and booked seats.
 * - Booking system supporting up to 6 seats per booking.
 * - Prevention of double bookings.
 * - Cancellation rules (only before show starts, releases all seats).
 * - Currency formatting using ₹.
 */

// ==========================================
// 1. Customer Class
// ==========================================
class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

// ==========================================
// 2. Seat Hierarchy
// ==========================================
abstract class Seat {
    private final String seatId;

    public Seat(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatId() {
        return seatId;
    }

    /**
     * Returns the price of the seat.
     * Subclasses must implement this method.
     */
    public abstract double getPrice();

    @Override
    public String toString() {
        return seatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(seatId, seat.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId);
    }
}

class RegularSeat extends Seat {
    private static final double PRICE = 150.0;

    public RegularSeat(String seatId) {
        super(seatId);
    }

    @Override
    public double getPrice() {
        return PRICE;
    }
}

class PremiumSeat extends Seat {
    private static final double PRICE = 250.0;

    public PremiumSeat(String seatId) {
        super(seatId);
    }

    @Override
    public double getPrice() {
        return PRICE;
    }
}

class ReclinerSeat extends Seat {
    private static final double PRICE = 400.0;

    public ReclinerSeat(String seatId) {
        super(seatId);
    }

    @Override
    public double getPrice() {
        return PRICE;
    }
}

// ==========================================
// 3. Show Class
// ==========================================
class Show {
    private final String showTime;
    private final Set<String> bookedSeatIds;
    private boolean started;

    public Show(String showTime) {
        this.showTime = showTime;
        this.bookedSeatIds = new HashSet<>();
        this.started = false;
    }

    public String getShowTime() {
        return showTime;
    }

    public boolean hasStarted() {
        return started;
    }

    public boolean isStarted() {
        return started;
    }

    public void startShow() {
        this.started = true;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isSeatBooked(String seatId) {
        return bookedSeatIds.contains(seatId);
    }

    public boolean isSeatBooked(Seat seat) {
        return seat != null && isSeatBooked(seat.getSeatId());
    }

    public void addBookedSeat(String seatId) {
        bookedSeatIds.add(seatId);
    }

    public void releaseSeat(String seatId) {
        bookedSeatIds.remove(seatId);
    }

    public void releaseSeat(Seat seat) {
        if (seat != null) {
            releaseSeat(seat.getSeatId());
        }
    }

    public void releaseSeats(List<Seat> seats) {
        if (seats != null) {
            for (Seat seat : seats) {
                releaseSeat(seat.getSeatId());
            }
        }
    }

    public Set<String> getBookedSeats() {
        return Collections.unmodifiableSet(bookedSeatIds);
    }

    /**
     * Attempts to book a list of seats for a customer.
     * Returns the created Booking if successful, or null otherwise.
     */
    public Booking bookSeats(Customer customer, List<Seat> seats) {
        Booking booking = new Booking(customer, this, seats);
        return booking.isConfirmed() ? booking : null;
    }

    /**
     * Varargs convenience method to book seats.
     */
    public Booking bookSeats(Customer customer, Seat... seats) {
        return bookSeats(customer, Arrays.asList(seats));
    }

    @Override
    public String toString() {
        return showTime;
    }
}

// ==========================================
// 4. Booking Class
// ==========================================
class Booking {
    private final Customer customer;
    private final Show show;
    private final List<Seat> seats;
    private boolean cancelled;
    private boolean confirmed;

    public Booking(Customer customer, Show show, List<Seat> seats) {
        this.customer = customer;
        this.show = show;
        this.seats = (seats != null) ? new ArrayList<>(seats) : new ArrayList<>();
        this.cancelled = false;
        this.confirmed = false;

        // Validation 1: At least one seat required
        if (this.seats.isEmpty()) {
            System.out.println("Booking failed: No seats selected.");
            return;
        }

        // Validation 2: Maximum 6 seats per booking
        if (this.seats.size() > 6) {
            System.out.println("Cannot book more than 6 seats per booking.");
            return;
        }

        // Validation 3: Check for duplicates within requested seats and availability in show
        Set<String> requestedIds = new HashSet<>();
        for (Seat seat : this.seats) {
            if (!requestedIds.add(seat.getSeatId())) {
                System.out.println("Duplicate seat " + seat.getSeatId() + " in booking request.");
                return;
            }
            if (show.isSeatBooked(seat.getSeatId())) {
                System.out.println("Seat " + seat.getSeatId() + " is already booked for this show.");
                return;
            }
        }

        // Register all seats as booked in the show
        for (Seat seat : this.seats) {
            show.addBookedSeat(seat.getSeatId());
        }
        this.confirmed = true;

        // Print confirmation message
        String seatIdsStr = this.seats.stream()
                .map(Seat::getSeatId)
                .collect(Collectors.joining(", "));
        System.out.printf(Locale.US, "Booking confirmed for %s: %s. Total: ₹%.2f.%n",
                customer.getName(), seatIdsStr, getTotalPrice());
    }

    public Customer getCustomer() {
        return customer;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Calculates the total price of all booked seats.
     */
    public double getTotalPrice() {
        double total = 0.0;
        for (Seat seat : seats) {
            total += seat.getPrice();
        }
        return total;
    }

    /**
     * Cancels the booking if the show has not started yet.
     * Releases all associated seats back to the show.
     */
    public boolean cancelBooking() {
        if (!confirmed) {
            System.out.println("Booking was not confirmed, cannot cancel.");
            return false;
        }
        if (cancelled) {
            System.out.println("Booking for " + customer.getName() + " is already cancelled.");
            return false;
        }
        if (show.hasStarted()) {
            System.out.println("Cannot cancel booking for " + customer.getName() + ": Show has already started.");
            return false;
        }

        this.cancelled = true;
        show.releaseSeats(seats);

        String seatLabel = (seats.size() == 1) ? "Seat " : "Seats ";
        String seatIdsStr = seats.stream()
                .map(Seat::getSeatId)
                .collect(Collectors.joining(", "));
        System.out.println(customer.getName() + "'s booking cancelled. " + seatLabel + seatIdsStr + " released.");
        return true;
    }

    /**
     * Alias for cancelBooking().
     */
    public boolean cancel() {
        return cancelBooking();
    }
}

// ==========================================
// 5. Main Public Class
// ==========================================
public class Q3_CampusPremiereTicketCounter {
    public static void main(String[] args) {
        // Create a show (e.g., '7 PM show')
        Show show = new Show("7 PM show");

        // Define available seats
        Seat a1 = new RegularSeat("A1");
        Seat a2 = new RegularSeat("A2");
        Seat f5 = new PremiumSeat("F5");
        Seat r1 = new ReclinerSeat("R1");

        // Customers
        Customer asha = new Customer("Asha");
        Customer ravi = new Customer("Ravi");
        Customer neha = new Customer("Neha");
        Customer guest = new Customer("Guest");

        // 1. Asha books A1, A2, and F5 (Regular ₹150 + Regular ₹150 + Premium ₹250 = ₹550.00)
        Booking ashaBooking = show.bookSeats(asha, Arrays.asList(a1, a2, f5));

        // 2. Someone attempts to book seat A2 which is already booked by Asha
        show.bookSeats(guest, Arrays.asList(a2));

        // 3. Ravi books R1 (Recliner ₹400.00)
        Booking raviBooking = show.bookSeats(ravi, Arrays.asList(r1));

        // 4. Asha cancels her booking before the show starts
        if (ashaBooking != null) {
            ashaBooking.cancelBooking();
        }

        // 5. Neha books seat A2 (which is now available after Asha's cancellation)
        show.bookSeats(neha, Arrays.asList(a2));
    }
}
