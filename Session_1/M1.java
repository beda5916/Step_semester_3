import java.util.Random;
import java.util.Scanner;

public class M1 {

    static String playRound(String playerMove, String computerMove) {

        if (playerMove.equals(computerMove)) {
            return "Draw";
        }

        if ((playerMove.equals("Rock") && computerMove.equals("Scissors")) ||
            (playerMove.equals("Paper") && computerMove.equals("Rock")) ||
            (playerMove.equals("Scissors") && computerMove.equals("Paper"))) {

            return "Player Wins";
        }

        return "Computer Wins";
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        String[] moves = {"Rock", "Paper", "Scissors"};

        int n = 5;

        String[] playerMoves = new String[n];
        String[] computerMoves = new String[n];
        String[] results = new String[n];

        int wins = 0;
        int losses = 0;
        int draws = 0;

        for (int i = 0; i < n; i++) {

            String playerMove;

            // Input validation
            while (true) {

                System.out.print("Enter your move (Rock/Paper/Scissors): ");

                playerMove = sc.nextLine().trim();

                if (playerMove.isEmpty()) {
                    System.out.println(
                        "Invalid input. Please enter Rock, Paper, or Scissors."
                    );
                    continue;
                }

                playerMove = playerMove.substring(0, 1).toUpperCase()
                        + playerMove.substring(1).toLowerCase();

                if (playerMove.equals("Rock") ||
                    playerMove.equals("Paper") ||
                    playerMove.equals("Scissors")) {

                    break;
                }

                System.out.println(
                    "Invalid move. Please enter Rock, Paper, or Scissors."
                );
            }

            // Generate computer move
            int randomIndex = random.nextInt(3);
            String computerMove = moves[randomIndex];

            // Determine result
            String result = playRound(playerMove, computerMove);

            // Store data in arrays
            playerMoves[i] = playerMove;
            computerMoves[i] = computerMove;
            results[i] = result;

            // Count results
            if (result.equals("Player Wins")) {
                wins++;
            } else if (result.equals("Computer Wins")) {
                losses++;
            } else {
                draws++;
            }

            System.out.println("Computer Move: " + computerMove);
            System.out.println("Result: " + result);
            System.out.println();
        }

        // Final table
        System.out.println("-------------------------------------------------------------");
        System.out.println("Round | Player Move | Computer Move | Result");
        System.out.println("-------------------------------------------------------------");

        for (int i = 0; i < n; i++) {

            System.out.println(
                (i + 1) + "     | " +
                playerMoves[i] + "       | " +
                computerMoves[i] + "       | " +
                results[i]
            );
        }

        // Calculate win percentage
        double winPercentage = (wins * 100.0) / n;

        System.out.println("-------------------------------------------------------------");
        System.out.println("Wins: " + wins);
        System.out.println("Losses: " + losses);
        System.out.println("Draws: " + draws);
        System.out.println("Win Percentage: " + winPercentage + "%");

        sc.close();
    }
}