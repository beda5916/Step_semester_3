import java.util.Arrays;

/**
 * ============================================================================
 * Problem: Ticket Price Slot Finder
 * ============================================================================
 * Given a sorted array of ticket prices and a new ticket price, find the index
 * where the new price exists or where it should be inserted to maintain sorted
 * order.
 *
 * Algorithm:
 * - Binary Search is used instead of a linear scan.
 * - Maintain two pointers: 'low' starting at index 0 and 'high' at length - 1.
 * - In each iteration, calculate mid = low + (high - low) / 2.
 *   - If prices[mid] == newPrice: the price is already present, return mid.
 *   - If prices[mid] < newPrice: new price belongs in the right half, set low = mid + 1.
 *   - If prices[mid] > newPrice: new price belongs in the left half, set high = mid - 1.
 * - When low > high, the element was not found. By the binary search invariant,
 *   all elements before 'low' are strictly less than newPrice, and all elements
 *   from 'low' onwards are greater than newPrice. Hence, 'low' represents the exact
 *   insertion slot.
 *
 * Complexity Analysis:
 * --------------------
 * 1. Time Complexity:
 *    - Binary Search: O(log n) - With each comparison, the search range is halved,
 *      requiring at most log2(n) + 1 comparisons.
 *    - Linear Scan Comparison: A linear scan would examine elements sequentially,
 *      requiring O(n) time in the worst case (e.g., inserting at the end).
 *      For large ticket price catalogs, O(log n) is dramatically faster.
 * 2. Space Complexity:
 *    - O(1) Auxiliary Space - The iterative binary search requires only a few
 *      pointer variables (low, high, mid), operating in-place without extra memory.
 * ============================================================================
 */
public class Q5_TicketPriceSlotFinder {

    /**
     * Finds the index where newPrice exists or should be inserted in the sorted prices array.
     *
     * @param prices   Sorted array of existing ticket prices.
     * @param newPrice The ticket price to find or insert.
     * @return The index of newPrice if found, or the insertion slot index if not found.
     */
    public static int findSlot(int[] prices, int newPrice) {
        if (prices == null || prices.length == 0) {
            return 0; // Empty catalog: insert at index 0
        }

        int low = 0;
        int high = prices.length - 1;

        while (low <= high) {
            // Avoid integer overflow compared to (low + high) / 2
            int mid = low + (high - low) / 2;

            if (prices[mid] == newPrice) {
                return mid; // Found existing ticket price
            } else if (prices[mid] < newPrice) {
                low = mid + 1; // Search right half
            } else {
                high = mid - 1; // Search left half
            }
        }

        // When the loop terminates, low points to the correct insertion index
        return low;
    }

    /**
     * Helper method to run and print a test case.
     */
    private static void runTestCase(String testName, int[] prices, int newPrice, int expected) {
        int actual = findSlot(prices, newPrice);
        System.out.printf("[%s]%n", testName);
        System.out.printf("  Prices Array   : %s%n", Arrays.toString(prices));
        System.out.printf("  New Price      : %d%n", newPrice);
        System.out.printf("  Expected Slot  : %d%n", expected);
        System.out.printf("  Computed Slot  : %d%n", actual);
        System.out.printf("  Status         : %s%n%n", (actual == expected ? "PASS" : "FAIL"));
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       Ticket Price Slot Finder - Test Suite     ");
        System.out.println("=================================================\n");

        int[] samplePrices = {120, 150, 200, 260};

        // Sample Test Case 1: Price already present
        runTestCase("Sample 1 - Price Already Present", samplePrices, 150, 1);

        // Sample Test Case 2: Insert in middle
        runTestCase("Sample 2 - Insert in Middle", samplePrices, 210, 3);

        // Sample Test Case 3: Insert at end
        runTestCase("Sample 3 - Insert at End", samplePrices, 300, 4);

        // Edge Case 1: Insert at the very beginning (smaller than all existing)
        runTestCase("Edge Case 1 - Insert at Beginning", samplePrices, 100, 0);

        // Edge Case 2: Empty array
        runTestCase("Edge Case 2 - Empty Array", new int[]{}, 150, 0);

        // Edge Case 3: Single element - smaller
        runTestCase("Edge Case 3 - Single Element (Insert Before)", new int[]{200}, 150, 0);

        // Edge Case 4: Single element - exact match
        runTestCase("Edge Case 4 - Single Element (Match)", new int[]{200}, 200, 0);

        // Edge Case 5: Single element - larger
        runTestCase("Edge Case 5 - Single Element (Insert After)", new int[]{200}, 250, 1);

        System.out.println("Complexity Analysis Summary:");
        System.out.println("----------------------------");
        System.out.println("Binary Search Time Complexity : O(log n)");
        System.out.println("Linear Scan Time Complexity   : O(n)");
        System.out.println("Auxiliary Space Complexity    : O(1)");
    }
}
