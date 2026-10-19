import java.util.Arrays;

/**
 * Q5: Maximize Area Between Two Boundaries (Container With Most Water)
 *
 * Given an array of non-negative integers where each element heights[i] represents
 * the height of a vertical line at position i, find two lines that, along with the
 * x-axis, form a container that holds the most water.
 *
 * Area between walls i and j = min(heights[i], heights[j]) * (j - i)
 *
 * COMPLEXITY ANALYSIS:
 * =====================
 * Approach 1 - Brute Force (nested loops):
 *   Time:  O(n^2) — checks every pair of walls
 *   Space: O(1)
 *
 * Approach 2 - Two Pointer (optimal):
 *   Time:  O(n) — single pass with two pointers moving inward
 *   Space: O(1)
 *
 * WHY TWO-POINTER IS OPTIMAL:
 *   - Start with left=0 and right=n-1 (maximum width).
 *   - The area is limited by the shorter wall. Moving the taller wall inward
 *     can only DECREASE width without any guarantee of height increase.
 *   - Moving the shorter wall inward might find a taller wall, potentially
 *     increasing the area despite the reduced width.
 *   - This greedy strategy guarantees we never skip the optimal pair.
 *   - For large inputs (n = 10^5), O(n) vs O(n^2) is the difference between
 *     milliseconds and seconds.
 */
public class Q5_MaximizeAreaBetweenBoundaries {

    // ==================== Approach 1: Brute Force O(n^2) ====================
    /**
     * Checks every possible pair of walls and returns the maximum area.
     * Time: O(n^2), Space: O(1)
     */
    public static int maxContainerAreaBruteForce(int[] heights) {
        if (heights == null || heights.length < 2) {
            return 0;
        }

        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int width = j - i;
                int height = Math.min(heights[i], heights[j]);
                int area = height * width;
                maxArea = Math.max(maxArea, area);
            }
        }

        return maxArea;
    }

    // ==================== Approach 2: Two Pointer O(n) ====================
    /**
     * Uses two pointers starting at opposite ends, moving the shorter wall inward.
     * Time: O(n), Space: O(1)
     *
     * Why move the shorter wall?
     * - The area is limited by min(heights[left], heights[right]) * (right - left).
     * - Moving the taller wall inward reduces width but can't increase the
     *   limiting height (it's still capped by the shorter wall).
     * - Moving the shorter wall inward might find a taller wall, potentially
     *   increasing the area.
     */
    public static int maxContainerArea(int[] heights) {
        if (heights == null || heights.length < 2) {
            return 0;
        }

        int left = 0;
        int right = heights.length - 1;
        int maxArea = 0;

        while (left < right) {
            // Calculate area with current walls
            int width = right - left;
            int height = Math.min(heights[left], heights[right]);
            int area = height * width;
            maxArea = Math.max(maxArea, area);

            // Move the pointer with the shorter wall inward
            if (heights[left] < heights[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    // ==================== Main Method — Test Cases ====================
    public static void main(String[] args) {
        System.out.println("=== Q5: Maximize Area Between Two Boundaries ===");
        System.out.println();

        // Test Case 1: Sample from problem
        int[] heights1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("Test 1: heights = " + Arrays.toString(heights1));
        System.out.println("  Brute Force: " + maxContainerAreaBruteForce(heights1));
        System.out.println("  Two Pointer: " + maxContainerArea(heights1));
        System.out.println("  Expected:    49");
        System.out.println("  Explanation: min(8,7) * (8-1) = 7 * 7 = 49");
        System.out.println();

        // Test Case 2: Two walls only
        int[] heights2 = {1, 1};
        System.out.println("Test 2: heights = " + Arrays.toString(heights2));
        System.out.println("  Brute Force: " + maxContainerAreaBruteForce(heights2));
        System.out.println("  Two Pointer: " + maxContainerArea(heights2));
        System.out.println("  Expected:    1");
        System.out.println("  Explanation: min(1,1) * (1-0) = 1");
        System.out.println();

        // Test Case 3: Descending heights
        int[] heights3 = {5, 4, 3, 2, 1};
        System.out.println("Test 3: heights = " + Arrays.toString(heights3));
        System.out.println("  Brute Force: " + maxContainerAreaBruteForce(heights3));
        System.out.println("  Two Pointer: " + maxContainerArea(heights3));
        System.out.println("  Expected:    6");
        System.out.println("  Explanation: min(3,1) * (4-0)=4 or min(5,2)*(3-0)=6 -> 6");
        System.out.println();

        // Test Case 4: All same height
        int[] heights4 = {3, 3, 3, 3, 3};
        System.out.println("Test 4: heights = " + Arrays.toString(heights4));
        System.out.println("  Brute Force: " + maxContainerAreaBruteForce(heights4));
        System.out.println("  Two Pointer: " + maxContainerArea(heights4));
        System.out.println("  Expected:    12");
        System.out.println("  Explanation: min(3,3) * (4-0) = 3 * 4 = 12");
        System.out.println();

        // Test Case 5: Large peak in the middle
        int[] heights5 = {2, 1, 100, 1, 2};
        System.out.println("Test 5: heights = " + Arrays.toString(heights5));
        System.out.println("  Brute Force: " + maxContainerAreaBruteForce(heights5));
        System.out.println("  Two Pointer: " + maxContainerArea(heights5));
        System.out.println("  Expected:    8");
        System.out.println("  Explanation: min(2,2) * (4-0) = 2 * 4 = 8");
        System.out.println();

        // Test Case 6: Single element (edge case)
        int[] heights6 = {5};
        System.out.println("Test 6: heights = " + Arrays.toString(heights6));
        System.out.println("  Two Pointer: " + maxContainerArea(heights6));
        System.out.println("  Expected:    0 (need at least 2 walls)");
        System.out.println();

        // Test Case 7: Empty array (edge case)
        int[] heights7 = {};
        System.out.println("Test 7: heights = " + Arrays.toString(heights7));
        System.out.println("  Two Pointer: " + maxContainerArea(heights7));
        System.out.println("  Expected:    0");
    }
}
