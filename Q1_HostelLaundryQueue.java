import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Hostel Laundry Queue System
 *
 * Implements an automated laundry management system with:
 * - Student model
 * - WashingMachine model with encapsulated busy/free status
 * - Polymorphic WashType hierarchy (QuickWash, NormalWash, HeavyWash)
 * - WashCycle controller enforcing machine availability constraints and cycle lifecycle
 */

// ============================================================================
// Student Class
// ============================================================================
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
// WashType Hierarchy (Abstract Base Class & Polymorphic Subclasses)
// ============================================================================
abstract class WashType {
    public abstract String getWashName();
    public abstract int getDurationMinutes();
    public abstract double getCharge();
}

class QuickWash extends WashType {
    @Override
    public String getWashName() {
        return "Quick wash";
    }

    @Override
    public int getDurationMinutes() {
        return 30;
    }

    @Override
    public double getCharge() {
        return 20.0;
    }
}

class NormalWash extends WashType {
    @Override
    public String getWashName() {
        return "Normal wash";
    }

    @Override
    public int getDurationMinutes() {
        return 45;
    }

    @Override
    public double getCharge() {
        return 30.0;
    }
}

class HeavyWash extends WashType {
    @Override
    public String getWashName() {
        return "Heavy wash";
    }

    @Override
    public int getDurationMinutes() {
        return 60;
    }

    @Override
    public double getCharge() {
        return 45.0;
    }
}

// ============================================================================
// WashingMachine Class
// ============================================================================
class WashingMachine {
    private final String id;
    private boolean busy;
    private WashCycle currentCycle;

    public WashingMachine(String id) {
        this.id = id;
        this.busy = false;
        this.currentCycle = null;
    }

    public String getId() {
        return id;
    }

    /**
     * Busy/free status is read-only from the outside.
     * Status cannot be directly modified by arbitrary external callers.
     */
    public boolean isBusy() {
        return busy;
    }

    public WashCycle getCurrentCycle() {
        return currentCycle;
    }

    /**
     * Internal state transition: marks the machine as busy for an active cycle.
     * Package-private to protect encapsulation.
     */
    void occupy(WashCycle cycle) {
        this.busy = true;
        this.currentCycle = cycle;
    }

    /**
     * Internal state transition: marks the machine as free upon cycle completion.
     * Package-private to protect encapsulation.
     */
    void release() {
        this.busy = false;
        this.currentCycle = null;
    }

    @Override
    public String toString() {
        return "Machine " + id + " [" + (busy ? "Busy" : "Free") + "]";
    }
}

// ============================================================================
// WashCycle Class
// ============================================================================
class WashCycle {
    private final Student student;
    private final WashingMachine machine;
    private final WashType washType;
    private boolean active;
    private boolean completed;

    public WashCycle(Student student, WashingMachine machine, WashType washType) {
        this.student = student;
        this.machine = machine;
        this.washType = washType;
        this.active = false;
        this.completed = false;
    }

    public Student getStudent() {
        return student;
    }

    public WashingMachine getMachine() {
        return machine;
    }

    public WashType getWashType() {
        return washType;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }

    /**
     * Starts the wash cycle on the assigned machine.
     * If the machine is already busy, the cycle cannot start.
     *
     * @return true if started successfully, false if machine was busy
     */
    public boolean start() {
        if (machine.isBusy()) {
            System.out.println("Machine " + machine.getId() + " is currently busy.");
            return false;
        }

        machine.occupy(this);
        this.active = true;
        System.out.printf("%s started on %s for %s (%d min). Charge: ₹%.2f.%n",
                washType.getWashName(),
                machine.getId(),
                student.getName(),
                washType.getDurationMinutes(),
                washType.getCharge());
        return true;
    }

    /**
     * Completes the wash cycle and frees the machine for next use.
     */
    public void complete() {
        if (!active) {
            System.out.println("No active cycle to complete on machine " + machine.getId() + ".");
            return;
        }

        machine.release();
        this.active = false;
        this.completed = true;
        System.out.println(machine.getId() + " cycle completed. " + machine.getId() + " is now free.");
    }
}

// ============================================================================
// Main Class & Demonstration
// ============================================================================
public class Q1_HostelLaundryQueue {
    public static void main(String[] args) {
        // Configure standard output for UTF-8 to support the ₹ currency symbol across environments
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (Exception ignored) {
        }

        // 1. Initialize washing machines
        WashingMachine m1 = new WashingMachine("M1");
        WashingMachine m2 = new WashingMachine("M2");

        // 2. Initialize students
        Student asha = new Student("Asha");
        Student ravi = new Student("Ravi");
        Student neha = new Student("Neha");

        // Step 1: Quick wash started on M1 for Asha (30 min). Charge: ₹20.00.
        WashCycle cycle1 = new WashCycle(asha, m1, new QuickWash());
        cycle1.start();

        // Step 2: Attempt to start wash on M1 while it is busy -> Machine M1 is currently busy.
        WashCycle cycleAttempt = new WashCycle(ravi, m1, new HeavyWash());
        cycleAttempt.start();

        // Step 3: Heavy wash started on M2 for Ravi (60 min). Charge: ₹45.00.
        WashCycle cycle2 = new WashCycle(ravi, m2, new HeavyWash());
        cycle2.start();

        // Step 4: M1 cycle completed. M1 is now free.
        cycle1.complete();

        // Step 5: Normal wash started on M1 for Neha (45 min). Charge: ₹30.00.
        WashCycle cycle3 = new WashCycle(neha, m1, new NormalWash());
        cycle3.start();
    }
}
