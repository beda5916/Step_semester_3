public class MovieBookingProfile_JavaBean_OTP_Property
{
    static class MovieBookingProfile
    {
        private String name;
        private boolean confirmed;
        private String otp;

        // Public no-argument constructor
        public MovieBookingProfile()
        {
            this.name = "";
            this.confirmed = false;
        }

        // Convenience constructor
        public MovieBookingProfile(String name)
        {
            this();
            this.name = name;
        }

        // Getter and setter for name
        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        // Boolean JavaBean getter
        public boolean isConfirmed()
        {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed)
        {
            this.confirmed = confirmed;
        }

        // OTP setter only
        public void setOtp(String otp)
        {
            if (otp != null && otp.matches("\\d{4,6}"))
            {
                this.otp = otp;
            }
        }
    }

    public static void main(String[] args)
    {
        MovieBookingProfile profile =
            new MovieBookingProfile("Rahul Dev");

        System.out.println(profile.getName());

        profile.setConfirmed(true);

        System.out.println(profile.isConfirmed());

        profile.setOtp("4471");
    }
}