public class Subclass_Ticket_Access
{
    static String classifyAccess(String fieldModifier, String accessorContext)
    {
        // private
        if (fieldModifier.equals("private"))
        {
            if (accessorContext.equals("SAME_CLASS"))
            {
                return "ALLOWED";
            }
            return "DENIED";
        }

        // default
        if (fieldModifier.equals("default"))
        {
            if (accessorContext.equals("SAME_CLASS") ||
                accessorContext.equals("SAME_PACKAGE"))
            {
                return "ALLOWED";
            }
            return "DENIED";
        }

        // protected
        if (fieldModifier.equals("protected"))
        {
            if (accessorContext.equals("SAME_CLASS") ||
                accessorContext.equals("SAME_PACKAGE") ||
                accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"))
            {
                return "ALLOWED";
            }

            if (accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE") ||
                accessorContext.equals("DIFFERENT_PACKAGE"))
            {
                return "DENIED";
            }

            return "DENIED";
        }

        // public
        if (fieldModifier.equals("public"))
        {
            return "ALLOWED";
        }

        return "DENIED";
    }

    public static void main(String[] args)
    {
        System.out.println(
            classifyAccess(
                "protected",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"
            )
        );

        System.out.println(
            classifyAccess(
                "protected",
                "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"
            )
        );

        System.out.println(
            classifyAccess(
                "private",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"
            )
        );

        System.out.println(
            classifyAccess(
                "default",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"
            )
        );
    }
}