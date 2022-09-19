package games;

import java.util.Random;
import java.util.Scanner;

import static util.Util.sleep;

public class RedLightGreenLight implements SquidGame {
    private static final String RESET = "\033[0m";
    private static final String RED_BOLD_BRIGHT = "\033[1;91m";
    private static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    private static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
    private static final String YELLOW_BRIGHT = "\033[0;93m";
    private static final String BLUE_BRIGHT = "\033[0;94m";
    private static final String PURPLE_BRIGHT = "\033[0;95m";
    private static final String CYAN_BRIGHT = "\033[0;96m";
    private static final String RED_BACKGROUND = "\033[41m";
    private static final String GREEN_BACKGROUND = "\033[42m";
    private static final String YELLOW_BACKGROUND = "\033[43m";
    private static final String BLUE_BACKGROUND = "\033[44m";
    private static final String PURPLE_BACKGROUND = "\033[45m";

    private static final String GREEN_LIGHT = printGreenLight();
    private static final String RED_LIGHT = printRedLight();
    private static final String GAME_OVER = RED_BACKGROUND + BLACK_BOLD_BRIGHT + "Game Over 🤯🔫" + RESET;

    private static final int TIME_LIMIT = 1000 * 60;
    private static final int BREATHER = 500;
    private static final int GREEN_LIGHT_MAX_TIME = 4000;

    private static int pressCount = 0;
    private static int gameCounter = 0;
    private static final String[] timer = {"3", "2", "1"};

    private static boolean gameOver = false;

    private static Scanner scanner;

    public static String printGreenLight() {
        return GREEN_BOLD_BRIGHT + "Green Light 🟢 👧" + RESET;
    }

    public static String printRedLight() {
        return RED_BOLD_BRIGHT + "Red Light 🔴 👧" + RESET;
    }

    @Override
    public void startGame() throws InterruptedException {
        scanner = new Scanner(System.in);
        boolean continuePlaying;
        do {
            pressCount = 0;
            gameOver = false;

            printSeparatingLine();
            if (gameCounter == 0) {
                String message = "Starting The Squid Game : " + RED_LIGHT + " / " + GREEN_LIGHT + "\n" +
                        "When " + GREEN_LIGHT + " Is On, Please, Press The Enter Key Continuously.\n" +
                        "Stop Pressing Once You See " + RED_LIGHT + ".\n" +
                        "Score A Minimum Of "
                        + printColourfulLetters("100", "cyan")
                        + " " + printColourfulLetters("Points", "blue") + " Within "
                        + printColourfulLetters("1", "cyan")
                        + " " + printColourfulLetters("Minute", "blue") + " To Stay Alive.\n";
                printingMessage(message, 75);
                System.out.println();
            }
            printingMessage(printColourfulBackground("GET READY!", "yellow"), 85);
            printTimer();
            printSeparatingLine();
            gameCounter++;

            Thread frontMan = new Thread(RedLightGreenLight::frontMan);
            Thread player = new Thread(RedLightGreenLight::player);

            frontMan.start();
            player.start();

            frontMan.join();
            player.join();

            printSeparatingLine();
            System.out.println(keepPlayingQuestion());
            String next = scanner.nextLine();
            while (!"N".equalsIgnoreCase(next) && !"Y".equalsIgnoreCase(next)) {
                System.out.println(printColourfulBackground("Invalid Input!", "red") + "\n" +
                        keepPlayingQuestion());
                next = scanner.nextLine();
            }
            if (next.equalsIgnoreCase("N")) {
                System.exit(0);
            }
            continuePlaying = next.equalsIgnoreCase("Y");
        } while (continuePlaying);
    }

    private String keepPlayingQuestion() {
        return "Do You Want To Keep Playing?: [" + printColourfulLetters("Y", "green")
                + "/" + printColourfulLetters("N", "red") + "] : ";
    }

    public static String printColourfulLetters(String text, String color) {
        switch (color) {
            case "red" -> color = RED_BOLD_BRIGHT;
            case "green" -> color = GREEN_BOLD_BRIGHT;
            case "yellow" -> color = YELLOW_BRIGHT;
            case "blue" -> color = BLUE_BRIGHT;
            case "purple" -> color = PURPLE_BRIGHT;
            case "cyan" -> color = CYAN_BRIGHT;
            default -> throw new IllegalArgumentException("Invalid Color");
        }
        return color + text + RESET;
    }

    private void printTimer() {
        for (String s : RedLightGreenLight.timer) {
            System.out.print("\r");
            printingMessage(printColourfulBackground(s, "yellow"), 90);
        }
        System.out.print("\r");
        System.out.println(printColourfulBackground("Stay Alive!", "yellow"));
    }

    public static void printSeparatingLine() {
        System.out.println();
        System.out.println("\u25CB \u25B3 \u25A1 ".repeat(10));
        System.out.println();
    }

    public static void printingMessage(String message, int speed) {
        for (int i = 0; i < message.length(); i++) {
            long start = System.currentTimeMillis();
            while (true) {
                if (System.currentTimeMillis() - start >= speed) break;
            }
            System.out.print(message.charAt(i));
        }
    }

    private static void frontMan() {
        int timeRemain = TIME_LIMIT;
        Random random = new Random();
        do {
            int dollCountingTime = random.nextInt(Math.min(timeRemain, GREEN_LIGHT_MAX_TIME));
            System.out.printf(
                    "%s Remaining Time: %d Seconds (Current Score %d): %n", GREEN_LIGHT, timeRemain / 1000, pressCount);
            System.out.println();

            timeRemain -= dollCountingTime;

            sleep(dollCountingTime);

            System.out.println(RED_LIGHT);

            int lastPressedInGreenLight = pressCount;
            int scanningForMovements = random.nextInt(2000) + BREATHER;
            sleep(scanningForMovements);
            timeRemain -= scanningForMovements;

            if (pressCount != lastPressedInGreenLight) {
                gameOver = true;
                pressCount = lastPressedInGreenLight;
                break;
            }
        } while (timeRemain >= 0);
        if (timeRemain <= 0 && 100 <= pressCount) {
            printSeparatingLine();
            printingMessage(printColourfulBackground("Congratulations Number 456!", "green") + "\n" +
                    printColourfulBackground("You Are Still ALive!", "green") + "\n" +
                    printColourfulBackground("Get Ready For The Next Game!", "green"), 75);
            System.out.println();
            printSeparatingLine();
            System.exit(0);
        } else {
            System.out.print(GAME_OVER + "\nYour Score Is: " + pressCount + ".\n" +
                    "Please, Press Enter Key To Continue.");
        }
    }

    public static String printColourfulBackground(String text, String color) {
        switch (color) {
            case "green" -> color = GREEN_BACKGROUND;
            case "red" -> color = RED_BACKGROUND;
            case "yellow" -> color = YELLOW_BACKGROUND;
            case "blue" -> color = BLUE_BACKGROUND;
            case "purple" -> color = PURPLE_BACKGROUND;
            default -> throw new IllegalArgumentException("Invalid Color");
        }
        return color + BLACK_BOLD_BRIGHT + text + RESET;
    }

    private static void player() {
        while (!gameOver) {
            scanner.nextLine();
            pressCount++;
        }
    }
}
