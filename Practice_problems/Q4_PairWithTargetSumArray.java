import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Pair With Target Sum (Array Version / Two-Sum Problem)
 * 
 * Given an unsorted array of integers (which may include negative numbers and duplicates)
 * and an integer target, determine whether there exist two DISTINCT elements (at different indices)
 * whose sum equals the target.
 * 
 * =========================================================================================
 * Comprehensive Complexity Analysis of All Three Approaches:
 * =========================================================================================
 * 
 * 1. Brute Force Approach: hasPairBruteForce(int[] nums, int target)
 *    --------------------------------------------------------------------------------------
 *    - Time Complexity: O(n^2)
 *      Uses two nested loops to inspect every distinct pair (i, j) where 0 <= i < j < n.
 *      Total pairs evaluated = n * (n - 1) / 2, leading to quadratic time complexity.
 *    - Auxiliary Space Complexity: O(1)
 *      Does not allocate any additional data structures; only uses a few primitive variables
 *      for loop indexing and arithmetic.
 * 
 * 2. Optimal Hash Map Approach: hasPairOptimal(int[] nums, int target)
 *    --------------------------------------------------------------------------------------
 *    - Time Complexity: O(n) average, O(n^2) worst-case (rare hash collisions)
 *      Performs a single linear scan of the array. For each element nums[i], it computes
 *      the complement (target - nums[i]) and checks for its presence in the HashMap.
 *      Hash map lookups and insertions operate in O(1) amortized time.
 *    - Auxiliary Space Complexity: O(n)
 *      Maintains a HashMap storing up to n key-value pairs (element value -> index),
 *      requiring linear extra memory in the worst case where no pair is found until the end.
 * 
 * 3. Sorted Two-Pointer Approach: hasPairSortedTwoPointer(int[] nums, int target)
 *    --------------------------------------------------------------------------------------
 *    - Time Complexity: O(n log n)
 *      Sorting the array dominates the runtime and takes O(n log n) using dual-pivot Quicksort
 *      (or TimSort). The subsequent two-pointer convergence scan takes O(n) linear time,
 *      resulting in an overall time complexity of O(n log n) + O(n) = O(n log n).
 *    - Auxiliary Space Complexity:
 *      * In-place sorting: O(1) auxiliary space (ignoring O(log n) recursion call stack).
 *      * Defensive copy: O(n) auxiliary space to clone the array so that the caller's
 *        original array order is not modified.
 * =========================================================================================
 */
public class Q4_PairWithTargetSumArray {

    /**
     * Approach 1: Brute Force
     * Checks all possible distinct pairs using nested loops.
     * 
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     * 
     * @param nums   The input array of integers (may contain negatives, duplicates, unsorted).
     * @param target The desired sum.
     * @return true if two distinct elements sum to target; false otherwise.
     */
    public static boolean hasPairBruteForce(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return false;
        }

        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                // Prevent 32-bit signed integer overflow during summation
                long sum = (long) nums[i] + nums[j];
                if (sum == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Approach 2: Optimal Hash Map
     * Uses a HashMap mapping element value to its index.
     * For each element, checks if (target - nums[i]) already exists in the map
     * at a distinct index.
     * 
     * Time Complexity: O(n) average
     * Space Complexity: O(n)
     * 
     * @param nums   The input array of integers (may contain negatives, duplicates, unsorted).
     * @param target The desired sum.
     * @return true if two distinct elements sum to target; false otherwise.
     */
    public static boolean hasPairOptimal(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return false;
        }

        // Map stores: value -> index
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            long complementLong = (long) target - nums[i];

            // If complement falls outside 32-bit signed integer range, it cannot exist in nums
            if (complementLong < Integer.MIN_VALUE || complementLong > Integer.MAX_VALUE) {
                seen.put(nums[i], i);
                continue;
            }

            int complement = (int) complementLong;

            // Check if complement exists at a different index
            if (seen.containsKey(complement)) {
                int prevIndex = seen.get(complement);
                if (prevIndex != i) {
                    return true;
                }
            }

            // Record the current element and its index
            seen.put(nums[i], i);
        }

        return false;
    }

    /**
     * Approach 3: Sorted Two-Pointer
     * Clones and sorts the array, then uses two pointers (left starting at beginning,
     * right starting at end) converging toward each other.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(1) auxiliary if sorted in-place, O(n) when cloning to protect input.
     * 
     * @param nums   The input array of integers (may contain negatives, duplicates, unsorted).
     * @param target The desired sum.
     * @return true if two distinct elements sum to target; false otherwise.
     */
    public static boolean hasPairSortedTwoPointer(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return false;
        }

        // Clone to preserve the caller's original array and avoid side effects
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int left = 0;
        int right = sorted.length - 1;

        while (left < right) {
            long currentSum = (long) sorted[left] + sorted[right];

            if (currentSum == target) {
                return true;
            } else if (currentSum < target) {
                left++;
            } else {
                right--;
            }
        }

        return false;
    }

    /**
     * Helper test runner that prints input, expected result, and outputs from all 3 approaches.
     */
    private static void runTestCase(String testName, int[] nums, int target, boolean expected) {
        boolean bfResult = hasPairBruteForce(nums, target);
        boolean optResult = hasPairOptimal(nums, target);
        boolean tpResult = hasPairSortedTwoPointer(nums, target);

        boolean allMatch = (bfResult == expected) && (optResult == expected) && (tpResult == expected);

        System.out.println("----------------------------------------------------------------------");
        System.out.println("Test Case: " + testName);
        System.out.println("Input Array: " + (nums == null ? "null" : Arrays.toString(nums)));
        System.out.println("Target Sum : " + target);
        System.out.println("Expected   : " + expected);
        System.out.println("Outputs    : BruteForce=" + bfResult + 
                           ", Optimal=" + optResult + 
                           ", TwoPointer=" + tpResult);
        System.out.println("Status     : " + (allMatch ? "PASSED" : "FAILED"));
    }

    /**
     * Main method demonstrating sample cases and edge cases.
     */
    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("   Pair With Target Sum (Two-Sum) - Verification & Demonstration      ");
        System.out.println("======================================================================");

        // Required Sample 1: Pair exists (2 + 7 = 9)
        runTestCase("Sample 1 - Pair Exists",
                new int[]{2, 7, 11, 15}, 9, true);

        // Required Sample 2: Pair does not exist
        runTestCase("Sample 2 - No Pair Exists",
                new int[]{3, 4, 6}, 20, false);

        // Edge Case 1: Negative numbers forming positive target (-2 + 8 = 6)
        runTestCase("Edge Case 1 - Negative Numbers",
                new int[]{-5, 1, 4, -2, 8}, 6, true);

        // Edge Case 2: Negative target with negative numbers (-10 + -7 = -17)
        runTestCase("Edge Case 2 - Negative Target",
                new int[]{-10, -3, 5, 2, -7}, -17, true);

        // Edge Case 3: Duplicate elements forming the target (3 + 3 = 6)
        runTestCase("Edge Case 3 - Valid Duplicates",
                new int[]{3, 3}, 6, true);

        // Edge Case 4: Target is 2 * element, but element only occurs once (cannot reuse same index)
        runTestCase("Edge Case 4 - Single Element Cannot Be Reused",
                new int[]{3, 4, 5}, 6, false);

        // Edge Case 5: Target with zeros (0 + 0 = 0)
        runTestCase("Edge Case 5 - Zeros Summing to Zero",
                new int[]{0, 4, 3, 0}, 0, true);

        // Edge Case 6: Target with zero and non-zero (0 + 7 = 7)
        runTestCase("Edge Case 6 - Zero with Non-Zero",
                new int[]{0, 7, 12}, 7, true);

        // Edge Case 7: Single element array (cannot form pair)
        runTestCase("Edge Case 7 - Single Element Array",
                new int[]{5}, 5, false);

        // Edge Case 8: Empty array
        runTestCase("Edge Case 8 - Empty Array",
                new int[]{}, 0, false);

        // Edge Case 9: Null array
        runTestCase("Edge Case 9 - Null Input",
                null, 10, false);

        // Edge Case 10: Array with large values / overflow protection
        runTestCase("Edge Case 10 - Large Numbers Overflow Check",
                new int[]{Integer.MAX_VALUE - 1, 1, 5}, Integer.MAX_VALUE, true);

        System.out.println("======================================================================");
        System.out.println("All demonstrations completed.");
    }
}
