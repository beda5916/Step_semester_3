import java.util.Arrays;

/**
 * Problem: Merging Two Token Queues
 * 
 * In customer service desks, bank counters, or clinic waiting systems,
 * customers receive numerical tokens in sorted order. When two separate queues
 * (e.g., Counter A and Counter B) need to be merged into a unified serving sequence,
 * we must combine the sorted token lists into a single sorted list.
 *
 * Algorithm:
 * - Two-Pointer Technique (Merge Step of Merge Sort)
 * 
 * Complexity Analysis:
 * -------------------------------------------------------------------------
 * Approach              | Time Complexity        | Auxiliary Space
 * -------------------------------------------------------------------------
 * Two-Pointer Merge     | O(m + n)               | O(m + n)
 * Join-and-Sort         | O((m + n) log(m + n))  | O(m + n)
 * -------------------------------------------------------------------------
 * 
 * Detailed Comparison:
 * 1. Two-Pointer Merge Technique:
 *    - Time Complexity: O(m + n)
 *      Let m = counterA.length and n = counterB.length.
 *      We initialize two pointers (i = 0 for Counter A, j = 0 for Counter B).
 *      At each step, we compare counterA[i] and counterB[j], place the smaller
 *      (or equal) element into the result array, and advance the corresponding pointer.
 *      Each comparison advances at least one pointer. When one array is exhausted,
 *      the remaining elements of the other array are copied directly.
 *      Total iterations = m + n, resulting in optimal linear O(m + n) runtime.
 *    - Auxiliary Space: O(m + n)
 *      Required to store the merged output array of size (m + n).
 * 
 * 2. Join-and-Sort Alternative:
 *    - Time Complexity: O((m + n) log(m + n))
 *      Concatenating the two arrays takes O(m + n) time.
 *      However, applying a general sorting algorithm (e.g., Dual-Pivot Quicksort or Timsort)
 *      takes O(N log N) where N = m + n.
 *      This completely discards the valuable precondition that both input arrays are already sorted.
 *    - Auxiliary Space: O(m + n)
 *      Also requires allocating space for the concatenated array.
 */
public class Q2_MergingTokenQueues {

    /**
     * Merges two sorted token queues represented as integer arrays into a single sorted array.
     * Preserves duplicate tokens and handles empty inputs gracefully.
     *
     * @param counterA Sorted array representing tokens from Counter A
     * @param counterB Sorted array representing tokens from Counter B
     * @return A new sorted array containing all tokens from both counters
     */
    public static int[] mergeTokens(int[] counterA, int[] counterB) {
        // Defensive handling for null references
        if (counterA == null && counterB == null) {
            return new int[0];
        }
        if (counterA == null) {
            return counterB.clone();
        }
        if (counterB == null) {
            return counterA.clone();
        }

        int m = counterA.length;
        int n = counterB.length;

        // If one of the queues is empty, return a copy of the other
        if (m == 0) {
            return counterB.clone();
        }
        if (n == 0) {
            return counterA.clone();
        }

        int[] merged = new int[m + n];
        int i = 0; // Pointer for counterA
        int j = 0; // Pointer for counterB
        int k = 0; // Pointer for merged output array

        // Merge elements while both arrays have unvisited elements
        while (i < m && j < n) {
            // Using '<=' ensures duplicate values are preserved and stable order maintained
            if (counterA[i] <= counterB[j]) {
                merged[k++] = counterA[i++];
            } else {
                merged[k++] = counterB[j++];
            }
        }

        // Copy any remaining elements from counterA
        while (i < m) {
            merged[k++] = counterA[i++];
        }

        // Copy any remaining elements from counterB
        while (j < n) {
            merged[k++] = counterB[j++];
        }

        return merged;
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("              MERGING TWO TOKEN QUEUES DEMO                ");
        System.out.println("============================================================\n");

        // Example 1: Required sample input (interleaved tokens with duplicates)
        int[] counterA1 = {3, 8, 15, 20};
        int[] counterB1 = {5, 8, 12};
        int[] result1 = mergeTokens(counterA1, counterB1);
        printTestCase("Example 1: Interleaved with duplicates", counterA1, counterB1, result1, "[3, 5, 8, 8, 12, 15, 20]");

        // Example 2: Required sample input (empty counter A)
        int[] counterA2 = {};
        int[] counterB2 = {4, 9};
        int[] result2 = mergeTokens(counterA2, counterB2);
        printTestCase("Example 2: Empty counter A", counterA2, counterB2, result2, "[4, 9]");

        // Example 3: Empty counter B
        int[] counterA3 = {1, 6, 11};
        int[] counterB3 = {};
        int[] result3 = mergeTokens(counterA3, counterB3);
        printTestCase("Example 3: Empty counter B", counterA3, counterB3, result3, "[1, 6, 11]");

        // Example 4: Both counters empty
        int[] counterA4 = {};
        int[] counterB4 = {};
        int[] result4 = mergeTokens(counterA4, counterB4);
        printTestCase("Example 4: Both counters empty", counterA4, counterB4, result4, "[]");

        // Example 5: Disjoint token ranges (all elements of A < all elements of B)
        int[] counterA5 = {1, 2, 3};
        int[] counterB5 = {10, 20, 30};
        int[] result5 = mergeTokens(counterA5, counterB5);
        printTestCase("Example 5: Disjoint token ranges", counterA5, counterB5, result5, "[1, 2, 3, 10, 20, 30]");

        // Example 6: Identical duplicate values
        int[] counterA6 = {7, 7, 7};
        int[] counterB6 = {7, 7};
        int[] result6 = mergeTokens(counterA6, counterB6);
        printTestCase("Example 6: All identical duplicate tokens", counterA6, counterB6, result6, "[7, 7, 7, 7, 7]");

        // Print complexity analysis summary
        printComplexityAnalysis();
    }

    private static void printTestCase(String testName, int[] a, int[] b, int[] actual, String expected) {
        System.out.println("Test: " + testName);
        System.out.println("  Counter A : " + Arrays.toString(a));
        System.out.println("  Counter B : " + Arrays.toString(b));
        System.out.println("  Output    : " + Arrays.toString(actual));
        System.out.println("  Expected  : " + expected);
        boolean pass = Arrays.toString(actual).equals(expected);
        System.out.println("  Status    : " + (pass ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    private static void printComplexityAnalysis() {
        System.out.println("============================================================");
        System.out.println("                    COMPLEXITY ANALYSIS                     ");
        System.out.println("============================================================");
        System.out.println("1. Two-Pointer Merge (Implemented):");
        System.out.println("   - Time Complexity:  O(m + n)");
        System.out.println("     Each element from counterA and counterB is inspected and merged exactly once.");
        System.out.println("   - Space Complexity: O(m + n)");
        System.out.println("     An auxiliary array of size m + n is allocated to hold the result.");
        System.out.println();
        System.out.println("2. Join-and-Sort Alternative:");
        System.out.println("   - Time Complexity:  O((m + n) log(m + n))");
        System.out.println("     Concatenation is O(m + n), but sorting ignores the pre-sorted invariant");
        System.out.println("     and incurs an extra O(N log N) overhead.");
        System.out.println("   - Space Complexity: O(m + n)");
        System.out.println("     Requires space for the combined array plus sort call-stack space.");
        System.out.println();
        System.out.println("Conclusion:");
        System.out.println("The two-pointer technique achieves optimal linear time O(m + n) by directly");
        System.out.println("leveraging the fact that both input token queues are already in sorted order.");
        System.out.println("============================================================");
    }
}
