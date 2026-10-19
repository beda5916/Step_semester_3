import java.util.Arrays;

/**
 * ============================================================================
 * Problem: Hot Weather Alert Windows
 * ============================================================================
 * Given an array of temperature readings, an alert window size 'k', and an average
 * temperature 'threshold', this program counts the number of contiguous subarrays
 * (windows) of length 'k' whose average temperature is greater than or equal to
 * 'threshold'.
 *
 * Algorithm (Sliding Window Technique):
 * -------------------------------------
 * 1. Condition Transformation:
 *    - The condition is: (windowSum / k) >= threshold.
 *    - To avoid floating-point division inaccuracies and potential precision loss,
 *      we rewrite this mathematically as: windowSum >= (long) k * threshold.
 *    - Using 'long' prevents integer overflow for large values of k and threshold.
 * 2. Initial Window:
 *    - Compute the sum of the first 'k' temperature readings (indices 0 to k - 1).
 *    - If this initial windowSum >= targetSum, increment the alert count.
 * 3. Sliding the Window:
 *    - For each step from index i = k up to readings.length - 1:
 *      - Add the incoming element entering the window: readings[i].
 *      - Subtract the outgoing element leaving the window: readings[i - k].
 *      - Check if the updated windowSum >= targetSum.
 *      - Increment alert count if the condition is met.
 *
 * Complexity Analysis:
 * --------------------
 * 1. Time Complexity:
 *    - Sliding Window Approach: O(n), where n = readings.length.
 *      - Summing the initial window takes O(k) operations.
 *      - Sliding across the remaining (n - k) elements requires O(1) operations
 *        per window (one addition and one subtraction).
 *      - Total operations: O(k) + O(n - k) = O(n).
 *    - Brute Force Comparison: O(n * k).
 *      - A brute force method would calculate the sum of all k elements from scratch
 *        for each of the (n - k + 1) possible windows.
 *      - Total operations: (n - k + 1) * k = O(n * k).
 *      - The sliding window technique completely eliminates redundant calculations.
 * 2. Space Complexity:
 *    - O(1) Auxiliary Space.
 *      - Operates in-place using only a few scalar variables (windowSum, targetSum,
 *        alertCount, loop index). No dynamic memory or extra arrays are allocated.
 * ============================================================================
 */
public class Q4_HotWeatherAlertWindows {

    /**
     * Counts the number of k-length contiguous subarrays with average >= threshold.
     *
     * @param readings  Array of daily/hourly temperature readings.
     * @param k         Window size (number of consecutive readings).
     * @param threshold Minimum average temperature required to trigger an alert.
     * @return Number of contiguous windows of length k with average >= threshold.
     */
    public static int countAlerts(int[] readings, int k, int threshold) {
        // Validation for edge cases: null array, non-positive k, or array smaller than window
        if (readings == null || k <= 0 || readings.length < k) {
            return 0;
        }

        int alertCount = 0;

        // Target sum calculation: avoiding floating point division
        // (average >= threshold) is mathematically equivalent to (sum >= k * threshold)
        long targetSum = (long) k * threshold;
        long windowSum = 0;

        // Step 1: Compute sum for the first window of size k
        for (int i = 0; i < k; i++) {
            windowSum += readings[i];
        }

        // Check first window
        if (windowSum >= targetSum) {
            alertCount++;
        }

        // Step 2: Slide the window across the remaining readings
        for (int i = k; i < readings.length; i++) {
            // Add incoming reading, subtract outgoing reading
            windowSum += readings[i] - readings[i - k];

            // Check if current window meets or exceeds threshold
            if (windowSum >= targetSum) {
                alertCount++;
            }
        }

        return alertCount;
    }

    /**
     * Helper method to run a test case and print formatted results.
     */
    private static void runTestCase(String testName, int[] readings, int k, int threshold, int expected) {
        int actual = countAlerts(readings, k, threshold);
        boolean passed = (actual == expected);

        System.out.printf("[%s]%n", testName);
        System.out.printf("  Readings Array : %s%n", (readings == null ? "null" : Arrays.toString(readings)));
        System.out.printf("  Window Size (k): %d%n", k);
        System.out.printf("  Threshold      : %d%n", threshold);
        System.out.printf("  Expected Alerts: %d%n", expected);
        System.out.printf("  Actual Alerts  : %d%n", actual);
        System.out.printf("  Status         : %s%n%n", (passed ? "PASS" : "FAIL"));
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    Hot Weather Alert Windows - Test Suite       ");
        System.out.println("=================================================\n");

        // Sample Test Case: Provided in the specification
        // readings = {2, 2, 2, 2, 5, 5, 5, 8}, k = 3, threshold = 4 -> 3
        // Windows of size 3:
        // [2, 2, 2] -> sum = 6  (avg = 2.00 < 4)
        // [2, 2, 2] -> sum = 6  (avg = 2.00 < 4)
        // [2, 2, 5] -> sum = 9  (avg = 3.00 < 4)
        // [2, 5, 5] -> sum = 12 (avg = 4.00 >= 4) -> Alert 1
        // [5, 5, 5] -> sum = 15 (avg = 5.00 >= 4) -> Alert 2
        // [5, 5, 8] -> sum = 18 (avg = 6.00 >= 4) -> Alert 3
        int[] sampleReadings = {2, 2, 2, 2, 5, 5, 5, 8};
        runTestCase("Sample 1 - Specification Example", sampleReadings, 3, 4, 3);

        // Test Case 2: All windows exceed threshold
        int[] allHot = {38, 40, 42, 45, 39};
        runTestCase("Test Case 2 - All Windows Trigger Alert", allHot, 3, 38, 3);

        // Test Case 3: No windows exceed threshold
        int[] allCold = {10, 12, 14, 11, 13};
        runTestCase("Test Case 3 - No Window Meets Threshold", allCold, 3, 20, 0);

        // Test Case 4: Single window where k equals array length
        int[] exactWindow = {30, 32, 34};
        runTestCase("Test Case 4 - Window Size Equals Array Length", exactWindow, 3, 32, 1);

        // Test Case 5: Window with exact threshold boundary
        // Windows of size 2: [20, 20] (avg 20), [20, 19] (avg 19.5), [19, 21] (avg 20)
        int[] boundaryReadings = {20, 20, 19, 21};
        runTestCase("Test Case 5 - Exact Threshold Boundary", boundaryReadings, 2, 20, 2);

        // Edge Case 1: Window size k is larger than readings array
        int[] shortArray = {35, 36};
        runTestCase("Edge Case 1 - Window Size Larger than Array", shortArray, 5, 30, 0);

        // Edge Case 2: Null array input
        runTestCase("Edge Case 2 - Null Array Input", null, 3, 25, 0);

        // Edge Case 3: Invalid window size k <= 0
        runTestCase("Edge Case 3 - Non-positive Window Size", sampleReadings, 0, 4, 0);

        // Print Complexity Summary
        System.out.println("Complexity Analysis Summary:");
        System.out.println("-------------------------------------------------");
        System.out.println("Sliding Window Time Complexity : O(n)");
        System.out.println("Brute Force Time Complexity    : O(n * k)");
        System.out.println("Auxiliary Space Complexity     : O(1)");
        System.out.println("=================================================");
    }
}
