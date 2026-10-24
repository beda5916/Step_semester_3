/**
 * ============================================================================
 * Problem 2: Cancelled Orders Cleanup
 * ============================================================================
 * A delivery app keeps order codes in a singly linked list. When a customer
 * cancels, every order with that code must be removed.
 *
 * Implement Node removeAll(Node head, int code) that removes every node with
 * the given value and returns the (possibly new) head.
 *
 * Custom Node class with data and next fields (no java.util.LinkedList).
 *
 * Algorithm:
 * ----------
 * 1. Remove matching nodes from the front by advancing head while head.data == code.
 * 2. Walk the remaining list with a 'prev' reference. For each node:
 *    - If current.data == code: skip it by setting prev.next = current.next.
 *    - Otherwise: advance prev to current.
 * 3. Return the (possibly updated) head.
 *
 * Complexity Analysis:
 * --------------------
 * - Time Complexity: O(n)
 *   Every node is visited exactly once during the single-pass traversal.
 *   Each node undergoes at most one comparison and one pointer reassignment.
 *   Total operations: n comparisons + at most n pointer updates = O(n).
 *
 * - Additional Space Complexity: O(1)
 *   Only a fixed number of pointer variables (head, prev, current) are used.
 *   No new nodes or auxiliary data structures are allocated.
 *   Removed nodes become eligible for garbage collection.
 * ============================================================================
 */
public class Q2_CancelledOrdersCleanup {

    // ==================== Node Class ====================

    /**
     * Represents a single node in the singly linked list.
     * Each node holds an integer order code and a reference to the next node.
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
     * Removes all nodes with the given order code from the singly linked list.
     * Returns the (possibly new) head of the modified list.
     *
     * @param head The head of the singly linked list.
     * @param code The order code to remove.
     * @return The new head of the list after all removals.
     *
     * Time Complexity:  O(n) — single pass through the list.
     * Space Complexity: O(1) — only pointer variables used.
     */
    public static Node removeAll(Node head, int code) {
        // Step 1: Remove matching nodes from the front
        // Keep advancing head while the head node matches the target code
        while (head != null && head.data == code) {
            head = head.next;
        }

        // If the entire list was removed, return null
        if (head == null) {
            return null;
        }

        // Step 2: Walk the remaining list with a 'prev' reference
        // At this point, head.data != code, so head is safe
        Node prev = head;
        Node current = head.next;

        while (current != null) {
            if (current.data == code) {
                // Skip the matching node by unlinking it
                prev.next = current.next;
            } else {
                // Move prev forward only when current is NOT removed
                prev = current;
            }
            // Always advance current to the next node
            current = current.next;
        }

        return head;
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
     * Format: "3 -> 8 -> null"
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
    private static void runTestCase(String testName, int[] listValues, int code, String expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("  Input List : " + arrayToListString(listValues));
        System.out.println("  Remove Code: " + code);

        Node head = buildList(listValues);
        head = removeAll(head, code);
        String actual = listToString(head);

        System.out.println("  Expected   : " + expected);
        System.out.println("  Actual     : " + actual);
        System.out.println("  Status     : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("        CANCELLED ORDERS CLEANUP - TEST SUITE             ");
        System.out.println("==========================================================\n");

        // Sample 1: Multiple occurrences scattered throughout the list
        // 5 -> 3 -> 5 -> 8 -> 5, remove 5 -> 3 -> 8 -> null
        runTestCase("Sample 1 - Multiple Occurrences",
            new int[]{5, 3, 5, 8, 5}, 5,
            "3 -> 8 -> null");

        // Sample 2: All nodes match the code (entire list removed)
        // 5 -> 5 -> 5, remove 5 -> null
        runTestCase("Sample 2 - All Nodes Match",
            new int[]{5, 5, 5}, 5,
            "null");

        // Test Case 3: Code not found in the list (no removal)
        // 1 -> 2 -> 3, remove 9 -> 1 -> 2 -> 3 -> null
        runTestCase("Code Not Found",
            new int[]{1, 2, 3}, 9,
            "1 -> 2 -> 3 -> null");

        // Test Case 4: Single node matching the code
        // 7, remove 7 -> null
        runTestCase("Single Node - Matches",
            new int[]{7}, 7,
            "null");

        // Test Case 5: Single node NOT matching the code
        // 7, remove 3 -> 7 -> null
        runTestCase("Single Node - Does Not Match",
            new int[]{7}, 3,
            "7 -> null");

        // Test Case 6: Consecutive duplicates at the beginning
        // 5 -> 5 -> 3 -> 8, remove 5 -> 3 -> 8 -> null
        runTestCase("Consecutive Duplicates at Head",
            new int[]{5, 5, 3, 8}, 5,
            "3 -> 8 -> null");

        // Test Case 7: Code only at the end
        // 1 -> 2 -> 3 -> 5, remove 5 -> 1 -> 2 -> 3 -> null
        runTestCase("Code Only at Tail",
            new int[]{1, 2, 3, 5}, 5,
            "1 -> 2 -> 3 -> null");

        // Test Case 8: Empty list
        runTestCase("Empty List",
            new int[]{}, 5,
            "null");

        // Complexity Summary
        System.out.println("==========================================================");
        System.out.println("                  COMPLEXITY ANALYSIS                      ");
        System.out.println("==========================================================");
        System.out.println("Time Complexity       : O(n) — single pass, each node visited once.");
        System.out.println("Additional Space      : O(1) — only pointer variables (prev, current).");
        System.out.println("==========================================================");
    }
}
