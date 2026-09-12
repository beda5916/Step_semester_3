public class Book_Copy_Circulation_Guard
{
    private int copiesTotal;
    private int copiesAvailable;

    // Constructor
    public Book_Copy_Circulation_Guard(int copiesTotal)
    {
        if (copiesTotal < 0)
        {
            this.copiesTotal = 0;
            this.copiesAvailable = 0;
        }
        else
        {
            this.copiesTotal = copiesTotal;
            this.copiesAvailable = copiesTotal;
        }
    }

    // Check out one copy
    public boolean checkOut()
    {
        if (copiesAvailable > 0)
        {
            copiesAvailable--;
            return true;
        }

        return false;
    }

    // Check in one copy
    public boolean checkIn()
    {
        if (copiesAvailable < copiesTotal)
        {
            copiesAvailable++;
            return true;
        }

        return false;
    }

    // Getter
    public int getCopiesAvailable()
    {
        return copiesAvailable;
    }

    public static void main(String[] args)
    {
        Book_Copy_Circulation_Guard book =
            new Book_Copy_Circulation_Guard(3);

        System.out.println(book.getCopiesAvailable());

        System.out.println(book.checkOut());
        System.out.println(book.getCopiesAvailable());

        System.out.println(book.checkOut());
        System.out.println(book.checkOut());
        System.out.println(book.checkOut());

        System.out.println(book.getCopiesAvailable());

        System.out.println(book.checkIn());
        System.out.println(book.getCopiesAvailable());
    }
}