/**
 * ============================================================================
 * Problem 4: Train Coaches Both Ways
 * ============================================================================
 * A train is stored as a doubly linked list of coach names. The control room
 * needs to print the train from the engine end and from the guard end, and
 * to detach a coach by name.
 *
 * Implement addLast, remove(String name), printForward and printBackward
 * on a doubly linked list with head and tail references.
 *
 * Complexity Analysis:
 * --------------------
 * 1. addLast(String name):
 *    - Time Complexity: O(1)
 *      Direct access to tail reference; new node is linked in constant time.
 *    - Space: O(1) auxiliary (one new node).
 *
 * 2. remove(String name) — Finding the coach:
 *    - Time Complexity: O(n)
 *      Must traverse the list linearly to find the coach by name.
 *      In the worst case, the coach is the last node, requiring n steps.
 *
 * 3. remove(String name) — Unlinking once found:
 *    - Time Complexity: O(1)
 *      Once the target node is located, unlinking requires only a constant
 *      number of pointer reassignments:
 *        node.prev.next = node.next
 *        node.next.prev = node.prev
 *      With special handling at head (node.prev == null) and tail (node.next == null).
 *
 * 4. printForward / printBackward:
 *    - Time Complexity: O(n) — traverses all n nodes.
 * ============================================================================
 */
public class Q4_TrainCoachesBothWays {

    // ==================== Doubly Linked List Node ====================

    /**
     * Represents a single coach (node) in the doubly linked list.
     * Each node holds a coach name and references to both prev and next coaches.
     */
    static class CoachNode {
        String name;
        CoachNode prev;
        CoachNode next;

        public CoachNode(String name) {
            this.name = name;
            this.prev = null;
            this.next = null;
        }
    }

    // ==================== Doubly Linked List (Train) ====================

    /**
     * Doubly linked list representing a train with head and tail references.
     */
    static class Train {
        CoachNode head;
        CoachNode tail;
        int size;

        public Train() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        /**
         * Adds a coach at the end of the train.
         * Time: O(1) — direct tail access.
         *
         * @param name The name of the coach to add.
         */
        public void addLast(String name) {
            CoachNode newNode = new CoachNode(name);

            if (tail == null) {
                // List is empty; new node is both head and tail
                head = newNode;
                tail = newNode;
            } else {
                // Link new node after current tail
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }

        /**
         * Removes the first coach with the given name from the train.
         * Time: O(n) to find + O(1) to unlink.
         *
         * @param name The name of the coach to remove.
         * @return true if the coach was found and removed, false otherwise.
         */
        public boolean remove(String name) {
            // Traverse to find the coach by name
            CoachNode current = head;
            while (current != null) {
                if (current.name.equals(name)) {
                    // Found the coach — now unlink it in O(1)
                    unlinkNode(current);
                    size--;
                    return true;
                }
                current = current.next;
            }
            return false; // Coach not found
        }

        /**
         * Unlinks a node from the doubly linked list.
         * Handles four cases: only node, head, tail, or middle node.
         * Time: O(1) — constant number of pointer reassignments.
         */
        private void unlinkNode(CoachNode node) {
            if (node.prev != null) {
                // Not the head: link previous node to the next node
                node.prev.next = node.next;
            } else {
                // Node is the head: advance head to the next node
                head = node.next;
            }

            if (node.next != null) {
                // Not the tail: link next node back to the previous node
                node.next.prev = node.prev;
            } else {
                // Node is the tail: move tail to the previous node
                tail = node.prev;
            }

            // Clear references (optional, helps GC)
            node.prev = null;
            node.next = null;
        }

        /**
         * Returns the forward traversal string.
         * Format: "Engine <-> A1 <-> B2 <-> Guard"
         */
        public String printForward() {
            if (head == null) {
                return "(empty)";
            }
            StringBuilder sb = new StringBuilder();
            CoachNode current = head;
            while (current != null) {
                if (sb.length() > 0) {
                    sb.append(" <-> ");
                }
                sb.append(current.name);
                current = current.next;
            }
            return sb.toString();
        }

        /**
         * Returns the backward traversal string (starting from tail).
         * Format: "Guard <-> B2 <-> A1 <-> Engine"
         */
        public String printBackward() {
            if (tail == null) {
                return "(empty)";
            }
            StringBuilder sb = new StringBuilder();
            CoachNode current = tail;
            while (current != null) {
                if (sb.length() > 0) {
                    sb.append(" <-> ");
                }
                sb.append(current.name);
                current = current.prev;
            }
            return sb.toString();
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Builds a Train from an array of coach names.
     */
    private static Train buildTrain(String[] coaches) {
        Train train = new Train();
        for (String coach : coaches) {
            train.addLast(coach);
        }
        return train;
    }

    /**
     * Runs a test case and prints formatted results.
     */
    private static void runTestCase(String testName, String[] coaches, String removeCoach,
                                     String expectedForward, String expectedBackward) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);

        Train train = buildTrain(coaches);
        System.out.println("  Initial Forward  : " + train.printForward());
        System.out.println("  Initial Backward : " + train.printBackward());
        System.out.println("  Removing         : " + removeCoach);

        boolean removed = train.remove(removeCoach);
        System.out.println("  Remove Success   : " + removed);

        String actualForward = train.printForward();
        String actualBackward = train.printBackward();

        System.out.println("  Expected Forward : " + expectedForward);
        System.out.println("  Actual Forward   : " + actualForward);
        System.out.println("  Expected Backward: " + expectedBackward);
        System.out.println("  Actual Backward  : " + actualBackward);

        boolean passed = expectedForward.equals(actualForward) && expectedBackward.equals(actualBackward);
        System.out.println("  Status           : " + (passed ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("        TRAIN COACHES BOTH WAYS - TEST SUITE              ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        // coaches = Engine, A1, B1, B2, Guard; remove B1
        runTestCase("Sample Problem - Remove Middle Coach",
            new String[]{"Engine", "A1", "B1", "B2", "Guard"},
            "B1",
            "Engine <-> A1 <-> B2 <-> Guard",
            "Guard <-> B2 <-> A1 <-> Engine");

        // Test Case 2: Remove the head (Engine)
        runTestCase("Remove Head Coach",
            new String[]{"Engine", "A1", "B1", "Guard"},
            "Engine",
            "A1 <-> B1 <-> Guard",
            "Guard <-> B1 <-> A1");

        // Test Case 3: Remove the tail (Guard)
        runTestCase("Remove Tail Coach",
            new String[]{"Engine", "A1", "B1", "Guard"},
            "Guard",
            "Engine <-> A1 <-> B1",
            "B1 <-> A1 <-> Engine");

        // Test Case 4: Remove from a two-coach train (remove first)
        runTestCase("Two Coaches - Remove First",
            new String[]{"Engine", "Guard"},
            "Engine",
            "Guard",
            "Guard");

        // Test Case 5: Single coach train (remove only coach)
        runTestCase("Single Coach - Remove Only Coach",
            new String[]{"Engine"},
            "Engine",
            "(empty)",
            "(empty)");

        // Test Case 6: Coach not found
        {
            System.out.println("==================================================");
            System.out.println("Test: Coach Not Found");
            Train train = buildTrain(new String[]{"Engine", "A1", "Guard"});
            System.out.println("  Train    : " + train.printForward());
            System.out.println("  Removing : X1 (not in train)");

            boolean removed = train.remove("X1");
            System.out.println("  Removed  : " + removed);
            System.out.println("  Forward  : " + train.printForward());
            System.out.println("  Status   : " + (!removed ? "PASS [✓]" : "FAIL [✗]"));
            System.out.println();
        }

        // Complexity Summary
        System.out.println("==========================================================");
        System.out.println("                  COMPLEXITY ANALYSIS                      ");
        System.out.println("==========================================================");
        System.out.println("Operation          | Time       | Space");
        System.out.println("-------------------+------------+--------");
        System.out.println("addLast(name)      | O(1)       | O(1)");
        System.out.println("remove - find      | O(n)       | O(1)");
        System.out.println("remove - unlink    | O(1)       | O(1)");
        System.out.println("printForward       | O(n)       | O(1)");
        System.out.println("printBackward      | O(n)       | O(1)");
        System.out.println("==========================================================");
    }
}
