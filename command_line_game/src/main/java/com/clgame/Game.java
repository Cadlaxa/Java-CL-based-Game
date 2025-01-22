package com.clgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Board board;
    private Random random;
    private SoundManager soundManager;
    private Scanner scanner;
    private HighScoreManager highScoreManager;
    private int highScore;
    private int lastHighestTile;
    private Map<Integer, String> tileSounds; // Map to store tile-specific sounds
    private List<String> highTileSounds; // List for random sounds above 2048

    public Game() {
        board = new Board();
        random = new Random();
        soundManager = new SoundManagerImpl();
        scanner = new Scanner(System.in); // Initialize the Scanner
        highScoreManager = new HighScoreManager();
        highScore = highScoreManager.loadHighScore(); // Load high score
        lastHighestTile = 0;

        // Initialize tile-specific sounds for 8, 16, 32, etc.
        tileSounds = new HashMap<>();
        tileSounds.put(8, "command_line_game/src/main/resources/tiles1/8.wav");
        tileSounds.put(16, "command_line_game/src/main/resources/tiles1/16.wav");
        tileSounds.put(32, "command_line_game/src/main/resources/tiles1/32.wav");
        tileSounds.put(64, "command_line_game/src/main/resources/tiles1/64.wav");
        tileSounds.put(128, "command_line_game/src/main/resources/tiles1/128.wav");
        tileSounds.put(256, "command_line_game/src/main/resources/tiles1/256.wav");
        tileSounds.put(512, "command_line_game/src/main/resources/tiles1/512.wav");
        tileSounds.put(1024, "command_line_game/src/main/resources/tiles1/1024.wav");
        tileSounds.put(2048, "command_line_game/src/main/resources/tiles1/2048.wav");

        // Initialize random sounds for tiles higher than 2048
        highTileSounds = new ArrayList<>();
        highTileSounds.add("command_line_game/src/main/resources/tiles1/one.wav");
        highTileSounds.add("command_line_game/src/main/resources/tiles1/two.wav");
        highTileSounds.add("command_line_game/src/main/resources/tiles1/three.wav");
        highTileSounds.add("command_line_game/src/main/resources/tiles1/four.wav");
        highTileSounds.add("command_line_game/src/main/resources/tiles1/five.wav");

    }

    public void play() {
        // Setup the board - add random 2 and 4
        board.addRandomDigit(2);
        board.addRandomDigit(4);

        // Play background music
        List<String> musicFilePaths = List.of(
        "command_line_game/src/main/resources/melody-loop.wav",
        "command_line_game/src/main/resources/melody-loop1.wav",
        "command_line_game/src/main/resources/melody-loop2.wav"
        // Add more music files as needed
        );
        soundManager.playBackgroundMusic(musicFilePaths);

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

            // Check if a new highest tile has been reached
            int currentHighestTile = board.getHighestTile();
            if (currentHighestTile > lastHighestTile) {
                playTileReachedSound(currentHighestTile);
                lastHighestTile = currentHighestTile; // Update the last highest tile
            }

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

    private void playTileReachedSound(int tileValue) {
        final String RESET = "\u001B[0m";
        final String BOLD = "\u001B[1m";
        final String MAGENTA = "\u001B[48;5;201m";
        try {
            // Check if a specific sound exists for the tile
            if (tileSounds.containsKey(tileValue)) {
                String soundPath = tileSounds.get(tileValue);
                soundManager.playSound(soundPath);
            }
            // For tiles higher than 2048, play a random sound
            else if (tileValue > 2048) {
                String randomSound = highTileSounds.get(random.nextInt(highTileSounds.size()));
                soundManager.playSound(randomSound);
            }
        } catch (Exception e) {
            // Log the error and print a message to the user
            System.out.println(BOLD + MAGENTA + "Error playing sound for tile: " + tileValue + RESET);
            System.out.println(BOLD + MAGENTA + "Error: " + e.getMessage() + RESET);
        }
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