import java.util.Arrays;

/**
 * Problem: Warehouse Grid Summary
 *
 * Given a 2D grid representing a warehouse where each cell (r, c) stores the count
 * of items in that bin, calculate:
 * 1. The total number of items stored across the entire warehouse.
 * 2. The coordinates (row, column) of the bin with the maximum number of items.
 *
 * Rules:
 * - Use nested loops to traverse the 2D grid.
 * - Track running total and maximum value with coordinates.
 * - If multiple bins contain the same maximum count, return the first one encountered
 *   in row-major order (row by row, left to right).
 * - Return result as int[] {totalItems, maxRow, maxCol}.
 *
 * Complexity Analysis:
 * --------------------
 * - Time Complexity: O(m * n)
 *     where m is the number of rows and n is the number of columns (total cells = m * n).
 *     The outer loop runs m times (one for each row), and the inner loop runs n times
 *     (one for each column in that row).
 *     Every cell is visited exactly once, performing O(1) constant-time additions
 *     and comparisons. Hence, total time is O(m * n).
 *
 * - Auxiliary Space Complexity: O(1)
 *     Only a fixed set of primitive variables (totalItems, maxRow, maxCol, maxValue, loop indices)
 *     are maintained. No additional data structures proportional to the grid dimensions are allocated.
 */
public class Q2_WarehouseGridSummary {

    /**
     * Calculates the total items in the warehouse grid and locates the first maximum bin.
     *
     * @param grid 2D array representing item counts in warehouse bins.
     * @return int array [totalItems, maxRow, maxCol], or [0, -1, -1] if grid is null/empty.
     */
    public static int[] warehouseSummary(int[][] grid) {
        // Edge case: null or empty grid
        if (grid == null || grid.length == 0) {
            return new int[]{0, -1, -1};
        }

        int totalItems = 0;
        int maxRow = -1;
        int maxCol = -1;
        int maxValue = Integer.MIN_VALUE;
        boolean foundFirstCell = false;

        // Nested loops: Outer loop iterates through each row (m rows)
        for (int r = 0; r < grid.length; r++) {
            if (grid[r] == null) {
                continue;
            }

            // Inner loop iterates through each column in the current row (n columns)
            for (int c = 0; c < grid[r].length; c++) {
                int count = grid[r][c];

                // Track running total
                totalItems += count;

                // Track max value and coordinates
                // Strictly greater (>) ensures that in case of a tie, the first encountered bin
                // (top-to-bottom, left-to-right) is retained.
                if (!foundFirstCell || count > maxValue) {
                    maxValue = count;
                    maxRow = r;
                    maxCol = c;
                    foundFirstCell = true;
                }
            }
        }

        // Handle case where grid contains only empty rows (e.g., {{}})
        if (!foundFirstCell) {
            return new int[]{0, -1, -1};
        }

        return new int[]{totalItems, maxRow, maxCol};
    }

    /**
     * Helper method to display grid and formatted test results.
     */
    private static void runTest(String testName, int[][] grid, int[] expected) {
        System.out.println("==================================================");
        System.out.println("Test: " + testName);
        System.out.println("Grid layout:");
        if (grid == null) {
            System.out.println("  null");
        } else if (grid.length == 0) {
            System.out.println("  [] (empty)");
        } else {
            for (int r = 0; r < grid.length; r++) {
                System.out.println("  Row " + r + ": " + (grid[r] != null ? Arrays.toString(grid[r]) : "null"));
            }
        }

        int[] actual = warehouseSummary(grid);
        System.out.println("Result: Total Items = " + actual[0] + ", Max Bin at = (" + actual[1] + ", " + actual[2] + ")");

        boolean passed = Arrays.equals(actual, expected);
        System.out.println("Expected: Total Items = " + expected[0] + ", Max Bin at = (" + expected[1] + ", " + expected[2] + ")");
        System.out.println("Status: " + (passed ? "PASSED" : "FAILED"));
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Warehouse Grid Summary Test Suite ===\n");

        // Test Case 1: Standard sample problem
        // Grid:
        //  4   9   2   (Row 0 sum: 15)
        //  7   1   6   (Row 1 sum: 14)
        //  3  12   5   (Row 2 sum: 20)
        // Total = 49, Max = 12 at (2, 1)
        int[][] sampleGrid = {
            {4, 9, 2},
            {7, 1, 6},
            {3, 12, 5}
        };
        runTest("Sample Problem Grid", sampleGrid, new int[]{49, 2, 1});

        // Test Case 2: Tie-breaking case (multiple maximums)
        // Bins with value 15 appear at (0, 1), (1, 2), and (2, 0).
        // First one encountered in row-major order should be selected: (0, 1).
        // Total = 10 + 15 + 8 + 6 + 12 + 15 + 15 + 4 + 7 = 92
        int[][] tieGrid = {
            {10, 15, 8},
            {6, 12, 15},
            {15, 4, 7}
        };
        runTest("Tie-Breaking for Max Bin (First in row-major order)", tieGrid, new int[]{92, 0, 1});

        // Test Case 3: Single cell grid (1x1)
        int[][] singleCellGrid = {
            {42}
        };
        runTest("Single Bin Grid (1x1)", singleCellGrid, new int[]{42, 0, 0});

        // Test Case 4: Single row (1 x N)
        // Total = 5 + 18 + 9 + 2 = 34, Max = 18 at (0, 1)
        int[][] singleRowGrid = {
            {5, 18, 9, 2}
        };
        runTest("Single Row Grid (1x4)", singleRowGrid, new int[]{34, 0, 1});

        // Test Case 5: Single column (M x 1)
        // Total = 8 + 3 + 25 + 11 = 47, Max = 25 at (2, 0)
        int[][] singleColGrid = {
            {8},
            {3},
            {25},
            {11}
        };
        runTest("Single Column Grid (4x1)", singleColGrid, new int[]{47, 2, 0});

        // Test Case 6: Rectangular non-square grid (2 x 4)
        // Total = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 = 36, Max = 8 at (1, 3)
        int[][] rectangularGrid = {
            {1, 2, 3, 4},
            {5, 6, 7, 8}
        };
        runTest("Rectangular Grid (2x4)", rectangularGrid, new int[]{36, 1, 3});

        // Test Case 7: Grid with all zero items
        int[][] allZerosGrid = {
            {0, 0},
            {0, 0}
        };
        runTest("All Zeros Grid", allZerosGrid, new int[]{0, 0, 0});

        // Test Case 8: Empty / null edge cases
        runTest("Empty Grid", new int[][]{}, new int[]{0, -1, -1});
        runTest("Grid with Empty Row", new int[][]{{}}, new int[]{0, -1, -1});
        runTest("Null Grid", null, new int[]{0, -1, -1});
    }
}
