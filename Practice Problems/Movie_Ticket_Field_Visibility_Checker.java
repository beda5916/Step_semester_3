public class Movie_Ticket_Field_Visibility_Checker
{
    // MovieTicket class
    static class MovieTicket
    {
        private String seatNumber;
        String screenId;
        protected double ticketPrice;
        public String movieTitle;
    }

    // Check whether a field can be accessed from a particular context
    static String classifyAccess(String fieldModifier, String accessorContext)
    {
        if (fieldModifier.equals("private"))
        {
            if (accessorContext.equals("SAME_CLASS"))
            {
                return "ALLOWED";
            }
            else
            {
                return "DENIED";
            }
        }

        if (fieldModifier.equals("default"))
        {
            if (accessorContext.equals("SAME_CLASS") ||
                accessorContext.equals("SAME_PACKAGE"))
            {
                return "ALLOWED";
            }
            else
            {
                return "DENIED";
            }
        }

        if (fieldModifier.equals("protected"))
        {
            if (accessorContext.equals("SAME_CLASS") ||
                accessorContext.equals("SAME_PACKAGE"))
            {
                return "ALLOWED";
            }
            else
            {
                return "DENIED";
            }
        }

        if (fieldModifier.equals("public"))
        {
            return "ALLOWED";
        }

        return "DENIED";
    }

    // Count allowed and denied attempts
    static String summarizeBatch(String[][] attempts)
    {
        int allowed = 0;
        int denied = 0;

        for (int i = 0; i < attempts.length; i++)
        {
            String result = classifyAccess(attempts[i][0], attempts[i][1]);

            if (result.equals("ALLOWED"))
            {
                allowed++;
            }
            else
            {
                denied++;
            }
        }

        return "Allowed: " + allowed + " | Denied: " + denied;
    }

    public static void main(String[] args)
    {
        System.out.println(
            classifyAccess("private", "SAME_CLASS")
        );

        System.out.println(
            classifyAccess("protected", "DIFFERENT_PACKAGE")
        );

        String[][] attempts =
        {
            {"default", "SAME_PACKAGE"},
            {"default", "DIFFERENT_PACKAGE"},
            {"public", "DIFFERENT_PACKAGE"}
        };

        System.out.println(
            summarizeBatch(attempts)
        );
    }
}
