class LoanReceipt
{
    private final String memberId;
    private final String[] bookIds;

    public LoanReceipt(String memberId, String[] bookIds)
    {
        this.memberId = memberId;

        if (bookIds == null)
        {
            this.bookIds = null;
        }
        else
        {
            this.bookIds = new String[bookIds.length];

            for (int i = 0; i < bookIds.length; i++)
            {
                this.bookIds[i] = bookIds[i];
            }
        }
    }

    public String[] getBookIds()
    {
        if (bookIds == null)
        {
            return null;
        }

        String[] copy = new String[bookIds.length];

        for (int i = 0; i < bookIds.length; i++)
        {
            copy[i] = bookIds[i];
        }

        return copy;
    }

    public LoanReceipt withCorrectedBookId(int index, String newId)
    {
        String[] copy = getBookIds();

        if (copy != null && index >= 0 && index < copy.length)
        {
            copy[index] = newId;
        }

        return new LoanReceipt(memberId, copy);
    }
}


class ReferenceOnlyLoanReceipt extends LoanReceipt
{
    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(
        String memberId,
        String[] bookIds,
        String roomNumber)
    {
        super(memberId, bookIds);
        this.roomNumber = roomNumber;
    }
}


public class Immutable_Loan_Receipt_Nightly_Circulation_Ledger
{
    private static String branchCode;

    static
    {
        branchCode = "PT-001";
    }

    static String processNightlyCirculation(LoanReceipt[] receipts)
    {
        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        if (receipts == null)
        {
            return "0 processed | 0 null skipped | 0 reference-only | 0 regular";
        }

        for (int i = 0; i < receipts.length; i++)
        {
            LoanReceipt receipt = receipts[i];

            if (receipt == null)
            {
                nullSkipped++;
                continue;
            }

            processed++;

            if (receipt instanceof ReferenceOnlyLoanReceipt)
            {
                referenceOnly++;
            }
            else
            {
                regular++;
            }
        }

        return processed + " processed | "
             + nullSkipped + " null skipped | "
             + referenceOnly + " reference-only | "
             + regular + " regular";
    }

    public static void main(String[] args)
    {
        LoanReceipt r =
            new LoanReceipt(
                "LIB-8841",
                new String[]{"BK-100", "BK-101"}
            );

        String[] ids = r.getBookIds();

        ids[0] = "HACKED";

        System.out.println(r.getBookIds()[0]);

        LoanReceipt corrected =
            r.withCorrectedBookId(1, "BK-102");

        System.out.println(r.getBookIds()[0]);
        System.out.println(r.getBookIds()[1]);

        System.out.println(corrected.getBookIds()[0]);
        System.out.println(corrected.getBookIds()[1]);

        LoanReceipt[] receipts =
        {
            new ReferenceOnlyLoanReceipt(
                "LIB-001",
                new String[]{"BK-200"},
                "Reading Room 3"
            ),
            null,
            new LoanReceipt(
                "LIB-002",
                new String[]{"BK-201"}
            )
        };

        System.out.println(
            processNightlyCirculation(receipts)
        );
    }
}