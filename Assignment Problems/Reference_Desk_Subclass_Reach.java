public class Reference_Desk_Subclass_Reach
{
    private String membershipPin;
    String branchCode;
    protected double finesOwed;
    public String displayName;

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

        else if (fieldModifier.equals("default"))
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

        else if (fieldModifier.equals("protected"))
        {
            if (accessorContext.equals("SAME_CLASS") ||
                accessorContext.equals("SAME_PACKAGE") ||
                accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"))
            {
                return "ALLOWED";
            }
            else
            {
                return "DENIED";
            }
        }

        else if (fieldModifier.equals("public"))
        {
            return "ALLOWED";
        }

        return "DENIED";
    }

    static String firstDeniedAttempt(String[][] attempts)
    {
        for (int i = 0; i < attempts.length; i++)
        {
            String fieldModifier = attempts[i][0];
            String accessorContext = attempts[i][1];

            String result = classifyAccess(fieldModifier, accessorContext);

            if (result.equals("DENIED"))
            {
                return fieldModifier + " via "
                    + accessorContext + " (attempt #" + (i + 1) + ")";
            }
        }

        return "None Denied";
    }

    public static void main(String[] args)
    {
        String[][] attempts =
        {
            {"public", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"},
            {"protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"},
            {"protected", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"}
        };

        System.out.println(firstDeniedAttempt(attempts));
    }
}