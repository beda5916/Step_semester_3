public class Monthly_Attendance_Announcer
{
    static class GymMember
    {
        protected String memberId;
        protected int monthlyFee;
        protected int sessionsAttended;

        public GymMember(String memberId, int monthlyFee)
        {
            this.memberId = memberId;
            this.monthlyFee = monthlyFee;
            this.sessionsAttended = 0;
        }

        public void attendSession()
        {
            sessionsAttended++;
        }

        public int getSessionsAttended()
        {
            return sessionsAttended;
        }

        public String displayInfo()
        {
            return "Standard | Sessions: "
                    + sessionsAttended;
        }
    }

    static class PremiumMember extends GymMember
    {
        private String trainerName;

        public PremiumMember(
                String memberId,
                int monthlyFee,
                String trainerName)
        {
            super(memberId, monthlyFee);
            this.trainerName = trainerName;
        }

        public String getTrainerName()
        {
            return trainerName;
        }

        @Override
        public String displayInfo()
        {
            return "Premium | Trainer: "
                    + trainerName
                    + " | Sessions: "
                    + sessionsAttended;
        }
    }

    static String batchPrint(GymMember[] members)
    {
        StringBuilder announcement =
                new StringBuilder();

        for (int i = 0; i < members.length; i++)
        {
            GymMember member = members[i];

            announcement.append(
                    member.displayInfo());

            if (member instanceof PremiumMember)
            {
                PremiumMember premium =
                        (PremiumMember) member;

                announcement.append(
                        " [Trainer via downcast: "
                        + premium.getTrainerName()
                        + "]");
            }

            announcement.append(" | ");
        }

        return announcement.toString();
    }

    public static void main(String[] args)
    {
        GymMember standard =
                new GymMember("MEM6", 1000);

        PremiumMember premium =
                new PremiumMember(
                        "MEM7",
                        2000,
                        "Coach Riya");

        GymMember[] members =
        {
            standard,
            premium
        };

        System.out.println(
                batchPrint(members));

        GymMember plain =
                new GymMember("MEM8", 1000);

        if (plain instanceof PremiumMember)
        {
            PremiumMember bad =
                    (PremiumMember) plain;

            System.out.println(
                    bad.getTrainerName());
        }
    }
}