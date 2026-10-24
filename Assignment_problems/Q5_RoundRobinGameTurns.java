/**
 * ============================================================================
 * Problem 5: Round-Robin Game Turns
 * ============================================================================
 * Players take turns in a fixed circle. Store the players in a circular
 * singly linked list and print who plays each turn for a given number of turns.
 *
 * Implement addLast(String name) on a circular list (keep a tail reference)
 * and printTurns(int turns).
 *
 * Circular Singly Linked List:
 * ----------------------------
 * - The last node's .next points back to the first node (head).
 * - head = tail.next (the first player is always tail.next).
 * - Walking with .next naturally wraps around to the beginning.
 *
 * Complexity Analysis:
 * --------------------
 * 1. addLast(String name):
 *    - Time Complexity: O(1)
 *      With a tail reference, adding to the end requires:
 *        newNode.next = tail.next (point new node to head)
 *        tail.next = newNode      (link old tail to new node)
 *        tail = newNode           (update tail reference)
 *      All operations are constant-time pointer assignments.
 *    - Space: O(1) auxiliary (one new node).
 *
 * 2. printTurns(int turns):
 *    - Time Complexity: O(turns)
 *      Starts at head (tail.next) and follows .next for 'turns' steps.
 *      Each step is O(1) — one pointer dereference and one print.
 *      The circular structure handles wrap-around automatically.
 *    - Space: O(1) auxiliary — only one pointer variable (current).
 * ============================================================================
 */
public class Q5_RoundRobinGameTurns {

    // ==================== Node Class ====================

    /**
     * Represents a single player (node) in the circular singly linked list.
     * Each node holds a player name and a reference to the next player.
     */
    static class Node {
        String name;
        Node next;

        public Node(String name) {
            this.name = name;
            this.next = null;
        }
    }

    // ==================== Circular Singly Linked List ====================

    /**
     * Circular singly linked list with a tail reference.
     * head is always tail.next (the first player in the circle).
     */
    static class CircularPlayerList {
        Node tail;
        int size;

        public CircularPlayerList() {
            this.tail = null;
            this.size = 0;
        }

        /**
         * Adds a player at the end of the circular list.
         * Time: O(1) — direct tail access with circular link maintenance.
         *
         * @param name The name of the player to add.
         */
        public void addLast(String name) {
            Node newNode = new Node(name);

            if (tail == null) {
                // First player: points to itself (circular)
                newNode.next = newNode;
                tail = newNode;
            } else {
                // Insert new node after tail, maintaining circularity
                // newNode.next points to head (tail.next)
                newNode.next = tail.next;
                // Old tail's next points to new node
                tail.next = newNode;
                // Update tail to new node
                tail = newNode;
            }
            size++;
        }

        /**
         * Prints who plays each turn for the given number of turns.
         * Starts at head (tail.next) and follows .next for 'turns' steps.
         * The circular structure wraps around automatically.
         *
         * Time: O(turns) | Space: O(1)
         *
         * @param turns The total number of turns to print.
         * @return A space-separated string of player names for each turn.
         */
        public String printTurns(int turns) {
            if (tail == null || turns <= 0) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            Node current = tail.next; // head = tail.next

            for (int i = 0; i < turns; i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(current.name);
                current = current.next; // naturally wraps around in circular list
            }

            return sb.toString();
        }

        /**
         * Returns the string representation of the circular list.
         * Format: "Asha -> Ravi -> Neha -> (back to Asha)"
         */
        @Override
        public String toString() {
            if (tail == null) {
                return "(empty)";
            }

            StringBuilder sb = new StringBuilder();
            Node head = tail.next;
            Node current = head;

            do {
                if (sb.length() > 0) {
                    sb.append(" -> ");
                }
                sb.append(current.name);
                current = current.next;
            } while (current != head);

            sb.append(" -> (back to ").append(head.name).append(")");
            return sb.toString();
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Builds a CircularPlayerList from an array of player names.
     */
    private static CircularPlayerList buildList(String[] players) {
        CircularPlayerList list = new CircularPlayerList();
        for (String player : players) {
            list.addLast(player);
        }
        return list;
    }

    /**
     * Runs a test case and prints formatted results.
     */
    private static void runTestCase(String testName, String[] players, int turns, String expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);

        CircularPlayerList list = buildList(players);
        System.out.println("  Circle   : " + list);
        System.out.println("  Turns    : " + turns);

        String actual = list.printTurns(turns);

        System.out.println("  Expected : " + expected);
        System.out.println("  Actual   : " + actual);
        System.out.println("  Status   : " + (expected.equals(actual) ? "PASS [✓]" : "FAIL [✗]"));
        System.out.println();
    }

    // ==================== Main Method ====================

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("         ROUND-ROBIN GAME TURNS - TEST SUITE              ");
        System.out.println("==========================================================\n");

        // Test Case 1: Sample from problem statement
        // players = Asha, Ravi, Neha; turns = 7
        // Expected: Asha Ravi Neha Asha Ravi Neha Asha
        runTestCase("Sample Problem",
            new String[]{"Asha", "Ravi", "Neha"},
            7,
            "Asha Ravi Neha Asha Ravi Neha Asha");

        // Test Case 2: Turns equal to number of players (exactly one full cycle)
        // players = A, B, C; turns = 3
        // Expected: A B C
        runTestCase("Exactly One Full Cycle",
            new String[]{"A", "B", "C"},
            3,
            "A B C");

        // Test Case 3: Single player, multiple turns
        // players = Solo; turns = 5
        // Expected: Solo Solo Solo Solo Solo
        runTestCase("Single Player Multiple Turns",
            new String[]{"Solo"},
            5,
            "Solo Solo Solo Solo Solo");

        // Test Case 4: Two players, odd number of turns
        // players = X, Y; turns = 5
        // Expected: X Y X Y X
        runTestCase("Two Players Odd Turns",
            new String[]{"X", "Y"},
            5,
            "X Y X Y X");

        // Test Case 5: Turns less than number of players (partial cycle)
        // players = A, B, C, D, E; turns = 2
        // Expected: A B
        runTestCase("Partial Cycle",
            new String[]{"A", "B", "C", "D", "E"},
            2,
            "A B");

        // Test Case 6: Large number of turns with small player group
        // players = P1, P2; turns = 10
        // Expected: P1 P2 P1 P2 P1 P2 P1 P2 P1 P2
        runTestCase("Many Turns Few Players",
            new String[]{"P1", "P2"},
            10,
            "P1 P2 P1 P2 P1 P2 P1 P2 P1 P2");

        // Test Case 7: Single turn
        // players = Alpha, Beta, Gamma; turns = 1
        // Expected: Alpha
        runTestCase("Single Turn",
            new String[]{"Alpha", "Beta", "Gamma"},
            1,
            "Alpha");

        // Complexity Summary
        System.out.println("==========================================================");
        System.out.println("                  COMPLEXITY ANALYSIS                      ");
        System.out.println("==========================================================");
        System.out.println("Operation          | Time       | Space");
        System.out.println("-------------------+------------+--------");
        System.out.println("addLast(name)      | O(1)       | O(1)");
        System.out.println("printTurns(turns)  | O(turns)   | O(1)");
        System.out.println("==========================================================");
    }
}
