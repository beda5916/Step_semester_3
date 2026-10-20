import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ============================================================================
 * Problem 3: Student Club Registration Management
 * ============================================================================
 * Develop a student club registration system that stores members by their
 * unique roll number. The system must prevent duplicate registrations for the
 * same student, even if the registration details arrive as a new object instance.
 *
 * Key Requirement:
 * - Only the roll number is considered for uniqueness.
 * - Override equals() and hashCode() in the Student class so that a HashSet
 *   correctly identifies duplicates based solely on roll number.
 *
 * OOP Concepts Demonstrated:
 * - equals() and hashCode() contract
 * - HashSet for unique element storage
 * - Encapsulation (private fields with getters)
 * - Object identity vs object equality (== vs equals)
 *
 * Complexity Analysis:
 * --------------------
 * - ADD operation: O(1) average (HashSet add with O(1) hash lookup).
 * - CONTAINS operation: O(1) average (HashSet contains with O(1) hash lookup).
 * - Overall for n operations: O(n).
 * - Space Complexity: O(k) where k = number of unique students registered.
 *
 * Why equals() and hashCode() Override is Essential:
 * --------------------------------------------------
 * Without overriding, HashSet uses Object's default reference-based equality.
 * Two Student objects with the same roll number but created via 'new' would be
 * treated as distinct entries, allowing duplicates. By overriding equals() to
 * compare roll numbers and hashCode() to hash the roll number, HashSet correctly
 * detects and rejects duplicate registrations regardless of object identity.
 * ============================================================================
 */
public class Q3_StudentClubRegistration {

    // ==================== Student Class ====================

    /**
     * Represents a student member with a roll number and name.
     * Equality is determined solely by roll number (not name).
     */
    static class Student {
        private final String rollNumber;
        private final String name;

        public Student(String rollNumber, String name) {
            this.rollNumber = rollNumber;
            this.name = name;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public String getName() {
            return name;
        }

        /**
         * Two Student objects are equal if and only if they have the same roll number.
         * Name is intentionally excluded to match the problem's uniqueness requirement.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return Objects.equals(rollNumber, student.rollNumber);
        }

        /**
         * hashCode must be consistent with equals: based only on roll number.
         * This ensures that HashSet/HashMap correctly bucket students by roll number.
         */
        @Override
        public int hashCode() {
            return Objects.hash(rollNumber);
        }

        @Override
        public String toString() {
            return "Student{rollNumber='" + rollNumber + "', name='" + name + "'}";
        }
    }

    // ==================== System Processor ====================

    /**
     * Processes a series of ADD and CONTAINS operations on a student club registry.
     *
     * @param operations Array of operation strings.
     * @return Array of output strings.
     */
    public static String[] processOperations(String[] operations) {
        Set<Student> members = new HashSet<>();
        StringBuilder output = new StringBuilder();

        // First pass: process all ADD operations
        for (String op : operations) {
            String[] parts = op.split(" ");
            String command = parts[0];

            if (command.equals("ADD")) {
                // ADD <RollNumber> <Name>
                Student student = new Student(parts[1], parts[2]);
                boolean added = members.add(student);
                output.append(added ? "Added" : "duplicate rejected").append("\n");
            }
        }

        // After all ADD operations, print member count
        output.append("member count " + members.size()).append("\n");

        // Second pass: process all CONTAINS operations
        for (String op : operations) {
            String[] parts = op.split(" ");
            String command = parts[0];

            if (command.equals("CONTAINS")) {
                // CONTAINS <RollNumber> <Name>
                Student query = new Student(parts[1], parts[2]);
                boolean found = members.contains(query);
                output.append("contains: " + found).append("\n");
            }
        }

        String result = output.toString().trim();
        return result.isEmpty() ? new String[0] : result.split("\n");
    }

    // ==================== Helper Method ====================

    private static void runTestCase(String testName, String[] operations, String[] expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("Operations:");
        for (String op : operations) {
            System.out.println("  > " + op);
        }

        String[] actual = processOperations(operations);

        System.out.println("Expected Output:");
        for (String line : expected) {
            System.out.println("  " + line);
        }
        System.out.println("Actual Output:");
        for (String line : actual) {
            System.out.println("  " + line);
        }

        boolean passed = expected.length == actual.length;
        if (passed) {
            for (int i = 0; i < expected.length; i++) {
                if (!expected[i].equals(actual[i])) {
                    passed = false;
                    break;
                }
            }
        }
        System.out.println("Status: " + (passed ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("    STUDENT CLUB REGISTRATION MANAGEMENT - TEST SUITE     ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        runTestCase("Sample Problem",
            new String[]{
                "ADD 21CS01 Asha",
                "ADD 21CS01 Asha",
                "ADD 21CS02 Ravi",
                "CONTAINS 21CS02 Ravi",
                "CONTAINS 21CS03 Priya"
            },
            new String[]{
                "Added",
                "duplicate rejected",
                "Added",
                "member count 2",
                "contains: true",
                "contains: false"
            }
        );

        // Test Case 2: Same roll number, different name (still a duplicate)
        // This verifies that only roll number is used for uniqueness
        runTestCase("Duplicate Roll Number with Different Name",
            new String[]{
                "ADD 21EC01 Neha",
                "ADD 21EC01 Sneha",
                "CONTAINS 21EC01 Neha"
            },
            new String[]{
                "Added",
                "duplicate rejected",
                "member count 1",
                "contains: true"
            }
        );

        // Test Case 3: All unique students
        runTestCase("All Unique Students",
            new String[]{
                "ADD 21CS01 Asha",
                "ADD 21CS02 Ravi",
                "ADD 21CS03 Priya",
                "ADD 21CS04 Kiran"
            },
            new String[]{
                "Added",
                "Added",
                "Added",
                "Added",
                "member count 4"
            }
        );

        // Test Case 4: Contains check with no members
        runTestCase("Contains on Empty Registry",
            new String[]{
                "CONTAINS 21CS01 Asha"
            },
            new String[]{
                "member count 0",
                "contains: false"
            }
        );

        // Test Case 5: Multiple duplicates of the same student
        runTestCase("Multiple Duplicate Attempts",
            new String[]{
                "ADD 21ME01 Dev",
                "ADD 21ME01 Dev",
                "ADD 21ME01 Dev",
                "CONTAINS 21ME01 Dev"
            },
            new String[]{
                "Added",
                "duplicate rejected",
                "duplicate rejected",
                "member count 1",
                "contains: true"
            }
        );
    }
}
