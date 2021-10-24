import games.RedLightGreenLight;

import java.util.Scanner;

import static games.RedLightGreenLight.printSeparatingLine;
import static games.RedLightGreenLight.printingMessage;

public class SquidGameJava {
    private static int gameNo;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printSeparatingLine();
        printingMessage("""
                Starting Squid Game. Please, Select A Game To Play:
                Red Light / Green Light -> Enter Digit > 1
                Exit Game -> Enter Number > 456
                Please, Enter Your Choice:
                """, 100);
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
        printingMessage("Please, Insert Valid Input\n" +
                ">", 75);
    }
}
