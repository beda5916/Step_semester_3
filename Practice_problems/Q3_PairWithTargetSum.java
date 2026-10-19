import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Pair With Target Sum
 * 
 * Given an unsorted array of integers (which may include negative numbers and duplicates)
 * and a target integer, determine whether there exist two distinct elements (i.e., at 
 * different indices) whose sum equals the target.
 *
 * =========================================================================================
 * COMPLEXITY ANALYSIS & COMPARISON
 * =========================================================================================
 * 
 * Approach 1: Brute Force (Nested Loops)
 * ---------------------------------------
 * - Algorithm:
 *   Iterate through all unique pairs (i, j) where 0 <= i < j < n, and check if
 *   nums[i] + nums[j] == target.
 *
 * - Time Complexity:
 *   * Best Case: O(1) - When the very first pair checked (index 0 and 1) sums up to target.
 *   * Average Case: O(n^2) - On average, n * (n - 1) / 4 comparisons before finding pair.
 *   * Worst Case: O(n^2) - When no such pair exists or valid pair is at the very end.
 *     Total comparisons: n * (n - 1) / 2 = O(n^2).
 *
 * - Space Complexity:
 *   * Auxiliary Space: O(1) - Only loop indices (i, j) and array length are stored.
 *   * Total Space: O(n) to store the input array.
 *
 * Approach 2: Optimal Hash Set (Complement Lookup)
 * ------------------------------------------------
 * - Algorithm:
 *   Traverse the array once. For each element num, compute complement = target - num.
 *   If complement is already present in HashSet, a valid pair has been found.
 *   Otherwise, insert num into the HashSet and continue.
 *
 * - Time Complexity:
 *   * Best Case: O(1) - Found on the second element (e.g., nums[0] + nums[1] == target).
 *   * Average Case: O(n) - Single pass of n iterations; HashSet contains() and add()
 *     operations execute in O(1) average time due to hash table hashing.
 *   * Worst Case: O(n^2) - Under catastrophic hash collision scenarios where HashSet
 *     buckets degrade into linear chains (extremely rare in practice).
 *
 * - Space Complexity:
 *   * Auxiliary Space: O(n) - In the worst case (no pair exists), up to n distinct elements
 *     are stored in the HashSet.
 *   * Total Space: O(n) input array + O(n) hash set.
 *
 * Detailed Comparison & Trade-Offs:
 * ---------------------------------
 * 1. Time vs. Space Trade-off:
 *    The HashSet approach trades O(n) additional memory to dramatically reduce time complexity
 *    from quadratic O(n^2) to linear O(n).
 * 2. Scalability:
 *    - For small arrays (e.g., n < 30):
 *      Brute force can be faster in practice due to zero memory allocation overhead,
 *      no object boxing (int vs Integer), and cache-friendly sequential memory access.
 *    - For large arrays (e.g., n = 100,000):
 *      Brute force requires ~5 * 10^9 operations (~several seconds).
 *      HashSet requires ~10^5 operations (~a few milliseconds).
 * 3. Handling Duplicates & Distinct Indices:
 *    Checking `seen.contains(complement)` BEFORE adding the current element guarantees:
 *    - An element cannot be paired with itself (satisfies i != j).
 *    - If duplicate values exist (e.g., nums = [3, 3], target = 6), the second occurrence
 *      successfully finds the first occurrence in `seen` and returns true.
 * =========================================================================================
 */
public class Q3_PairWithTargetSum {

    /**
     * Approach 1: Brute Force
     * Checks all distinct pairs (i, j) with i != j using nested loops.
     *
     * @param nums   The input array of integers (may contain negatives, duplicates, unsorted).
     * @param target The target sum to find.
     * @return true if two distinct elements sum to target; false otherwise.
     *
     * Time Complexity:  O(n^2)
     * Space Complexity: O(1) auxiliary
     */
    public static boolean hasPairWithSumBruteForce(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return false;
        }

        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Approach 2: Optimal Hash Set
     * Uses a HashSet to store seen elements and checks if complement (target - num) exists.
     *
     * @param nums   The input array of integers (may contain negatives, duplicates, unsorted).
     * @param target The target sum to find.
     * @return true if two distinct elements sum to target; false otherwise.
     *
     * Time Complexity:  O(n) average
     * Space Complexity: O(n) auxiliary
     */
    public static boolean hasPairWithSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return false;
        }

        Set<Integer> seen = new HashSet<>();

        for (int num : nums) {
            int complement = target - num;

            // Check if complement has already been encountered at an earlier index
            if (seen.contains(complement)) {
                return true;
            }

            // Add current number to set of seen elements
            seen.add(num);
        }

        return false;
    }

    /**
     * Helper method to find and return the pair values if one exists.
     * Useful for detailed demonstration/verification in the main method.
     *
     * @param nums   The input array of integers.
     * @param target The target sum.
     * @return An int array of size 2 containing the two elements, or null if no pair exists.
     */
    public static int[] findPair(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return null;
        }

        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            int complement = target - num;
            if (seen.contains(complement)) {
                return new int[]{complement, num};
            }
            seen.add(num);
        }

        return null;
    }

    /**
     * Helper test runner method to execute and format test case results.
     */
    private static void runTestCase(int testNumber, String description, int[] nums, int target, boolean expected) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Test Case %d: %s%n", testNumber, description);
        System.out.printf("Input Array : %s%n", nums == null ? "null" : Arrays.toString(nums));
        System.out.printf("Target Sum  : %d%n", target);

        boolean resultBrute = hasPairWithSumBruteForce(nums, target);
        boolean resultOptimal = hasPairWithSum(nums, target);
        int[] pair = findPair(nums, target);

        System.out.printf("Brute Force Result : %b%n", resultBrute);
        System.out.printf("HashSet Result     : %b%n", resultOptimal);
        System.out.printf("Expected Result    : %b%n", expected);

        if (pair != null) {
            System.out.printf("Pair Found         : (%d, %d) -> %d + %d = %d%n", 
                              pair[0], pair[1], pair[0], pair[1], target);
        } else {
            System.out.println("Pair Found         : None");
        }

        boolean passed = (resultBrute == expected) && (resultOptimal == expected);
        System.out.printf("Status             : %s%n", passed ? "PASSED [OK]" : "FAILED [FAIL]");
    }

    /**
     * Main method demonstrating sample inputs, edge cases, and verification.
     */
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("                  PAIR WITH TARGET SUM - DEMONSTRATION & TESTS                  ");
        System.out.println("================================================================================");

        // Required Sample Cases
        runTestCase(1, "Standard positive array with valid pair",
                new int[]{2, 7, 11, 15}, 9, true);

        runTestCase(2, "Small array with no valid pair",
                new int[]{3, 4, 6}, 20, false);

        // Edge Cases: Negative Numbers
        runTestCase(3, "Array with negative numbers (positive target)",
                new int[]{-5, 8, -3, 12}, 5, true); // -3 + 8 = 5

        runTestCase(4, "Array with negative numbers (negative target)",
                new int[]{-10, -20, 30, -5}, -30, true); // -10 + -20 = -30

        runTestCase(5, "Array with negatives and no matching pair",
                new int[]{-5, 8, -3, 12}, -8, false);

        // Edge Cases: Duplicates
        runTestCase(6, "Duplicates forming valid pair (4 + 4 = 8)",
                new int[]{4, 1, 4}, 8, true);

        runTestCase(7, "Single duplicate value that cannot pair with itself",
                new int[]{4, 1, 2}, 8, false);

        runTestCase(8, "Multiple zeros summing to zero",
                new int[]{0, 5, 0, 9}, 0, true); // 0 + 0 = 0

        runTestCase(9, "Zero target with opposites",
                new int[]{-100, 50, 100}, 0, true); // -100 + 100 = 0

        // Edge Cases: Single Element & Empty / Null
        runTestCase(10, "Single element array (target equals double of element)",
                new int[]{5}, 10, false);

        runTestCase(11, "Single element array (target equals element)",
                new int[]{5}, 5, false);

        runTestCase(12, "Empty array",
                new int[]{}, 0, false);

        runTestCase(13, "Null array input",
                null, 10, false);

        // Additional Case: Large unsorted array with multiple candidates
        runTestCase(14, "Unsorted array with negative, zero, and positive numbers",
                new int[]{14, -2, 7, 0, -8, 3, 25}, 23, true); // -2 + 25 = 23

        System.out.println("================================================================================");
        System.out.println("All test cases completed.");
        System.out.println("================================================================================");
    }
}
