public class Seat_Booking_Encapsulation_Guard
{
    static class CineScreen
    {
        private int seatsTotal;
        private int seatsAvailable;

        // Constructor
        CineScreen(int seatsTotal)
        {
            if (seatsTotal <= 0)
            {
                throw new IllegalArgumentException("seatsTotal must be positive");
            }

            this.seatsTotal = seatsTotal;
            this.seatsAvailable = seatsTotal;
        }

        // Book one seat
        void bookSeat()
        {
            if (seatsAvailable > 0)
            {
                seatsAvailable--;
            }
        }

        // Cancel one booking
        void cancelBooking()
        {
            if (seatsAvailable < seatsTotal)
            {
                seatsAvailable++;
            }
        }

        // Getter
        int getSeatsAvailable()
        {
            return seatsAvailable;
        }
    }

    public static void main(String[] args)
    {
        CineScreen screen = new CineScreen(2);

        System.out.println(screen.getSeatsAvailable());

        screen.bookSeat();
        System.out.println(screen.getSeatsAvailable());

        screen.bookSeat();
        System.out.println(screen.getSeatsAvailable());

        // Third booking is rejected silently
        screen.bookSeat();
        System.out.println(screen.getSeatsAvailable());

        // Cancel one booking
        screen.cancelBooking();
        System.out.println(screen.getSeatsAvailable());

        // Cancel another booking
        screen.cancelBooking();
        System.out.println(screen.getSeatsAvailable());

        // Third cancellation is rejected silently
        screen.cancelBooking();
        System.out.println(screen.getSeatsAvailable());
    }
}
