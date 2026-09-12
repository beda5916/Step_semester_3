public class LibraryMember_JavaBean_Security_Answer_Property
{
    private String membershipId;
    private String name;
    private boolean premiumMember;
    private String securityAnswer;

    // Public no-argument constructor
    public LibraryMember_JavaBean_Security_Answer_Property()
    {
    }

    // Write-once membershipId
    public String getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(String id)
    {
        if (membershipId == null)
        {
            membershipId = id;
        }
    }

    // JavaBean name property
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    // JavaBean premiumMember property
    public boolean isPremiumMember()
    {
        return premiumMember;
    }

    public void setPremiumMember(boolean premium)
    {
        premiumMember = premium;
    }

    // Write-only securityAnswer
    public void setSecurityAnswer(String answer)
    {
        if (answer == null)
        {
            securityAnswer = null;
        }
        else
        {
            securityAnswer = Integer.toHexString(answer.hashCode());
        }
    }

    public static void main(String[] args)
    {
        LibraryMember_JavaBean_Security_Answer_Property m =
            new LibraryMember_JavaBean_Security_Answer_Property();

        m.setMembershipId("LIB-8841");
        m.setMembershipId("LIB-9999");

        m.setName("Priya Nair");

        m.setPremiumMember(true);

        m.setSecurityAnswer("BlueMountain");

        System.out.println(m.getMembershipId());
        System.out.println(m.getName());
        System.out.println(m.isPremiumMember());
    }
}