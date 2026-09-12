public class Immutable_Booking_Receipt_Nightly_Settlement
{
    static class BookingReceipt
    {
        private final String bookingId;
        private final String[] seatNumbers;

        public BookingReceipt(String bookingId, String[] seatNumbers)
        {
            this.bookingId = bookingId;

            if (seatNumbers == null)
            {
                this.seatNumbers = new String[0];
            }
            else
            {
                this.seatNumbers = seatNumbers.clone();
            }
        }

        public String getBookingId()
        {
            return bookingId;
        }

        public String[] getSeatNumbers()
        {
            return seatNumbers.clone();
        }

        public BookingReceipt withUpdatedSeat(int index, String newSeat)
        {
            if (index < 0 || index >= seatNumbers.length)
            {
                return this;
            }

            String[] updatedSeats = seatNumbers.clone();
            updatedSeats[index] = newSeat;

            return new BookingReceipt(bookingId, updatedSeats);
        }
    }

    static class GroupBookingReceipt extends BookingReceipt
    {
        private final int groupSize;

        public GroupBookingReceipt(
                String bookingId,
                String[] seatNumbers,
                int groupSize)
        {
            super(bookingId, seatNumbers);
            this.groupSize = groupSize;
        }

        public int getGroupSize()
        {
            return groupSize;
        }
    }

    static String processNightlySettlement(BookingReceipt[] receipts)
    {
        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;

        if (receipts == null)
        {
            return "0 processed | 0 null skipped | 0 group | 0 individual";
        }

        for (int i = 0; i < receipts.length; i++)
        {
            BookingReceipt receipt = receipts[i];

            if (receipt == null)
            {
                nullSkipped++;
                continue;
            }

            processed++;

            if (receipt instanceof GroupBookingReceipt)
            {
                group++;
            }
            else
            {
                individual++;
            }
        }

        return processed + " processed | "
                + nullSkipped + " null skipped | "
                + group + " group | "
                + individual + " individual";
    }

    public static void main(String[] args)
    {
        String[] seats =
        {
            "A1",
            "A2",
            "A3"
        };

        BookingReceipt receipt =
                new BookingReceipt("BK-100", seats);

        seats[0] = "X1";

        System.out.println(receipt.getSeatNumbers()[0]);

        String[] returnedSeats = receipt.getSeatNumbers();
        returnedSeats[0] = "X2";

        System.out.println(receipt.getSeatNumbers()[0]);

        BookingReceipt updated =
                receipt.withUpdatedSeat(1, "B2");

        System.out.println(receipt.getSeatNumbers()[1]);
        System.out.println(updated.getSeatNumbers()[1]);

        GroupBookingReceipt groupReceipt =
                new GroupBookingReceipt(
                        "BK-101",
                        new String[]{"C1", "C2"},
                        2);

        BookingReceipt individualReceipt =
                new BookingReceipt(
                        "BK-102",
                        new String[]{"D1"});

        BookingReceipt[] receipts =
        {
            receipt,
            null,
            groupReceipt,
            individualReceipt
        };

        System.out.println(
                processNightlySettlement(receipts)
        );
    }
}