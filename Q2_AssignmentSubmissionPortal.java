import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Assignment Submission Portal System
 *
 * Implements an academic assignment submission portal featuring:
 * - Student model
 * - Polymorphic Assignment hierarchy (CodingAssignment: 10% penalty/day, WrittenAssignment: 20% penalty/day)
 * - Submission tracking with encapsulated status (SUBMITTED, GRADED)
 * - Automated late penalty calculation applied to awarded marks
 * - Resubmission restrictions preventing resubmission once an assignment is graded
 */

// ============================================================================
// 1. Student Class
// ============================================================================
/**
 * Represents a student submitting assignments in the portal.
 */
class Student {
    private final String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

// ============================================================================
// 2. Status Enum
// ============================================================================
/**
 * Represents the lifecycle status of an assignment submission.
 * Enforces strict state transitions that cannot be directly modified externally.
 */
enum Status {
    SUBMITTED("Submitted"),
    GRADED("Graded");

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

// ============================================================================
// 3. Assignment Hierarchy (Abstract Base Class & Polymorphic Subclasses)
// ============================================================================
/**
 * Abstract base class for assignments with due dates and late penalty rules.
 */
abstract class Assignment {
    private final String title;
    private final double maxMarks;
    private final LocalDate dueDate;

    public Assignment(String title, double maxMarks, LocalDate dueDate) {
        this.title = title;
        this.maxMarks = maxMarks;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public double getMaxMarks() {
        return maxMarks;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Returns the late penalty rate per day as a decimal fraction (e.g., 0.10 for 10%).
     * Subclasses define their specific penalty rate.
     */
    public abstract double getLatePenaltyRatePerDay();

    /**
     * Calculates the number of days late relative to the due date.
     * Returns 0 if submitted on or before the due date.
     *
     * @param submissionDate The date the assignment was submitted
     * @return Number of days late (non-negative)
     */
    public long calculateLateDays(LocalDate submissionDate) {
        if (submissionDate == null || !submissionDate.isAfter(dueDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, submissionDate);
    }

    /**
     * Calculates the late penalty percentage based on days late.
     * Capped at 1.0 (100%).
     *
     * @param submissionDate The date the assignment was submitted
     * @return Late penalty fraction between 0.0 and 1.0
     */
    public double calculatePenaltyPercentage(LocalDate submissionDate) {
        long lateDays = calculateLateDays(submissionDate);
        if (lateDays <= 0) {
            return 0.0;
        }
        return Math.min(1.0, lateDays * getLatePenaltyRatePerDay());
    }

    /**
     * Calculates the final marks after deducting late penalty from awarded marks.
     * Penalty is applied to awarded marks: finalMarks = awardedMarks - (awardedMarks * penalty%).
     *
     * @param awardedMarks Marks awarded to the student before late penalty
     * @param submissionDate The date of submission
     * @return Final marks after penalty deduction (floored at 0.0)
     */
    public double calculateFinalMarks(double awardedMarks, LocalDate submissionDate) {
        double penaltyFraction = calculatePenaltyPercentage(submissionDate);
        double penaltyDeduction = awardedMarks * penaltyFraction;
        return Math.max(0.0, awardedMarks - penaltyDeduction);
    }

    @Override
    public String toString() {
        return title + " (Max: " + maxMarks + ", Due: " + dueDate + ")";
    }
}

/**
 * CodingAssignment: Applies a 10% late penalty per day.
 */
class CodingAssignment extends Assignment {
    private static final double PENALTY_RATE_PER_DAY = 0.10; // 10% penalty per late day

    public CodingAssignment(String title, double maxMarks, LocalDate dueDate) {
        super(title, maxMarks, dueDate);
    }

    @Override
    public double getLatePenaltyRatePerDay() {
        return PENALTY_RATE_PER_DAY;
    }
}

/**
 * WrittenAssignment: Applies a 20% late penalty per day.
 */
class WrittenAssignment extends Assignment {
    private static final double PENALTY_RATE_PER_DAY = 0.20; // 20% penalty per late day

    public WrittenAssignment(String title, double maxMarks, LocalDate dueDate) {
        super(title, maxMarks, dueDate);
    }

    @Override
    public double getLatePenaltyRatePerDay() {
        return PENALTY_RATE_PER_DAY;
    }
}

// ============================================================================
// 4. Submission Class
// ============================================================================
/**
 * Represents an assignment submission by a student.
 * Encapsulates status, grading, and resubmission validation.
 */
class Submission {
    private final Student student;
    private final Assignment assignment;
    private LocalDate submissionDate;
    private Status status;
    private double awardedMarks;
    private double finalMarks;

    public Submission(Student student, Assignment assignment, LocalDate submissionDate) {
        this.student = student;
        this.assignment = assignment;
        this.submissionDate = submissionDate;
        this.status = Status.SUBMITTED;
        this.awardedMarks = 0.0;
        this.finalMarks = 0.0;
        printSubmissionReceipt();
    }

    public Student getStudent() {
        return student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Returns the current status of the submission.
     * Note: Status cannot be modified directly from outside, preserving encapsulation.
     */
    public Status getStatus() {
        return status;
    }

    public double getAwardedMarks() {
        return awardedMarks;
    }

    public double getFinalMarks() {
        return finalMarks;
    }

    /**
     * Grades the submission by applying any late penalty and calculating final marks.
     * Transitions submission status to GRADED.
     *
     * @param awardedMarks Marks awarded before penalty
     */
    public void grade(double awardedMarks) {
        this.awardedMarks = awardedMarks;
        double penaltyFraction = assignment.calculatePenaltyPercentage(submissionDate);
        this.finalMarks = assignment.calculateFinalMarks(awardedMarks, submissionDate);
        this.status = Status.GRADED;

        String finalMarksFormatted = formatMarks(finalMarks);
        String maxMarksFormatted = formatMarks(assignment.getMaxMarks());

        if (penaltyFraction > 0.0) {
            int penaltyPercent = (int) Math.round(penaltyFraction * 100);
            System.out.println(student.getName() + " graded: " + finalMarksFormatted + "/"
                    + maxMarksFormatted + " after " + penaltyPercent + "% late penalty. Status: " + status + ".");
        } else {
            System.out.println(student.getName() + " graded: " + finalMarksFormatted + "/"
                    + maxMarksFormatted + ". Status: " + status + ".");
        }
    }

    /**
     * Resubmits the assignment with a new submission date.
     * Once GRADED, resubmissions are rejected.
     *
     * @param newSubmissionDate The new submission date
     * @return true if resubmission was accepted, false otherwise
     */
    public boolean resubmit(LocalDate newSubmissionDate) {
        if (this.status == Status.GRADED) {
            System.out.println("Cannot resubmit: '" + assignment.getTitle() + "' has already been graded.");
            return false;
        }
        this.submissionDate = newSubmissionDate;
        this.status = Status.SUBMITTED;
        printSubmissionReceipt();
        return true;
    }

    /**
     * Overload to resubmit using the current system date.
     */
    public boolean resubmit() {
        return resubmit(LocalDate.now());
    }

    /**
     * Prints receipt indicating assignment receipt and whether it was submitted on time or late.
     */
    public void printSubmissionReceipt() {
        long lateDays = assignment.calculateLateDays(submissionDate);
        String timing;
        if (lateDays <= 0) {
            timing = "(on time)";
        } else if (lateDays == 1) {
            timing = "(1 day late)";
        } else {
            timing = "(" + lateDays + " days late)";
        }

        System.out.println(student.getName() + "'s submission for '" + assignment.getTitle()
                + "' received " + timing + ". Status: " + status + ".");
    }

    /**
     * Helper method to format marks without unnecessary decimal zeros.
     */
    private static String formatMarks(double marks) {
        if (marks == (long) marks) {
            return String.valueOf((long) marks);
        }
        return String.format(Locale.US, "%.1f", marks);
    }
}

// ============================================================================
// 5. Assignment Submission Portal Helper
// ============================================================================
/**
 * Portal service managing assignment creation and submission workflows.
 */
class AssignmentSubmissionPortal {
    public static Submission submit(Student student, Assignment assignment, LocalDate submissionDate) {
        return new Submission(student, assignment, submissionDate);
    }
}

// ============================================================================
// 6. Main Class & Demonstration
// ============================================================================
public class Q2_AssignmentSubmissionPortal {
    public static void main(String[] args) {
        // 1. Initialize Students
        Student asha = new Student("Asha");
        Student ravi = new Student("Ravi");

        // 2. Initialize Assignments
        // Linked List Lab (CodingAssignment) due on 2026-10-15, max 50 marks
        LocalDate codingDueDate = LocalDate.of(2026, 10, 15);
        Assignment linkedListLab = new CodingAssignment("Linked List Lab", 50, codingDueDate);

        // Design Essay (WrittenAssignment) due on 2026-10-15, max 50 marks
        LocalDate writtenDueDate = LocalDate.of(2026, 10, 15);
        Assignment designEssay = new WrittenAssignment("Design Essay", 50, writtenDueDate);

        // 3. Submissions
        // Asha submits on time (2026-10-15)
        Submission ashaSubmission = new Submission(asha, linkedListLab, LocalDate.of(2026, 10, 15));

        // Ravi submits 2 days late (2026-10-17)
        Submission raviSubmission = new Submission(ravi, designEssay, LocalDate.of(2026, 10, 17));

        // 4. Grading
        // Asha graded: 45/50 (on time, 0% penalty)
        ashaSubmission.grade(45);

        // Ravi graded: 40 awarded marks -> 40% penalty (2 days * 20%) -> 24/50
        raviSubmission.grade(40);

        // 5. Attempt resubmission after grading (should be rejected)
        ashaSubmission.resubmit(LocalDate.of(2026, 10, 18));
    }
}
