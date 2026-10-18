import java.util.Locale;

/**
 * Member class representing a gym member with a name.
 */
class Member {
    private String name;

    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

/**
 * Abstract class representing a gym membership plan.
 * Base rate is ₹1,000 per month across all plans.
 */
abstract class MembershipPlan {
    public static final double BASE_RATE = 1000.0; // ₹1,000/month base rate

    private final String planName;
    private final int durationMonths;

    public MembershipPlan(String planName, int durationMonths) {
        this.planName = planName;
        this.durationMonths = durationMonths;
    }

    public String getPlanName() {
        return planName;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    /**
     * Calculates the total fee for this membership plan.
     *
     * @return fee in ₹
     */
    public abstract double calculateFee();
}

/**
 * Monthly Plan: 1 month duration at full price (₹1,000).
 */
class MonthlyPlan extends MembershipPlan {
    public MonthlyPlan() {
        super("Monthly", 1);
    }

    @Override
    public double calculateFee() {
        return getDurationMonths() * BASE_RATE; // 1 * ₹1,000 = ₹1,000.00
    }
}

/**
 * Quarterly Plan: 3 months duration with 10% discount (₹2,700).
 */
class QuarterlyPlan extends MembershipPlan {
    public QuarterlyPlan() {
        super("Quarterly", 3);
    }

    @Override
    public double calculateFee() {
        double standardPrice = getDurationMonths() * BASE_RATE; // 3 * ₹1,000 = ₹3,000.00
        return standardPrice * 0.90; // 10% off = ₹2,700.00
    }
}

/**
 * Annual Plan: 12 months duration with 25% discount (₹9,000).
 */
class AnnualPlan extends MembershipPlan {
    public AnnualPlan() {
        super("Annual", 12);
    }

    @Override
    public double calculateFee() {
        double standardPrice = getDurationMonths() * BASE_RATE; // 12 * ₹1,000 = ₹12,000.00
        return standardPrice * 0.75; // 25% off = ₹9,000.00
    }
}

/**
 * Membership class managing member status and gym check-ins.
 */
class Membership {
    /**
     * Status enum representing lifecycle states of a membership.
     */
    public enum Status {
        ACTIVE("Active"),
        FROZEN("Frozen"),
        EXPIRED("Expired");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private final Member member;
    private final MembershipPlan plan;
    private Status status;

    public Membership(Member member, MembershipPlan plan) {
        this.member = member;
        this.plan = plan;
        this.status = Status.ACTIVE;
        System.out.printf(Locale.US, "%s membership created for %s. Fee: ₹%,.2f. Status: %s.%n",
                plan.getPlanName(), member.getName(), plan.calculateFee(), status);
    }

    public Member getMember() {
        return member;
    }

    public MembershipPlan getPlan() {
        return plan;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Check in member to gym. Only allowed when membership is ACTIVE.
     *
     * @return true if check-in successful, false otherwise
     */
    public boolean checkIn() {
        if (status == Status.ACTIVE) {
            System.out.printf("%s checked in successfully.%n", member.getName());
            return true;
        } else {
            System.out.printf("Check-in denied: %s's membership is %s.%n", member.getName(), status);
            return false;
        }
    }

    /**
     * Freeze membership. Valid only from ACTIVE status.
     *
     * @return true if freeze successful, false otherwise
     */
    public boolean freeze() {
        if (status == Status.EXPIRED) {
            System.out.println("Cannot freeze an Expired membership.");
            return false;
        }
        if (status == Status.FROZEN) {
            System.out.println("Membership is already Frozen.");
            return false;
        }
        status = Status.FROZEN;
        System.out.printf("%s's membership frozen. Status: %s.%n", member.getName(), status);
        return true;
    }

    /**
     * Unfreeze membership. Valid only from FROZEN status.
     *
     * @return true if unfreeze successful, false otherwise
     */
    public boolean unfreeze() {
        if (status == Status.EXPIRED) {
            System.out.println("Cannot unfreeze an Expired membership.");
            return false;
        }
        if (status == Status.ACTIVE) {
            System.out.println("Membership is already Active.");
            return false;
        }
        status = Status.ACTIVE;
        System.out.printf("%s's membership unfrozen. Status: %s.%n", member.getName(), status);
        return true;
    }

    /**
     * Expire membership. Valid transition from ACTIVE or FROZEN status.
     *
     * @return true if expire successful, false otherwise
     */
    public boolean expire() {
        if (status == Status.EXPIRED) {
            System.out.println("Cannot expire an already Expired membership.");
            return false;
        }
        status = Status.EXPIRED;
        System.out.printf("%s's membership expired. Status: %s.%n", member.getName(), status);
        return true;
    }
}

/**
 * FitZone Membership Desk main application demonstrating sample operations.
 */
public class Q4_FitZoneMembershipDesk {
    public static void main(String[] args) {
        // Asha creates a Quarterly membership
        Member asha = new Member("Asha");
        Membership ashaMembership = new Membership(asha, new QuarterlyPlan());

        // Ravi creates a Monthly membership
        Member ravi = new Member("Ravi");
        Membership raviMembership = new Membership(ravi, new MonthlyPlan());

        // Asha checks in (ACTIVE -> success)
        ashaMembership.checkIn();

        // Asha freezes her membership (ACTIVE -> FROZEN)
        ashaMembership.freeze();

        // Asha attempts to check in while frozen (FROZEN -> denied)
        ashaMembership.checkIn();

        // Ravi's membership expires (ACTIVE -> EXPIRED)
        raviMembership.expire();

        // Attempting to freeze Ravi's expired membership (EXPIRED -> freeze is invalid)
        raviMembership.freeze();
    }
}
