import java.util.Scanner;

public class M4 {

    static char findFirstNonRepeatingChar(String text) {

        int[] frequency = new int[256];

        // Count frequency of each character
        for (int i = 0; i < text.length(); i++) {
            frequency[text.charAt(i)]++;
        }

        // Find the first character whose frequency is 1
        for (int i = 0; i < text.length(); i++) {

            if (frequency[text.charAt(i)] == 1) {
                return text.charAt(i);
            }
        }

        // No non-repeating character found
        return '\0';
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String text = sc.nextLine();

        char result = findFirstNonRepeatingChar(text);

        if (result == '\0') {
            System.out.println("No non-repeating character found.");
        } else {
            System.out.println(
                "First non-repeating character: " + result
            );
        }

        sc.close();
    }
}