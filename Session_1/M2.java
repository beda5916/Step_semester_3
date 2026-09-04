import java.util.Scanner;

public class M2 {

    // Approach 1: Iterative
    static boolean isPalindromeIterative(String text) {

        int left = 0;
        int right = text.length() - 1;

        while (left < right) {

            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }

    // Approach 2: Recursive
    static boolean isPalindromeRecursive(String text) {

        return checkRecursive(text, 0, text.length() - 1);
    }

    static boolean checkRecursive(String text, int left, int right) {

        if (left >= right) {
            return true;
        }

        if (text.charAt(left) != text.charAt(right)) {
            return false;
        }

        return checkRecursive(text, left + 1, right - 1);
    }

    // Approach 3: Array Reversal
    static boolean isPalindromeArrayReversal(String text) {

        char[] original = text.toCharArray();
        char[] reversed = new char[original.length];

        for (int i = 0; i < original.length; i++) {
            reversed[i] = original[original.length - 1 - i];
        }

        for (int i = 0; i < original.length; i++) {

            if (original[i] != reversed[i]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String text = sc.nextLine();

        boolean iterative = isPalindromeIterative(text);
        boolean recursive = isPalindromeRecursive(text);
        boolean arrayReversal = isPalindromeArrayReversal(text);

        System.out.println();
        System.out.println("Iterative: " + iterative);
        System.out.println("Recursive: " + recursive);
        System.out.println("Array Reversal: " + arrayReversal);

        if (iterative == recursive && recursive == arrayReversal) {

            System.out.println("All three approaches agree.");

            if (iterative) {
                System.out.println("Result: Palindrome");
            } else {
                System.out.println("Result: Not a Palindrome");
            }

        } else {
            System.out.println("Error: The approaches do not agree.");
        }

        sc.close();
    }
}