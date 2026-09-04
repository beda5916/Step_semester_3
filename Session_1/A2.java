import java.util.Scanner;

public class A2 {

    static void checkTypingAccuracy(String original, String typed) {

        int matched = 0;
        int firstMismatch = -1;

        for (int i = 0; i < original.length(); i++) {

            if (original.charAt(i) == typed.charAt(i)) {
                matched++;
            } else {
                if (firstMismatch == -1) {
                    firstMismatch = i;
                }
            }
        }

        double accuracy = ((double) matched / original.length()) * 100;

        System.out.println(
            "Matched: " + matched + "/" + original.length()
        );

        System.out.printf(
            "Accuracy: %.2f%%%n", accuracy
        );

        if (firstMismatch == -1) {
            System.out.println("No Mismatches");
        } else {
            char originalChar = original.charAt(firstMismatch);
            char typedChar = typed.charAt(firstMismatch);

            System.out.println(
                "First Mismatch at position "
                + (firstMismatch + 1)
                + " ('"
                + originalChar
                + "' vs '"
                + typedChar
                + "')"
            );
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter original passage: ");
        String original = sc.nextLine();

        System.out.print("Enter typed text: ");
        String typed = sc.nextLine();

        if (original.length() != typed.length()) {
            System.out.println(
                "Error: Both strings must have equal length."
            );
        } else {
            checkTypingAccuracy(original, typed);
        }

        sc.close();
    }
}