/**
 * Employee Leave Request Workflow System
 *
 * Requirements:
 * - Abstract Employee class with subclasses: FullTimeEmployee, PartTimeEmployee, Contractor.
 * - Max leave days per employee type: FullTime (20), PartTime (10), Contractor (5).
 * - LeaveRequest class with status enum: PENDING, APPROVED, REJECTED.
 * - State transitions:
 *     PENDING  -> APPROVED (valid)
 *     PENDING  -> REJECTED (valid)
 *     APPROVED -> PENDING  (invalid)
 *     REJECTED -> PENDING  (invalid)
 * - Manager / Reviewer role to approve or reject leave requests.
 * - Workflow: Submit leave request, review process, state enforcement.
 */

// ==========================================
// 1. Leave Status Enum
// ==========================================
enum LeaveStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    LeaveStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

// ==========================================
// 2. Leave Request Class
// ==========================================
class LeaveRequest {
    private final Employee employee;
    private final String dateRange;
    private final int numberOfDays;
    private LeaveStatus status;

    public LeaveRequest(Employee employee, String dateRange, int numberOfDays) {
        this.employee = employee;
        this.dateRange = dateRange;
        this.numberOfDays = numberOfDays;
        this.status = LeaveStatus.PENDING;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getDateRange() {
        return dateRange;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    /**
     * Enforces valid state transitions:
     * - PENDING -> APPROVED : Valid
     * - PENDING -> REJECTED : Valid
     * - APPROVED -> PENDING : Invalid
     * - REJECTED -> PENDING : Invalid
     * - Any transition once already finalized (Approved/Rejected) is invalid.
     *
     * @param newStatus the target status to transition to
     * @return true if status was updated successfully, false otherwise
     */
    public boolean setStatus(LeaveStatus newStatus) {
        if (this.status == newStatus) {
            return true;
        }

        // Check invalid transitions from APPROVED or REJECTED to PENDING
        if (this.status == LeaveStatus.APPROVED && newStatus == LeaveStatus.PENDING) {
            System.out.println("Cannot change leave request status from Approved to Pending.");
            return false;
        }
        if (this.status == LeaveStatus.REJECTED && newStatus == LeaveStatus.PENDING) {
            System.out.println("Cannot change leave request status from Rejected to Pending.");
            return false;
        }

        // Allow transitions only from PENDING to APPROVED or REJECTED
        if (this.status == LeaveStatus.PENDING && (newStatus == LeaveStatus.APPROVED || newStatus == LeaveStatus.REJECTED)) {
            this.status = newStatus;
            return true;
        }

        // Any other transition between finalized states
        System.out.println("Cannot change leave request status from " + this.status + " to " + newStatus + ".");
        return false;
    }
}

// ==========================================
// 3. Reviewer Interface
// ==========================================
interface Reviewer {
    boolean approveLeave(LeaveRequest request);
    boolean rejectLeave(LeaveRequest request);
    boolean reviewLeave(LeaveRequest request, LeaveStatus newStatus);
}

// ==========================================
// 4. Abstract Employee Class
// ==========================================
abstract class Employee {
    private final String id;
    private final String name;
    private final int maxLeaveDays;
    private int leaveDaysTaken;

    public Employee(String id, String name, int maxLeaveDays) {
        this.id = id;
        this.name = name;
        this.maxLeaveDays = maxLeaveDays;
        this.leaveDaysTaken = 0;
    }

    public Employee(String name, int maxLeaveDays) {
        this("EMP-" + name.toLowerCase(), name, maxLeaveDays);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxLeaveDays() {
        return maxLeaveDays;
    }

    public int getLeaveDaysTaken() {
        return leaveDaysTaken;
    }

    public int getRemainingLeaveDays() {
        return maxLeaveDays - leaveDaysTaken;
    }

    public void deductLeaveDays(int days) {
        this.leaveDaysTaken += days;
    }

    public abstract String getEmployeeType();

    /**
     * Submits a leave request for the employee.
     *
     * @param dateRange the date range string (e.g., "Jan 1-5")
     * @param numberOfDays the number of days requested
     * @return the created LeaveRequest in PENDING status
     */
    public LeaveRequest submitLeaveRequest(String dateRange, int numberOfDays) {
        if (numberOfDays <= 0) {
            System.out.println("Leave request days must be greater than zero.");
            return null;
        }
        if (numberOfDays > getRemainingLeaveDays()) {
            System.out.printf("Cannot submit leave request for %s (%s). Requested %d days, but only %d days remaining (Max: %d).%n",
                    name, dateRange, numberOfDays, getRemainingLeaveDays(), maxLeaveDays);
            return null;
        }

        LeaveRequest request = new LeaveRequest(this, dateRange, numberOfDays);
        System.out.println("Leave request submitted for " + name + " (" + dateRange + "). Status: " + request.getStatus() + ".");
        return request;
    }
}

// ==========================================
// 5. Employee Subclasses
// ==========================================

/**
 * Full-time employee with max 20 leave days.
 */
class FullTimeEmployee extends Employee {
    private static final int MAX_LEAVE_DAYS = 20;

    public FullTimeEmployee(String id, String name) {
        super(id, name, MAX_LEAVE_DAYS);
    }

    public FullTimeEmployee(String name) {
        super(name, MAX_LEAVE_DAYS);
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time Employee";
    }
}

/**
 * Part-time employee with max 10 leave days.
 */
class PartTimeEmployee extends Employee {
    private static final int MAX_LEAVE_DAYS = 10;

    public PartTimeEmployee(String id, String name) {
        super(id, name, MAX_LEAVE_DAYS);
    }

    public PartTimeEmployee(String name) {
        super(name, MAX_LEAVE_DAYS);
    }

    @Override
    public String getEmployeeType() {
        return "Part-Time Employee";
    }
}

/**
 * Contractor with max 5 leave days.
 */
class Contractor extends Employee {
    private static final int MAX_LEAVE_DAYS = 5;

    public Contractor(String id, String name) {
        super(id, name, MAX_LEAVE_DAYS);
    }

    public Contractor(String name) {
        super(name, MAX_LEAVE_DAYS);
    }

    @Override
    public String getEmployeeType() {
        return "Contractor";
    }
}

// ==========================================
// 6. Manager / Reviewer Implementation
// ==========================================
class Manager extends FullTimeEmployee implements Reviewer {

    public Manager(String id, String name) {
        super(id, name);
    }

    public Manager(String name) {
        super(name);
    }

    @Override
    public boolean approveLeave(LeaveRequest request) {
        if (request == null) {
            System.out.println("Invalid leave request.");
            return false;
        }

        boolean updated = request.setStatus(LeaveStatus.APPROVED);
        if (updated) {
            request.getEmployee().deductLeaveDays(request.getNumberOfDays());
            System.out.printf("%s's leave request (%s) approved. Status: %s.%n",
                    request.getEmployee().getName(),
                    request.getDateRange(),
                    request.getStatus());
        }
        return updated;
    }

    @Override
    public boolean rejectLeave(LeaveRequest request) {
        if (request == null) {
            System.out.println("Invalid leave request.");
            return false;
        }

        boolean updated = request.setStatus(LeaveStatus.REJECTED);
        if (updated) {
            System.out.printf("%s's leave request (%s) rejected. Status: %s.%n",
                    request.getEmployee().getName(),
                    request.getDateRange(),
                    request.getStatus());
        }
        return updated;
    }

    @Override
    public boolean reviewLeave(LeaveRequest request, LeaveStatus newStatus) {
        if (request == null) {
            System.out.println("Invalid leave request.");
            return false;
        }
        if (newStatus == LeaveStatus.APPROVED) {
            return approveLeave(request);
        } else if (newStatus == LeaveStatus.REJECTED) {
            return rejectLeave(request);
        } else {
            return request.setStatus(newStatus);
        }
    }
}

// ==========================================
// 7. Main System Class
// ==========================================
public class Q2_EmployeeLeaveSystem {

    public static void main(String[] args) {
        // Step 1: Create employees and manager
        Employee john = new FullTimeEmployee("EMP001", "John");
        Employee jane = new PartTimeEmployee("EMP002", "Jane");
        Manager manager = new Manager("MGR001", "Sarah");

        // Step 2: Submit leave request for John (Jan 1-5, 5 days)
        LeaveRequest johnRequest = john.submitLeaveRequest("Jan 1-5", 5);

        // Step 3: Manager approves John's leave request
        manager.approveLeave(johnRequest);

        // Step 4: Submit leave request for Jane (Feb 10-11, 2 days)
        LeaveRequest janeRequest = jane.submitLeaveRequest("Feb 10-11", 2);

        // Step 5: Manager rejects Jane's leave request
        manager.rejectLeave(janeRequest);

        // Step 6: Attempt invalid status transition (Approved -> Pending)
        johnRequest.setStatus(LeaveStatus.PENDING);
    }
}
