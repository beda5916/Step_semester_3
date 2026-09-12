public class Membership_Numbers_Referral_Codes_Weekly_Checkin_Settlement
{
    static class GymMember
    {
        private static int nextNumber = 2000;
        private static int membersEnrolled = 0;

        protected String memberId;
        protected int monthlyFee;

        private final String membershipNumber;
        private int feesPaid;

        public GymMember(String memberId, int monthlyFee)
        {
            this.memberId = memberId;
            this.monthlyFee = monthlyFee;

            nextNumber++;
            membershipNumber = "GYM-" + nextNumber;

            membersEnrolled++;
            feesPaid = 0;
        }

        public String getMembershipNumber()
        {
            return membershipNumber;
        }

        public boolean isValidReferralCode(String code)
        {
            if (code == null || code.length() != 4)
            {
                return false;
            }

            if (code.charAt(0) != 'G')
            {
                return false;
            }

            if (!Character.isDigit(code.charAt(1)))
            {
                return false;
            }

            if (!Character.isDigit(code.charAt(2)))
            {
                return false;
            }

            if (!Character.isUpperCase(code.charAt(3)))
            {
                return false;
            }

            return true;
        }

        public void payFee(int amount)
        {
            feesPaid += amount;
        }

        public void payFee(int amount, String mode)
        {
            payFee(amount);
        }

        public int getFeesPaid()
        {
            return feesPaid;
        }

        public static int getMembersEnrolled()
        {
            return membersEnrolled;
        }
    }

    static class GroupClassMember extends GymMember
    {
        private String className;

        public GroupClassMember(
                String memberId,
                int monthlyFee,
                String className)
        {
            super(memberId, monthlyFee);
            this.className = className;
        }
    }

    static String processWeeklyCheckIn(GymMember[] members)
    {
        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;

        for (int i = 0; i < members.length; i++)
        {
            if (members[i] == null)
            {
                nullSkipped++;
                continue;
            }

            processed++;

            if (members[i] instanceof GroupClassMember)
            {
                group++;
            }
            else
            {
                individual++;
            }
        }

        return processed
                + " processed | "
                + nullSkipped
                + " null skipped | "
                + group
                + " group | "
                + individual
                + " individual";
    }

    public static void main(String[] args)
    {
        GymMember member =
                new GymMember("MEM1", 1000);

        System.out.println(
                member.getMembershipNumber());

        System.out.println(
                GymMember.getMembersEnrolled());

        System.out.println(
                member.isValidReferralCode("G12A"));

        System.out.println(
                member.isValidReferralCode("G1AA"));

        System.out.println(
                member.isValidReferralCode("X12A"));

        member.payFee(500);
        member.payFee(500, "UPI");

        System.out.println(
                member.getFeesPaid());

        GroupClassMember groupMember =
                new GroupClassMember(
                        "MEM2",
                        1500,
                        "Zumba");

        GymMember[] members =
        {
            member,
            groupMember,
            null
        };

        System.out.println(
                processWeeklyCheckIn(members));
    }
}