import java.util.Arrays;

/**
 * Problem: Class Topper Finder
 * 
 * Given a 2D array where each row represents a student and each column represents
 * marks obtained in a subject, find the student (row index) with the highest total marks.
 * If there is a tie, return the student with the smallest row index.
 * 
 * Time & Space Complexity Analysis:
 * ---------------------------------
 * - Time Complexity: O(m * n), where:
 *     m = number of students (rows)
 *     n = number of subjects (columns per student)
 *   Outer loop runs m times (once for each student).
 *   Inner loop runs n times for each student to sum up subject scores.
 *   Total operations: m * n, which is O(m * n) linear in terms of total entries.
 * 
 * - Space Complexity: O(1) auxiliary space:
 *   Only a constant amount of extra memory is used for tracking variables
 *   (topperIndex, maxTotal, currentTotal, loop indices). No additional data
 *   structures proportional to input size are allocated.
 */
public class Q1_ClassTopperFinder {

    /**
     * Finds the topper student and their total marks.
     *
     * @param marks 2D array representing marks of m students across n subjects.
     * @return int array [topperRowIndex, maxTotalMarks], or [-1, -1] if input is invalid/empty.
     */
    public static int[] findTopper(int[][] marks) {
        if (marks == null || marks.length == 0) {
            return new int[]{-1, -1};
        }

        int topperIndex = -1;
        int maxTotal = Integer.MIN_VALUE;

        // Outer loop: iterate through each student (row)
        for (int i = 0; i < marks.length; i++) {
            int currentTotal = 0;

            // Inner loop: sum marks of all subjects for the current student
            if (marks[i] != null) {
                for (int j = 0; j < marks[i].length; j++) {
                    currentTotal += marks[i][j];
                }
            }

            // Strictly greater comparison ensures tie-breaking selects smallest row index
            if (currentTotal > maxTotal) {
                maxTotal = currentTotal;
                topperIndex = i;
            }
        }

        return new int[]{topperIndex, maxTotal};
    }

    /**
     * Helper method to print test results cleanly.
     */
    private static void printResult(String testName, int[][] marks) {
        System.out.println("--- " + testName + " ---");
        System.out.println("Marks Array:");
        if (marks != null) {
            for (int i = 0; i < marks.length; i++) {
                System.out.println("  Student " + i + ": " + Arrays.toString(marks[i]));
            }
        } else {
            System.out.println("  null");
        }

        int[] result = findTopper(marks);
        System.out.println("Output: (" + result[0] + ", " + result[1] + ")");
        if (result[0] != -1) {
            System.out.println("Result: Student " + result[0] + " is the topper with total marks = " + result[1]);
        } else {
            System.out.println("Result: No valid student data found.");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Test Case 1: Example from problem statement
        // Student 0: 78 + 85 + 90 = 253
        // Student 1: 88 + 92 + 79 = 259 (Topper)
        // Student 2: 65 + 70 + 95 = 230
        int[][] exampleMarks = {
            {78, 85, 90},
            {88, 92, 79},
            {65, 70, 95}
        };
        printResult("Test Case 1 (Sample Example)", exampleMarks);

        // Test Case 2: Tie-breaking case (Student 0 and Student 2 both have total = 270)
        // Student 0: 90 + 90 + 90 = 270
        // Student 1: 80 + 85 + 90 = 255
        // Student 2: 100 + 85 + 85 = 270
        // Expected output: (0, 270) because row index 0 is smaller than 2
        int[][] tieMarks = {
            {90, 90, 90},
            {80, 85, 90},
            {100, 85, 85}
        };
        printResult("Test Case 2 (Tie-breaking: Smallest Row Index)", tieMarks);

        // Test Case 3: Single student
        // Student 0: 95 + 92 + 88 + 99 = 374
        // Expected output: (0, 374)
        int[][] singleStudentMarks = {
            {95, 92, 88, 99}
        };
        printResult("Test Case 3 (Single Student)", singleStudentMarks);

        // Test Case 4: Multiple students with varying scores (Topper at the end)
        // Student 0: 50 + 60 = 110
        // Student 1: 70 + 75 = 145
        // Student 2: 80 + 80 = 160
        // Student 3: 98 + 95 = 193 (Topper)
        int[][] lastStudentTopperMarks = {
            {50, 60},
            {70, 75},
            {80, 80},
            {98, 95}
        };
        printResult("Test Case 4 (Topper at the Last Row)", lastStudentTopperMarks);
    }
}
