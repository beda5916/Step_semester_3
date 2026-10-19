package Step_semester_3.Practice_problems;

/**
 * Problem: Library Catalog Lookup
 *
 * Description:
 * Efficiently look up a book title by its ISBN from a sorted catalog using Binary Search.
 *
 * Complexity Analysis:
 * =========================================================================================
 * 1. Time Complexity: O(log n)
 *    - The catalog is sorted in ascending order of ISBN.
 *    - Binary Search repeatedly divides the search interval in half.
 *    - In each iteration, it performs an O(1) comparison between the target ISBN and
 *      the middle element's ISBN (assuming fixed-length ISBN strings, typically 10 or 13 chars).
 *    - The maximum number of comparisons is floor(log2(n)) + 1.
 *    - Hence, the time complexity is O(log n).
 *
 * 2. Space Complexity: O(1)
 *    - The algorithm uses an iterative binary search approach.
 *    - Only a few primitive integer pointers (low, high, mid) and references are maintained.
 *    - No additional dynamic data structures or recursive call stack frames are created.
 *    - Hence, auxiliary space complexity is O(1).
 * =========================================================================================
 */
public class Q1_LibraryCatalogLookup {

    /**
     * Book class representing a book in the catalog.
     * Encapsulates the ISBN and Title of the book.
     */
    public static class Book {
        private final String isbn;
        private final String title;

        public Book(String isbn, String title) {
            this.isbn = isbn;
            this.title = title;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "Book{isbn='" + isbn + "', title='" + title + "'}";
        }
    }

    /**
     * Searches for a book by its ISBN in a sorted 2D String array catalog using Binary Search.
     *
     * @param catalog    2D array where each entry is [isbn, title], sorted ascending by ISBN.
     * @param targetIsbn The ISBN to search for.
     * @return The title of the book if found, otherwise "Not Found".
     */
    public static String findBook(String[][] catalog, String targetIsbn) {
        if (catalog == null || targetIsbn == null || catalog.length == 0) {
            return "Not Found";
        }

        int low = 0;
        int high = catalog.length - 1;

        while (low <= high) {
            // Avoid potential integer overflow with low + (high - low) / 2
            int mid = low + (high - low) / 2;
            String currentIsbn = catalog[mid][0];
            int cmp = currentIsbn.compareTo(targetIsbn);

            if (cmp == 0) {
                // Book found; return title
                return catalog[mid][1];
            } else if (cmp < 0) {
                // Target is in the right half
                low = mid + 1;
            } else {
                // Target is in the left half
                high = mid - 1;
            }
        }

        return "Not Found";
    }

    /**
     * Overloaded method: Searches for a book by its ISBN in an array of Book objects using Binary Search.
     *
     * @param catalog    Array of Book objects sorted ascending by ISBN.
     * @param targetIsbn The ISBN to search for.
     * @return The title of the book if found, otherwise "Not Found".
     */
    public static String findBook(Book[] catalog, String targetIsbn) {
        if (catalog == null || targetIsbn == null || catalog.length == 0) {
            return "Not Found";
        }

        int low = 0;
        int high = catalog.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            String currentIsbn = catalog[mid].getIsbn();
            int cmp = currentIsbn.compareTo(targetIsbn);

            if (cmp == 0) {
                return catalog[mid].getTitle();
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return "Not Found";
    }

    /**
     * Main method demonstrating sample test cases and edge cases.
     */
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       LIBRARY CATALOG LOOKUP (BINARY SEARCH)    ");
        System.out.println("=================================================\n");

        // Sample 5-book catalog (sorted by ISBN ascending)
        String[][] stringCatalog = {
            {"0001112223", "The Great Gatsby"},
            {"0002223334", "To Kill a Mockingbird"},
            {"0003334445", "Classic Mythology"},
            {"0004445556", "1984"},
            {"0005556667", "Pride and Prejudice"}
        };

        System.out.println("Catalog Contents (String[][]):");
        for (int i = 0; i < stringCatalog.length; i++) {
            System.out.printf("  [%d] ISBN: %s | Title: %s%n", i, stringCatalog[i][0], stringCatalog[i][1]);
        }
        System.out.println();

        // Required Test Cases:
        runTestCase("Test 1 (Target Present - Mid)", stringCatalog, "0003334445", "Classic Mythology");
        runTestCase("Test 2 (Target Not Present)", stringCatalog, "0009998887", "Not Found");

        // Additional Boundary & Edge Cases:
        runTestCase("Test 3 (Target at First Position)", stringCatalog, "0001112223", "The Great Gatsby");
        runTestCase("Test 4 (Target at Last Position)", stringCatalog, "0005556667", "Pride and Prejudice");
        runTestCase("Test 5 (Target Smaller Than All)", stringCatalog, "0000000001", "Not Found");
        runTestCase("Test 6 (Target In-Between Not Found)", stringCatalog, "0002500000", "Not Found");
        runTestCase("Test 7 (Empty Catalog)", new String[0][2], "0003334445", "Not Found");
        runTestCase("Test 8 (Null Target ISBN)", stringCatalog, null, "Not Found");

        // Single-element Catalog tests:
        String[][] singleItemCatalog = {
            {"0007778889", "Data Structures in Java"}
        };
        runTestCase("Test 9 (Single Element - Found)", singleItemCatalog, "0007778889", "Data Structures in Java");
        runTestCase("Test 10 (Single Element - Not Found)", singleItemCatalog, "0001111111", "Not Found");

        // Demonstration using Book[] object catalog:
        System.out.println("\n-------------------------------------------------");
        System.out.println("Demonstration using Book[] Objects:");
        Book[] bookCatalog = {
            new Book("0001112223", "The Great Gatsby"),
            new Book("0002223334", "To Kill a Mockingbird"),
            new Book("0003334445", "Classic Mythology"),
            new Book("0004445556", "1984"),
            new Book("0005556667", "Pride and Prejudice")
        };

        String queryIsbn = "0003334445";
        String result = findBook(bookCatalog, queryIsbn);
        System.out.printf("Lookup for ISBN '%s' in Book[] catalog -> Title: \"%s\"%n", queryIsbn, result);

        queryIsbn = "0009998887";
        result = findBook(bookCatalog, queryIsbn);
        System.out.printf("Lookup for ISBN '%s' in Book[] catalog -> Title: \"%s\"%n", queryIsbn, result);

        System.out.println("\nAll test cases executed successfully!");
    }

    /**
     * Helper runner method to execute and format test cases.
     */
    private static void runTestCase(String testName, String[][] catalog, String targetIsbn, String expected) {
        String actual = findBook(catalog, targetIsbn);
        boolean passed = (expected == null && actual == null) || (expected != null && expected.equals(actual));
        System.out.printf("%-35s | ISBN: %-12s | Expected: %-20s | Actual: %-20s | Status: %s%n",
                testName,
                (targetIsbn == null ? "null" : targetIsbn),
                expected,
                actual,
                (passed ? "PASSED [OK]" : "FAILED [X]"));
    }
}
