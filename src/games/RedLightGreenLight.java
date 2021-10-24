package games;

import java.util.Random;
import java.util.Scanner;

import static Util.Util.sleep;

public class RedLightGreenLight implements SquidGame {
    // Windows User may not be able to see actual Red Emoji
    private static final String RESET = "\033[0m";
    private static final String RED_BOLD_BRIGHT = "\033[1;91m";
    private static final String RED_LIGHT = RED_BOLD_BRIGHT + "Red Light 🔴 👧" + RESET;
    private static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    private static final String GREEN_LIGHT = GREEN_BOLD_BRIGHT + "Green Light 🟢 👧" + RESET;
    private static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
    private static final String RED_BACKGROUND = "\033[41m";
    private static final String GAME_OVER = RED_BACKGROUND + BLACK_BOLD_BRIGHT + "Game Over 🤯🔫" + RESET;

    private static final int TIME_LIMIT = 1000 * 60; //  1 Minute
    private static final int BREATHER = 500;
    private static final int GREEN_LIGHT_MAX_TIME = 4000;

    // game variables
    private static int pressCount = 0;
    private static boolean gameOver = false;

    private static Scanner scanner;

    @Override
    public void startGame() throws InterruptedException {
        scanner = new Scanner(System.in);
        boolean continuePlaying;
        do {
            // resetting variables
            pressCount = 0;
            gameOver = false;

            printSeparatingLine();
            String message = "Starting The Squid Game #1: " + RED_LIGHT + " / " + GREEN_LIGHT + "\n" +
                    "When " + GREEN_LIGHT + " Is On, Please, Press The Enter Key Continuously.\n" +
                    "Stop Pressing Once You See " + RED_LIGHT + ".\n";
            printingMessage(message, 100);
            printingMessage("GET READY!   ", 150);
            printingMessage("3   2   1   ", 600);
            printingMessage("HERE WE GO!\n", 150);
            printSeparatingLine();


            Thread frontMan = new Thread(RedLightGreenLight::frontMan);
            Thread player = new Thread(RedLightGreenLight::player);

            // Starting the threads
            frontMan.start();
            player.start();

            // waiting threads to finish
            frontMan.join();
            player.join();

            printSeparatingLine();
            printingMessage("Do You Want To Keep Playing?: [Y/N] : ", 75);
            String next = scanner.next();
            continuePlaying = next.equalsIgnoreCase("Y");
        } while (continuePlaying); // if pressed Y, restart the game
    }

    public static void printSeparatingLine() {
        System.out.println();
        printingMessage("\u25CB \u25B3 \u25A1 ".repeat(10), 10);
        System.out.println("\n");
    }

    public static void printingMessage(String message, int speed) {
        for (int i = 0; i < message.length(); i++) {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < speed) {
            }
            System.out.print(message.charAt(i));
        }
    }

    private static void frontMan() {
        int timeRemain = TIME_LIMIT;
        Random random = new Random();
        do {
            // calculate time for "Mugunghwa Kkoci Pieot Seumnida"
            int dollCountingTime = random.nextInt(Math.min(timeRemain, GREEN_LIGHT_MAX_TIME)) + 1000;
            System.out.printf("%s Remaining Time: %d Seconds (Current Score %d): %n", GREEN_LIGHT, timeRemain / 1000, pressCount);
            System.out.println();

            // countdown the timer
            timeRemain -= dollCountingTime;

            // doll 👧 is now chanting "Mugunghwa Kkoci Pieot Seumnida"
            sleep(dollCountingTime);

            // Stop pressing enter now
            System.out.println(RED_LIGHT);

            // The Doll is now scanning for players' movement, Don't Move

            int lastPressedInGreenLight = pressCount;
            int scanningForMovements = random.nextInt(2000) + BREATHER;
            sleep(scanningForMovements); // Scanning for any movement
            timeRemain -= scanningForMovements;

            // IF player has moved (pressed enter in Red light time) game is over
            if (pressCount != lastPressedInGreenLight) {
                gameOver = true;
                pressCount = lastPressedInGreenLight;
                break;
            }
        } while (timeRemain >= 0);

        printingMessage(GAME_OVER + "\nYour Score Is: " + pressCount + ".\n" +
        "Please, Press Enter Key To Continue.", 75);
    }

    private static void player() {
        // keep on reading Enter until game is over
        while (!gameOver) {
            scanner.nextLine();
            pressCount++;
        }
    }
}
