package com.clgame;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private Board board;
    private Random random;
    private SoundManager soundManager;
    private Scanner scanner;
    private HighScoreManager highScoreManager;
    private int highScore;

    public Game() {
        board = new Board();
        random = new Random();
        soundManager = new SoundManagerImpl();
        scanner = new Scanner(System.in); // Initialize the Scanner
        highScoreManager = new HighScoreManager();
        highScore = highScoreManager.loadHighScore(); // Load high score
    }

    public void play() {
        // Setup the board - add random 2 and 4
        board.addRandomDigit(2);
        board.addRandomDigit(4);

        // Play background music
        soundManager.playBackgroundMusic("command_line_game/src/main/resources/melody-loop.wav");

        // Game loop
        while (!board.isGameOver()) {
            board.showBoard();
            final String RESET = "\u001B[0m";
            final String BOLD = "\u001B[1m";
            final String YELLOW = "\u001B[33m";
            final String CYAN = "\u001B[36m";
            
            System.out.println(BOLD + YELLOW + "Current Score: " + board.getScore() + RESET);
            System.out.println(BOLD + CYAN + "High Score: " + highScore + RESET);
            char move = getUserMove();
            board.processMove(move);
            soundManager.playSound("command_line_game/src/main/resources/move.wav");

            // Add a random 2 or 4
            int r = random.nextInt(100);
            if (r % 2 == 0) {
                board.addRandomDigit(2);
            } else {
                board.addRandomDigit(4);
            }
        }

        // End game
        endGame();
        soundManager.stopBackgroundMusic();

        // Close the scanner to avoid resource leaks
        scanner.close();
    }

    public char getUserMove() {
        // ANSI escape codes for bold, foreground, and background colors
        final String RESET = "\u001B[0m";          // Reset
        final String BOLD = "\u001B[1m";           // Bold
        final String GREEN = "\u001B[32m";
        final String BLUE = "\u001B[34m";
        final String CYAN = "\u001B[36m";
        final String PURPLE = "\u001B[35m";
        final String YELLOW = "\u001B[33m";

        // Show all possible moves
        System.out.println(BOLD + GREEN + "\nCHOOSE A MOVE: " + RESET);
        System.out.println(BOLD + YELLOW + "W/w: Up" + RESET);
        System.out.println(BOLD + CYAN + "S/s: Down" + RESET);
        System.out.println(BOLD + BLUE + "A/a: Left" + RESET);
        System.out.println(BOLD + PURPLE + "D/d: Right" + RESET);
        System.out.print(BOLD + "Enter move: ");

        // Read the move from the user
        String moveInput = scanner.nextLine();
        if (moveInput.equalsIgnoreCase("a") ||
            moveInput.equalsIgnoreCase("w") ||
            moveInput.equalsIgnoreCase("s") ||
            moveInput.equalsIgnoreCase("d")) {
            return moveInput.toUpperCase().charAt(0);
        }

        // If the input is invalid
        final String RED = "\u001B[41m";
        System.out.println(BOLD + RED + "\nInvalid Input!" + RESET);
        soundManager.playSound("command_line_game/src/main/resources/alert.wav");
        System.out.println();

        // Wait for 2 seconds before continuing
        try {
            Thread.sleep(2000);  // 2000 milliseconds = 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Show the board
        board.showBoard();

        // Recur
        return getUserMove();
    }

    private void endGame() {
        // ANSI escape codes for bold, foreground and background colors
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
    
        final String MAGENTA = "\u001B[48;5;201m";
        final String CYAN = "\u001B[46m";
        board.showBoard(); // Display the final state of the board

        System.out.println("Final Score: " + board.getScore());
        
        if (board.getScore() > highScore) {
            highScore = board.getScore();
            System.out.println("New High Score!");
            highScoreManager.saveHighScore(highScore);
        }

        if (board.gameWon()) {
            // Stop background music when the game ends
            soundManager.stopBackgroundMusic();
            System.out.println(BOLD + CYAN + "\nYou WON SLAYED!"+ RESET);
            soundManager.playSound("command_line_game/src/main/resources/win.wav");
            try {
                Thread.sleep(3000);  // 3000 milliseconds = 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Stop background music when the game ends
            soundManager.stopBackgroundMusic();
            System.out.println(BOLD + MAGENTA + "\nYou LOST!"+ RESET);
            soundManager.playSound("command_line_game/src/main/resources/lose.wav");
            try {
                Thread.sleep(4000);  // 4000 milliseconds = 4 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
