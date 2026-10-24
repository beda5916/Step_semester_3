/**
 * ============================================================================
 * Problem 1: Priority Token Insertion
 * ============================================================================
 * The token list sometimes needs a token added at the front (priority),
 * at the end (normal) or at a given position (rescheduled).
 *
 * Implement addFirst, addLast and insertAt(int index, int token) (0-based)
 * on a singly linked list and print the list after all operations.
 *
 * Custom Node class with data and next fields (no java.util.LinkedList).
 *
 * Complexity Analysis:
 * --------------------
 * 1. addFirst(int token):
 *    - Time Complexity: O(1)
 *      Creates a new node, sets its next to current head, updates head.
 *      All operations are constant-time pointer assignments.
 *    - Space Complexity: O(1) auxiliary (one new node).
 *
 * 2. addLast(int token):
 *    - Time Complexity: O(n)
 *      Must traverse the entire list to find the last node (no tail reference).
 *      Then appends the new node in O(1). Total: O(n).
 *    - Space Complexity: O(1) auxiliary (one new node).
 *
 * 3. insertAt(int index, int token):
 *    - Time Complexity: O(index) <= O(n)
 *      Walks to the node just before the target index, which takes at most
 *      'index' steps. The actual insertion (pointer reassignment) is O(1).
 *      Worst case when index == n: O(n).
 *    - Space Complexity: O(1) auxiliary (one new node).
 * ============================================================================
 */
public class Q1_PriorityTokenInsertion {

    // ==================== Node Class ====================

    /**
     * Represents a single node in the singly linked list.
     * Each node holds an integer token and a reference to the next node.
     */
    static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // ==================== Singly Linked List ====================

    /**
     * A singly linked list supporting addFirst, addLast, and insertAt operations.
     */
    static class TokenList {
        Node head;
        int size;

        public TokenList() {
            this.head = null;
            this.size = 0;
        }

        /**
         * Adds a token at the front of the list (priority insertion).
         * Time: O(1) | Space: O(1)
         *
         * @param token The token value to add.
         */
        public void addFirst(int token) {
            Node newNode = new Node(token);
            newNode.next = head;
            head = newNode;
            size++;
        }

        /**
         * Adds a token at the end of the list (normal insertion).
         * Time: O(n) | Space: O(1)
         *
         * @param token The token value to add.
         */
        public void addLast(int token) {
            Node newNode = new Node(token);

            if (head == null) {
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        }

        /**
         * Inserts a token at a given 0-based index (rescheduled insertion).
         * Time: O(index) <= O(n) | Space: O(1)
         *
         * @param index The 0-based position at which to insert.
         * @param token The token value to insert.
         * @throws IndexOutOfBoundsException if index < 0 or index > size.
         */
        public void insertAt(int index, int token) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
            }

            // If inserting at the front, delegate to addFirst
            if (index == 0) {
                addFirst(token);
                return;
            }

            // Walk to the node just before the target index
            Node newNode = new Node(token);
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            // Set the new node's next first, then link the previous node to it
            newNode.next = current.next;
            current.next = newNode;
            size++;
        }

        /**
         * Returns the string representation of the list.
         * Format: "100 -> 101 -> 102 -> null"
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node current = head;
            while (current != null) {
                sb.append(current.data).append(" -> ");
                current = current.next;
            }
            sb.append("null");
            return sb.toString();
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Builds a TokenList from an array of integers.
     */
    private static TokenList buildList(int[] values) {
        TokenList list = new TokenList();
        for (int val : values) {
            list.addLast(val);
        }
        return list;
    }

    /**
     * Runs a test case and prints formatted results.
     */
    private static void runTestCase(String testName, String[] steps, String expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("Steps:");
        for (String step : steps) {
            System.out.println("  > " + step);
        }
        System.out.println("Expected : " + expected);
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("         PRIORITY TOKEN INSERTION - TEST SUITE            ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        // start = 101 -> 102 -> 103
        // addFirst(100), addLast(104), insertAt(2, 150)
        // Expected: 100 -> 101 -> 150 -> 102 -> 103 -> 104 -> null
        {
            TokenList list = buildList(new int[]{101, 102, 103});
            System.out.println("==================================================");
            System.out.println("Test: Sample Problem");
            System.out.println("  Start    : " + list);

            list.addFirst(100);
            System.out.println("  addFirst(100) : " + list);

            list.addLast(104);
            System.out.println("  addLast(104)  : " + list);

            list.insertAt(2, 150);
            System.out.println("  insertAt(2,150): " + list);

            String expected = "100 -> 101 -> 150 -> 102 -> 103 -> 104 -> null";
            String actual = list.toString();
            System.out.println("  Expected : " + expected);
            System.out.println("  Actual   : " + actual);
            System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Test Case 2: Insert into empty list
        {
            TokenList list = new TokenList();
            System.out.println("==================================================");
            System.out.println("Test: Insert into Empty List");
            System.out.println("  Start    : " + list);

            list.addFirst(10);
            System.out.println("  addFirst(10)  : " + list);

            list.addLast(20);
            System.out.println("  addLast(20)   : " + list);

            list.insertAt(1, 15);
            System.out.println("  insertAt(1,15): " + list);

            String expected = "10 -> 15 -> 20 -> null";
            String actual = list.toString();
            System.out.println("  Expected : " + expected);
            System.out.println("  Actual   : " + actual);
            System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Test Case 3: insertAt at index 0 (same as addFirst)
        {
            TokenList list = buildList(new int[]{2, 3});
            System.out.println("==================================================");
            System.out.println("Test: insertAt Index 0 (Equivalent to addFirst)");
            System.out.println("  Start    : " + list);

            list.insertAt(0, 1);
            System.out.println("  insertAt(0,1) : " + list);

            String expected = "1 -> 2 -> 3 -> null";
            String actual = list.toString();
            System.out.println("  Expected : " + expected);
            System.out.println("  Actual   : " + actual);
            System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Test Case 4: insertAt at end (same as addLast)
        {
            TokenList list = buildList(new int[]{1, 2});
            System.out.println("==================================================");
            System.out.println("Test: insertAt at End (Equivalent to addLast)");
            System.out.println("  Start    : " + list);

            list.insertAt(2, 3);
            System.out.println("  insertAt(2,3) : " + list);

            String expected = "1 -> 2 -> 3 -> null";
            String actual = list.toString();
            System.out.println("  Expected : " + expected);
            System.out.println("  Actual   : " + actual);
            System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Test Case 5: Multiple addFirst operations
        {
            TokenList list = new TokenList();
            System.out.println("==================================================");
            System.out.println("Test: Multiple addFirst Operations");

            list.addFirst(3);
            list.addFirst(2);
            list.addFirst(1);
            System.out.println("  addFirst(3), addFirst(2), addFirst(1)");

            String expected = "1 -> 2 -> 3 -> null";
            String actual = list.toString();
            System.out.println("  Expected : " + expected);
            System.out.println("  Actual   : " + actual);
            System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Complexity Summary
        System.out.println("==========================================================");
        System.out.println("                  COMPLEXITY ANALYSIS                      ");
        System.out.println("==========================================================");
        System.out.println("Operation        | Time       | Space");
        System.out.println("-----------------+------------+--------");
        System.out.println("addFirst(token)  | O(1)       | O(1)");
        System.out.println("addLast(token)   | O(n)       | O(1)");
        System.out.println("insertAt(i, tok) | O(i)<=O(n) | O(1)");
        System.out.println("==========================================================");
    }
}
