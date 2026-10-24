/**
 * ============================================================================
 * Problem 3: Reverse the Queue
 * ============================================================================
 * At a ticket window, the queue order must be reversed so the last person
 * is served first. Reverse the singly linked list in place.
 *
 * Implement Node reverse(Node head) and return the new head.
 * No new nodes may be created — only pointer reassignments.
 *
 * Algorithm (Iterative Three-Pointer Technique):
 * -----------------------------------------------
 * Use three references — prev, current, and next:
 * 1. Initialize prev = null, current = head.
 * 2. For each node:
 *    a. Save current.next in 'next' (to avoid losing the rest of the list).
 *    b. Reverse the pointer: current.next = prev.
 *    c. Advance prev to current, current to next.
 * 3. When current becomes null, prev points to the new head (last original node).
 *
 * Complexity Analysis:
 * --------------------
 * - Time Complexity: O(n)
 *   Each of the n nodes is visited exactly once. At each node, we perform
 *   O(1) constant-time pointer assignments (save next, reverse link, advance).
 *   Total operations: 3n pointer assignments = O(n).
 *
 * - Additional Space Complexity: O(1)
 *   Only three pointer variables (prev, current, next) are maintained.
 *   No new nodes are created; reversal is done purely by reassigning .next fields.
 *   This is an in-place reversal with constant auxiliary space.
 * ============================================================================
 */
public class Q3_ReverseTheQueue {

    // ==================== Node Class ====================

    /**
     * Represents a single node in the singly linked list.
     * Each node holds an integer data value and a reference to the next node.
     */
    static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // ==================== Core Algorithm ====================

    /**
     * Reverses a singly linked list in place using the three-pointer technique.
     * No new nodes are created; only .next references are reassigned.
     *
     * @param head The head of the original singly linked list.
     * @return The new head of the reversed list (previously the last node).
     *
     * Time Complexity:  O(n) — single pass through the list.
     * Space Complexity: O(1) — only three pointer variables.
     */
    public static Node reverse(Node head) {
        Node prev = null;
        Node current = head;

        while (current != null) {
            // Step a: Save the next node before we overwrite the link
            Node next = current.next;

            // Step b: Reverse the current node's pointer
            current.next = prev;

            // Step c: Advance prev and current one step forward
            prev = current;
            current = next;
        }

        // When current is null, prev is the new head
        return prev;
    }

    // ==================== Helper Methods ====================

    /**
     * Builds a singly linked list from an array of integers.
     */
    private static Node buildList(int[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        Node head = new Node(values[0]);
        Node current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new Node(values[i]);
            current = current.next;
        }
        return head;
    }

    /**
     * Converts a linked list to its string representation.
     * Format: "4 -> 3 -> 2 -> 1 -> null"
     */
    private static String listToString(Node head) {
        if (head == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data).append(" -> ");
            current = current.next;
        }
        sb.append("null");
        return sb.toString();
    }

    /**
     * Converts an int array to a display string for the initial list.
     */
    private static String arrayToListString(int[] values) {
        if (values == null || values.length == 0) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]).append(" -> ");
        }
        sb.append("null");
        return sb.toString();
    }

    /**
     * Runs a test case and prints formatted results.
     */
    private static void runTestCase(String testName, int[] listValues, String expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("  Input    : " + arrayToListString(listValues));

        Node head = buildList(listValues);
        head = reverse(head);
        String actual = listToString(head);

        System.out.println("  Expected : " + expected);
        System.out.println("  Actual   : " + actual);
        System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("            REVERSE THE QUEUE - TEST SUITE                ");
        System.out.println("==========================================================\n");

        // Sample 1: Standard reversal
        // 1 -> 2 -> 3 -> 4 -> null => 4 -> 3 -> 2 -> 1 -> null
        runTestCase("Sample 1 - Standard Reversal",
            new int[]{1, 2, 3, 4},
            "4 -> 3 -> 2 -> 1 -> null");

        // Sample 2: Single element list
        // 9 -> null => 9 -> null
        runTestCase("Sample 2 - Single Element",
            new int[]{9},
            "9 -> null");

        // Test Case 3: Two elements
        // 1 -> 2 -> null => 2 -> 1 -> null
        runTestCase("Two Elements",
            new int[]{1, 2},
            "2 -> 1 -> null");

        // Test Case 4: Empty list
        // null => null
        runTestCase("Empty List",
            new int[]{},
            "null");

        // Test Case 5: Five elements
        // 10 -> 20 -> 30 -> 40 -> 50 -> null => 50 -> 40 -> 30 -> 20 -> 10 -> null
        runTestCase("Five Elements",
            new int[]{10, 20, 30, 40, 50},
            "50 -> 40 -> 30 -> 20 -> 10 -> null");

        // Test Case 6: All same values (reversal should maintain all)
        // 7 -> 7 -> 7 -> null => 7 -> 7 -> 7 -> null
        runTestCase("All Same Values",
            new int[]{7, 7, 7},
            "7 -> 7 -> 7 -> null");

        // Test Case 7: Verify double reversal restores original
        {
            System.out.println("==================================================");
            System.out.println("Test: Double Reversal Restores Original");
            int[] original = {1, 2, 3, 4, 5};
            System.out.println("  Original : " + arrayToListString(original));

            Node head = buildList(original);
            head = reverse(head);
            String afterFirst = listToString(head);
            System.out.println("  Reversed : " + afterFirst);

            head = reverse(head);
            String afterSecond = listToString(head);
            System.out.println("  Reversed Again: " + afterSecond);

            String expected = arrayToListString(original);
            System.out.println("  Expected : " + expected);
            System.out.println("  Status   : " + (expected.equals(afterSecond) ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Complexity Summary
        System.out.println("==========================================================");
        System.out.println("                  COMPLEXITY ANALYSIS                      ");
        System.out.println("==========================================================");
        System.out.println("Time Complexity       : O(n) — each node visited exactly once.");
        System.out.println("Additional Space      : O(1) — only three pointer variables (prev, current, next).");
        System.out.println("Constraint Satisfied  : No new nodes created; in-place reversal.");
        System.out.println("==========================================================");
    }
}
