import games.RedLightGreenLight;

import java.util.Scanner;

import static games.RedLightGreenLight.*;

public class SquidGameJava {
    private static int gameNo;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printSeparatingLine();
        printingMessage("Starting Squid Game. Please, Select A Game To Play:\n\n" +
                printRedLight() + " / " + printGreenLight() + " -> Enter Digit > " + printColourfulLetters("1", "purple") + "\n" +
                printColourfulLetters("Exit Game", "blue") + " -> Enter Number > " + printColourfulLetters("456", "purple") + "\n" +
                "Please, Enter Your Choice:\n", 75);
        System.out.print(">");


        try {
            do {
                String input = validateInput(scanner);
                gameNo = Integer.parseInt(input);
                startGame();

            } while (gameNo != 456);

        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    private static String validateInput(Scanner scanner) {
        String input = scanner.nextLine();
        while (!input.matches("^[0-9]+$")) {
            errorMessage();
            input = scanner.nextLine();
        }
        return input;
    }

    private static void startGame() throws InterruptedException {
        if (gameNo == 1) {
            new RedLightGreenLight().startGame();
        } else if (gameNo != 456) {
            errorMessage();
        }
    }

    private static void errorMessage() {
        System.out.print(printColourfulBackground("Invalid Input", "red") +
                "\n>");
    }
}
