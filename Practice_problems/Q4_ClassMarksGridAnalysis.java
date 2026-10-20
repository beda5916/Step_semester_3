/**
 * ============================================================================
 * Problem 4: Class Marks Grid Analysis
 * ============================================================================
 * Create a program to manage and analyze student marks in multiple subjects,
 * stored in a 2D array (grid). For a given set of student marks, calculate
 * and report:
 *   1. Each student's total marks.
 *   2. The average marks for each subject (formatted to two decimal places).
 *   3. The student with the highest total marks (the topper).
 *
 * Algorithm:
 * ----------
 * 1. Traverse each row (student) to compute total marks per student.
 * 2. Traverse each column (subject) to compute subject averages.
 * 3. Track the maximum total to identify the topper.
 *
 * Complexity Analysis:
 * --------------------
 * - Time Complexity: O(m * n)
 *   where m = number of students and n = number of subjects.
 *   We traverse the entire 2D grid to compute totals and averages.
 *   Finding the topper is O(m) since we compare pre-computed totals.
 *   Overall: O(m * n) + O(m) = O(m * n).
 *
 * - Auxiliary Space Complexity: O(m + n)
 *   O(m) for storing per-student totals.
 *   O(n) for storing per-subject sums (used to compute averages).
 *   The result strings are proportional to m + n as well.
 * ============================================================================
 */
public class Q4_ClassMarksGridAnalysis {

    /**
     * Represents the result of the grid analysis.
     */
    static class AnalysisResult {
        String totalsLine;
        String averagesLine;
        String topperLine;
    }

    /**
     * Analyzes the marks grid and produces totals, averages, and topper information.
     *
     * @param names Array of student names (length m).
     * @param marks 2D array of marks (m students x n subjects).
     * @return AnalysisResult containing the three output lines.
     */
    public static AnalysisResult analyzeMarks(String[] names, int[][] marks) {
        AnalysisResult result = new AnalysisResult();

        if (names == null || marks == null || names.length == 0 || marks.length == 0) {
            result.totalsLine = "Totals (none)";
            result.averagesLine = "averages (none)";
            result.topperLine = "topper (none)";
            return result;
        }

        int m = names.length;       // number of students
        int n = marks[0].length;    // number of subjects

        // Step 1: Compute total marks per student
        int[] totals = new int[m];
        for (int i = 0; i < m; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += marks[i][j];
            }
            totals[i] = sum;
        }

        // Build totals line: "Totals Asha 253, Ravi 259, Neha 230"
        StringBuilder totalsBuilder = new StringBuilder("Totals ");
        for (int i = 0; i < m; i++) {
            if (i > 0) {
                totalsBuilder.append(", ");
            }
            totalsBuilder.append(names[i]).append(" ").append(totals[i]);
        }
        result.totalsLine = totalsBuilder.toString();

        // Step 2: Compute average marks per subject
        double[] subjectAverages = new double[n];
        for (int j = 0; j < n; j++) {
            int colSum = 0;
            for (int i = 0; i < m; i++) {
                colSum += marks[i][j];
            }
            subjectAverages[j] = (double) colSum / m;
        }

        // Build averages line: "averages 77.00, 82.33, 88.00"
        StringBuilder avgBuilder = new StringBuilder("averages ");
        for (int j = 0; j < n; j++) {
            if (j > 0) {
                avgBuilder.append(", ");
            }
            avgBuilder.append(String.format("%.2f", subjectAverages[j]));
        }
        result.averagesLine = avgBuilder.toString();

        // Step 3: Find the topper (student with highest total)
        int topperIndex = 0;
        for (int i = 1; i < m; i++) {
            if (totals[i] > totals[topperIndex]) {
                topperIndex = i;
            }
        }
        result.topperLine = "topper " + names[topperIndex] + " (" + totals[topperIndex] + ")";

        return result;
    }

    // ==================== Helper Method ====================

    private static void runTestCase(String testName, String[] names, int[][] marks,
                                     String expectedTotals, String expectedAverages, String expectedTopper) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("Input:");
        if (names != null && marks != null) {
            for (int i = 0; i < names.length; i++) {
                StringBuilder sb = new StringBuilder("  " + names[i] + " [");
                for (int j = 0; j < marks[i].length; j++) {
                    if (j > 0) sb.append(", ");
                    sb.append(marks[i][j]);
                }
                sb.append("]");
                System.out.println(sb.toString());
            }
        }

        AnalysisResult result = analyzeMarks(names, marks);

        System.out.println("Expected:");
        System.out.println("  " + expectedTotals);
        System.out.println("  " + expectedAverages);
        System.out.println("  " + expectedTopper);
        System.out.println("Actual:");
        System.out.println("  " + result.totalsLine);
        System.out.println("  " + result.averagesLine);
        System.out.println("  " + result.topperLine);

        boolean passed = expectedTotals.equals(result.totalsLine)
                      && expectedAverages.equals(result.averagesLine)
                      && expectedTopper.equals(result.topperLine);
        System.out.println("Status: " + (passed ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("       CLASS MARKS GRID ANALYSIS - TEST SUITE             ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        // Asha: 78+85+90 = 253, Ravi: 88+92+79 = 259, Neha: 65+70+95 = 230
        // Subject averages: (78+88+65)/3=77.00, (85+92+70)/3=82.33, (90+79+95)/3=88.00
        // Topper: Ravi (259)
        runTestCase("Sample Problem",
            new String[]{"Asha", "Ravi", "Neha"},
            new int[][]{
                {78, 85, 90},
                {88, 92, 79},
                {65, 70, 95}
            },
            "Totals Asha 253, Ravi 259, Neha 230",
            "averages 77.00, 82.33, 88.00",
            "topper Ravi (259)"
        );

        // Test Case 2: Single student
        // Dev: 90+95+88 = 273
        // Subject averages: 90.00, 95.00, 88.00
        // Topper: Dev (273)
        runTestCase("Single Student",
            new String[]{"Dev"},
            new int[][]{
                {90, 95, 88}
            },
            "Totals Dev 273",
            "averages 90.00, 95.00, 88.00",
            "topper Dev (273)"
        );

        // Test Case 3: Tie-breaking — first student with highest total wins
        // Asha: 80+80+80 = 240, Ravi: 70+90+80 = 240
        // Both have same total; Asha appears first so she is the topper
        runTestCase("Tie-Breaking (First Student Wins)",
            new String[]{"Asha", "Ravi"},
            new int[][]{
                {80, 80, 80},
                {70, 90, 80}
            },
            "Totals Asha 240, Ravi 240",
            "averages 75.00, 85.00, 80.00",
            "topper Asha (240)"
        );

        // Test Case 4: Topper at last position
        // A: 50+50+50=150, B: 60+60+60=180, C: 90+90+90=270
        runTestCase("Topper at Last Position",
            new String[]{"A", "B", "C"},
            new int[][]{
                {50, 50, 50},
                {60, 60, 60},
                {90, 90, 90}
            },
            "Totals A 150, B 180, C 270",
            "averages 66.67, 66.67, 66.67",
            "topper C (270)"
        );

        // Test Case 5: All zeros
        runTestCase("All Zeros",
            new String[]{"X", "Y"},
            new int[][]{
                {0, 0, 0},
                {0, 0, 0}
            },
            "Totals X 0, Y 0",
            "averages 0.00, 0.00, 0.00",
            "topper X (0)"
        );
    }
}
