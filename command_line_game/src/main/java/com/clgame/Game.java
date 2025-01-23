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
    private Map<Integer, List<String>> tileSounds; // Map to store tile-specific sounds
    private List<String> highTileSounds; // List for random sounds above 2048
    private List<String> moveSounds; // List for random sounds for moves
    // HC sfx
    private Map<Integer, String> scoreSounds;
    private List<String> highScoreRandomSounds;
    private int lastMilestone = 0;

    final String RES = "command_line_game/src/main/resources/";
    final String MELODY = "command_line_game/src/main/resources/melody/";
    final String TILES1 = "command_line_game/src/main/resources/tiles1/";
    final String TILES2 = "command_line_game/src/main/resources/tiles2/";
    final String HC = "command_line_game/src/main/resources/scores-sfx/";

    public Game() {
        board = new Board();
        random = new Random();
        soundManager = new SoundManagerImpl();
        scanner = new Scanner(System.in); // Initialize the Scanner
        highScoreManager = new HighScoreManager();
        highScore = highScoreManager.loadHighScore(); // Load high score
        lastHighestTile = 0;
        initializeScoreSounds();
        initializeHighScoreRandomSounds();

        // Initialize tile-specific sounds for 8, 16, 32, etc.
        tileSounds = new HashMap<>();
        tileSounds.put(8, List.of(TILES1 + "8.wav", TILES2 + "8.wav"));
        tileSounds.put(16, List.of(TILES1 + "16.wav", TILES2 + "16.wav"));
        tileSounds.put(32, List.of(TILES1 + "32.wav", TILES2 + "32.wav"));
        tileSounds.put(64, List.of(TILES1 + "64.wav", TILES2 + "64.wav"));
        tileSounds.put(128, List.of(TILES1 + "128.wav", TILES2 + "128.wav"));
        tileSounds.put(256, List.of(TILES1 + "256.wav", TILES2 + "256.wav"));
        tileSounds.put(512, List.of(TILES1 + "512.wav", TILES2 + "512.wav"));
        tileSounds.put(1024, List.of(TILES1 + "1024.wav", TILES2 + "1024.wav"));
        tileSounds.put(2048, List.of(TILES1 + "2048.wav", TILES2 + "2048.wav"));

        // Initialize random sounds for tiles higher than 2048
        highTileSounds = new ArrayList<>();
        highTileSounds.add(TILES1 + "one.wav");
        highTileSounds.add(TILES1 + "two.wav");
        highTileSounds.add(TILES1 + "three.wav");
        highTileSounds.add(TILES1 + "four.wav");
        highTileSounds.add(TILES1 + "five.wav");

    }

    private void initializeScoreSounds() {
        // Add sounds for specific scores
        scoreSounds = new HashMap<>();
        scoreSounds.put(100, HC + "100 sc.wav");
        scoreSounds.put(500, HC + "500 sc.wav");
        scoreSounds.put(1000, HC + "1K sc.wav");
        scoreSounds.put(1500, HC + "1500 sc.wav");
        scoreSounds.put(2000, HC + "2K sc.wav");
        scoreSounds.put(3000, HC + "3K sc.wav");
        scoreSounds.put(4000, HC + "4K sc.wav");
        scoreSounds.put(5000, HC + "5K sc.wav");
    }

    private void initializeHighScoreRandomSounds() {
        // Add random sounds for scores above 5000
        highScoreRandomSounds = new ArrayList<>();
        highScoreRandomSounds.add(HC + "sc rand.wav");
        highScoreRandomSounds.add(HC + "sc rand1.wav");
        highScoreRandomSounds.add(HC + "sc rand2.wav");
        highScoreRandomSounds.add(HC + "sc rand3.wav");
        highScoreRandomSounds.add(HC + "sc rand4.wav");
    }

    public void checkAndPlayScoreSounds(int currentScore) {
        // Play sounds for specific score milestones
        for (Map.Entry<Integer, String> entry : scoreSounds.entrySet()) {
            int milestone = entry.getKey();
            if (currentScore >= milestone && lastMilestone < milestone) {
                soundManager.playSound(entry.getValue());
                lastMilestone = milestone; // Update last milestone to prevent replaying
            }
        }
    
        // If score is 5000 or above, play random sounds every 500 ticks
        if (currentScore >= 5000 && currentScore / 500 > lastMilestone / 500) {
            String randomSound = highScoreRandomSounds.get(new Random().nextInt(highScoreRandomSounds.size()));
            soundManager.playSound(randomSound);
            lastMilestone = (currentScore / 500) * 500; // Update to the nearest 500 tick milestone
        }
    }

    public void play() {
        // Setup the board - add random 2 and 4
        board.addRandomDigit(2);
        board.addRandomDigit(4);

        // Play background music
        List<String> musicFilePaths = List.of(
            MELODY + "melody-loop3.wav",
            MELODY + "melody-loop1.wav",
            MELODY + "melody-loop2.wav",
            MELODY + "melody-loop.wav",
            MELODY + "melody-loop4.wav",
            MELODY + "melody-loop5.wav",
            MELODY + "melody-loop6.wav",
            MELODY + "melody-loop7.wav",
            MELODY + "melody-loop8.wav",
            MELODY + "melody-loop9.wav",
            MELODY + "melody-loop10.wav"
        );
        soundManager.playBackgroundMusic(musicFilePaths);

        // Game loop
        while (!board.isGameOver()) {
            board.showBoard();
            final String RESET = "\u001B[0m";
            final String BOLD = "\u001B[1m";
            final String YELLOW = "\u001B[33m";
            final String CYAN = "\u001B[36m";

            checkAndPlayScoreSounds(board.getScore());
            System.out.println(BOLD + YELLOW + "Current Score: " + board.getScore() + RESET);
            System.out.println(BOLD + CYAN + "High Score: " + highScore + RESET);
            char move = getUserMove();
            board.processMove(move);
            try {
                // Initialize random sounds for tiles higher than 2048
                    moveSounds = new ArrayList<>();
                    moveSounds.add(RES + "move.wav");
                    moveSounds.add(RES + "move1.wav");
                    moveSounds.add(RES + "move2.wav");
                    moveSounds.add(RES + "move3.wav");
                    moveSounds.add(RES + "move4.wav");
                String randomMoveSound = moveSounds.get(random.nextInt(moveSounds.size()));
                soundManager.playSound(randomMoveSound);
            } catch (Exception e) {
                System.err.println("Error playing move sound: " + e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
    
            // Randomly play a sound for tiles, regardless of their value
            try {
                soundManager.playRandomSound();
            } catch (Exception e) {
                
            }

            // Check if a new highest tile has been reached
            int currentHighestTile = board.getHighestTile();
            if (currentHighestTile > lastHighestTile) {
                try {
                    playTileReachedSound(currentHighestTile);
                    lastHighestTile = currentHighestTile; // Update the last highest tile
                } catch (Exception e) {
                    System.err.println("Error playing tile reached sound: " + e.getMessage());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
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
        soundManager.stopBackgroundMusic();
        endGame();

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
        soundManager.playSound(RES + "alert.wav");
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
            // Check if the tile has sounds in the map
            if (tileSounds.containsKey(tileValue)) {
                // Get the list of sounds for the current tile value
                List<String> soundsForTile = tileSounds.get(tileValue);
                String randomSound = soundsForTile.get(random.nextInt(soundsForTile.size()));
    
                // Play the random sound
                soundManager.playSound(randomSound);
            } 
            // For tiles higher than 2048, play a random sound from highTileSounds
            else if (tileValue > 2048) {
                String randomSound = highTileSounds.get(random.nextInt(highTileSounds.size()));
                soundManager.playSound(randomSound);
            }
        } catch (Exception e) {
            System.out.println(BOLD + MAGENTA + "Error playing sound for tile: " + tileValue + RESET);
            System.out.println(BOLD + MAGENTA + "Error: " + e.getMessage() + RESET);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
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
            System.out.println(BOLD + CYAN + "\nYou WON SLAYED!"+ RESET);
            soundManager.playSound(RES + "win.wav");
            try {
                Thread.sleep(3000);  // 3000 milliseconds = 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (!board.gameWon()) {
            // Stop background music when the game ends
            System.out.println(BOLD + MAGENTA + "\nYou LOST!"+ RESET);
            soundManager.playSound(RES + "lose.wav");
            try {
                Thread.sleep(4000);  // 4000 milliseconds = 4 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Stop background music when the game ends
            System.out.println(BOLD + MAGENTA + "\nYou LOST!"+ RESET);
            soundManager.playSound(RES + "lose.wav");
            try {
                Thread.sleep(4000);  // 4000 milliseconds = 4 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}