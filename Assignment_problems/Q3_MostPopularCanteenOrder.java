import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ============================================================================
 * Problem: Most Popular Canteen Order
 * ============================================================================
 * Given an array of order items placed at a college canteen, determine the item
 * that was ordered most frequently along with its order count.
 * 
 * Tie-Breaking Rule:
 * If two or more items have the same maximum frequency, return the item that
 * appears FIRST in the original orders list.
 * 
 * Algorithm:
 * 1. Pass 1 (Counting):
 *    - Use a HashMap<String, Integer> to record the frequency of each order item.
 *    - In the same pass, maintain the maximum frequency observed so far (maxCount).
 * 2. Pass 2 (First Item with Max Count):
 *    - Iterate sequentially through the original 'orders' array.
 *    - The first item whose frequency in the HashMap equals maxCount is our answer.
 *    - This guarantees O(1) tie-breaking according to the earliest appearance.
 * 
 * Complexity Analysis:
 * ----------------------------------------------------------------------------
 * Approach                 | Time Complexity | Auxiliary Space | Remarks
 * ----------------------------------------------------------------------------
 * 1. HashMap (Two passes)  | O(n)            | O(k) <= O(n)    | Optimal runtime
 * 2. Brute Force (Nested)  | O(n^2)          | O(1)            | Inefficient for large n
 * ----------------------------------------------------------------------------
 * 
 * Detailed Comparison:
 * 1. HashMap Approach (Optimized):
 *    - Time Complexity: O(n)
 *      - Pass 1: Traverses the array of n elements. For each item, insertion and
 *        lookup in the HashMap take O(1) on average. Total time for Pass 1 = O(n).
 *      - Pass 2: Traverses the array sequentially. For each item, map lookup takes
 *        O(1) on average. As soon as an item with count == maxCount is found,
 *        we return immediately. At most n iterations. Total time for Pass 2 = O(n).
 *      - Overall Time: O(n) + O(n) = O(n) linear time.
 *    - Space Complexity: O(k), where k is the number of distinct items (k <= n).
 *      In the worst case where every order is distinct, k = n, giving O(n) space.
 * 
 * 2. Brute-Force Approach:
 *    - Time Complexity: O(n^2)
 *      - An outer loop iterates through each of the n items.
 *      - For each item, an inner loop scans all n items to count occurrences.
 *      - Total comparisons: n * n = n^2 operations.
 *      - For large datasets (e.g., n = 100,000 canteen orders per month),
 *        O(n^2) requires ~10^10 operations (several seconds to minutes), whereas
 *        O(n) requires only ~10^5 operations (a few milliseconds).
 *    - Space Complexity: O(1) auxiliary space (no hash table allocated).
 */
public class Q3_MostPopularCanteenOrder {

    /**
     * Represents the result of a most popular item query, holding the item name and its count.
     */
    public static class OrderResult implements Map.Entry<String, Integer> {
        private final String item;
        private final int count;

        public OrderResult(String item, int count) {
            this.item = item;
            this.count = count;
        }

        public String getItem() {
            return item;
        }

        public int getCount() {
            return count;
        }

        @Override
        public String getKey() {
            return item;
        }

        @Override
        public Integer getValue() {
            return count;
        }

        @Override
        public Integer setValue(Integer value) {
            throw new UnsupportedOperationException("OrderResult is immutable");
        }

        @Override
        public String toString() {
            return "(" + item + ", " + count + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderResult that = (OrderResult) o;
            return count == that.count && Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, count);
        }
    }

    /**
     * Finds the most popular canteen order item using a HashMap.
     * If tied, returns the item that appears first in the orders list.
     *
     * @param orders Array of order names placed at the canteen
     * @return OrderResult containing the item name and frequency count, or null if input is empty/null
     */
    public static OrderResult mostPopular(String[] orders) {
        if (orders == null || orders.length == 0) {
            return null;
        }

        // Pass 1: Count frequency of each item and determine the maximum frequency
        Map<String, Integer> freqMap = new HashMap<>();
        int maxCount = 0;

        for (String item : orders) {
            if (item == null) {
                continue;
            }
            int updatedCount = freqMap.getOrDefault(item, 0) + 1;
            freqMap.put(item, updatedCount);
            if (updatedCount > maxCount) {
                maxCount = updatedCount;
            }
        }

        // Handle case where all items in array were null
        if (freqMap.isEmpty()) {
            return null;
        }

        // Pass 2: Scan the original orders list to find the FIRST item matching maxCount
        for (String item : orders) {
            if (item != null && freqMap.get(item) == maxCount) {
                return new OrderResult(item, maxCount);
            }
        }

        return null;
    }

    /**
     * Brute-force implementation for complexity and performance comparison.
     * Takes O(n^2) time by counting frequencies with nested loops.
     *
     * @param orders Array of order names placed at the canteen
     * @return OrderResult containing the item name and frequency count, or null if input is empty/null
     */
    public static OrderResult mostPopularBruteForce(String[] orders) {
        if (orders == null || orders.length == 0) {
            return null;
        }

        String bestItem = null;
        int maxCount = 0;

        // Outer loop: pick each element as a candidate
        for (int i = 0; i < orders.length; i++) {
            String candidate = orders[i];
            if (candidate == null) {
                continue;
            }

            // Inner loop: count occurrences of candidate across the array
            int currentCount = 0;
            for (int j = 0; j < orders.length; j++) {
                if (candidate.equals(orders[j])) {
                    currentCount++;
                }
            }

            // Strictly greater ensures we keep the first element in case of a tie
            if (currentCount > maxCount) {
                maxCount = currentCount;
                bestItem = candidate;
            }
        }

        if (bestItem == null) {
            return null;
        }

        return new OrderResult(bestItem, maxCount);
    }

    /**
     * Helper method to print test results cleanly and verify correctness.
     */
    private static void printTestCase(String testName, String[] orders, String expected) {
        System.out.println("Test: " + testName);
        System.out.println("  Orders  : " + Arrays.toString(orders));
        OrderResult result = mostPopular(orders);
        OrderResult bruteResult = mostPopularBruteForce(orders);

        String actual = (result != null) ? result.toString() : "null";
        System.out.println("  Output (HashMap)     : " + actual);
        System.out.println("  Output (Brute Force) : " + ((bruteResult != null) ? bruteResult.toString() : "null"));
        System.out.println("  Expected             : " + expected);

        boolean pass = actual.equals(expected);
        boolean consistent = Objects.equals(result, bruteResult);
        System.out.println("  Status               : " + (pass && consistent ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    /**
     * Prints detailed complexity analysis comparison.
     */
    private static void printComplexityAnalysis() {
        System.out.println("==========================================================================");
        System.out.println("                          COMPLEXITY ANALYSIS                             ");
        System.out.println("==========================================================================");
        System.out.println("1. HashMap Approach (Two Passes) - IMPLEMENTED:");
        System.out.println("   - Time Complexity:  O(n)");
        System.out.println("     * Pass 1: Traverses n items. Hash table put/get takes O(1) average time.");
        System.out.println("     * Pass 2: Traverses orders array; checks freqMap in O(1) average time.");
        System.out.println("     * Total time = O(n) + O(n) = O(n) linear time.");
        System.out.println("   - Space Complexity: O(k) <= O(n)");
        System.out.println("     * Stores k distinct items in the HashMap (at most n items).");
        System.out.println();
        System.out.println("2. Brute-Force Approach (Nested Loops):");
        System.out.println("   - Time Complexity:  O(n^2)");
        System.out.println("     * Outer loop runs n times for each candidate item.");
        System.out.println("     * Inner loop scans all n items to count matching occurrences.");
        System.out.println("     * Total comparisons = n * n = n^2 operations.");
        System.out.println("   - Space Complexity: O(1) auxiliary space.");
        System.out.println();
        System.out.println("Scalability Comparison:");
        System.out.println("   - For n = 1,000 orders  : HashMap ~ 1,000 ops vs Brute Force ~ 1,000,000 ops (1000x faster)");
        System.out.println("   - For n = 100,000 orders: HashMap ~ 100,000 ops vs Brute Force ~ 10,000,000,000 ops");
        System.out.println("Conclusion: The HashMap approach scales linearly and resolves ties deterministically.");
        System.out.println("==========================================================================");
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("                 MOST POPULAR CANTEEN ORDER DEMO                          ");
        System.out.println("==========================================================================\n");

        // Example 1: Sample from requirements
        String[] orders1 = {"dosa", "idli", "vada", "dosa", "idli", "dosa", "tea"};
        printTestCase("Example 1: Clear winner (dosa appears 3 times)", orders1, "(dosa, 3)");

        // Example 2: Sample from requirements (Tie-breaker case)
        // "tea" and "coffee" both appear 2 times. "tea" appears first at index 0.
        String[] orders2 = {"tea", "coffee", "coffee", "tea"};
        printTestCase("Example 2: Tie between tea and coffee (tea appears first)", orders2, "(tea, 2)");

        // Example 3: All items are unique (All count = 1, first item wins)
        String[] orders3 = {"burger", "pizza", "sandwich", "noodles"};
        printTestCase("Example 3: All items unique (burger appears first with count 1)", orders3, "(burger, 1)");

        // Example 4: Single item order list
        String[] orders4 = {"samosa"};
        printTestCase("Example 4: Single order in array", orders4, "(samosa, 1)");

        // Example 5: All orders identical
        String[] orders5 = {"coffee", "coffee", "coffee"};
        printTestCase("Example 5: All identical orders", orders5, "(coffee, 3)");

        // Example 6: Multi-way tie with 3 items each appearing 2 times
        // "idli" is at index 0, "vada" at 1, "dosa" at 2. "idli" wins tie.
        String[] orders6 = {"idli", "vada", "dosa", "idli", "vada", "dosa"};
        printTestCase("Example 6: Three-way tie (idli appears first)", orders6, "(idli, 2)");

        // Example 7: Edge case - empty array
        printTestCase("Example 7: Empty orders array", new String[]{}, "null");

        // Print full complexity analysis comparison
        printComplexityAnalysis();
    }
}
