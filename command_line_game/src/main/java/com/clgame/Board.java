package com.clgame;

import java.util.Random;

public class Board {
    private static final int SIZE = 4;
    private static final char MOVE_LEFT = 'A';
    private static final char MOVE_RIGHT = 'D';
    private static final char MOVE_UP = 'W';
    private static final char MOVE_DOWN = 'S';
    final String VERSION = "v2.1";

    private int score; // Track the score
    private int[][] board;
    private Random random;
    private int highestTile; // Track the highest tile

    public Board() {
        this.board = new int[SIZE][SIZE];
        this.random = new Random();
        this.score = 0; // Initialize score to 0
        this.highestTile = 0; // Initialize highest tile to 0
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public int getHighestTile() {
        return highestTile;
    }

    public void showBoard() {
		clearScreen();  // Clear screen before displaying the board

		// ANSI escape codes for bold, foreground and background colors
		final String RESET = "\u001B[0m";
		final String BOLD = "\u001B[1m";
	
		// Colors for background (box) and foreground (number) text
		final String RED = "\u001B[41m";      // Red background for 2
		final String ORANGE = "\u001B[48;5;214m";  // Orange background for 4
		final String YELLOW = "\u001B[43m";   // Yellow background for 8
		final String GREEN = "\u001B[42m";    // Green background for 16
		final String BLUE = "\u001B[44m";     // Blue background for 32
		final String INDIGO = "\u001B[48;5;57m";  // Indigo background for 64
		final String VIOLET = "\u001B[45m";    // Violet background for 128
		final String PINK = "\u001B[48;5;213m";   // Pink background for 256
		final String MAGENTA = "\u001B[48;5;201m";  // Magenta background for 512
		final String CYAN = "\u001B[46m";     // Cyan background for 1024
		final String GOLD = "\u001B[48;5;220m"; // Gold background for 2048
		final String WHITE = "\u001B[47m";    // White background for others

		final String YELLOWT = "\u001B[33m";

		System.out.println(BOLD + YELLOWT + "\r\n" + 
                    "   ____   ___  _  _    ___  \r\n" + 
                    "  |___ \\ / _ \\| || |  ( _ ) \r\n" + 
                    "    __) | | | | || |_ / _ \\ \r\n" + 
                    "   / __/| |_| |__   _| (_) |\r\n" + 
                    "  |_____|\\___/   |_|  \\___/ \r\n" + 
                    "         retro " + VERSION + "\r" + 
                    RESET);

		// print the top separator (sa taas to ng box)
		for (int i = 0; i < 4; i++) {
			System.out.print(BOLD + "-------");
		}
		System.out.println();
	
		// print each row
		for (int i = 0; i < 4; i++) {
			// print the blank space before the row
			System.out.print(BOLD + "|");
			for (int j = 0; j < 4; j++) {
				System.out.print(BOLD + "      |");
			}
			System.out.println();
	
			// print the first pipe symbol
			System.out.print(BOLD + "|");
			for (int j = 0; j < 4; j++) {
				if (board[i][j] == 0) {
					System.out.printf(BOLD + "  %-3s |", "");
				} else {
					String color = getColorForTile(board[i][j]);
					String bgColor = getBackgroundColorForTile(board[i][j]);
					System.out.printf(BOLD + "%s" + bgColor + BOLD + "  %-3s " + RESET + BOLD + "|", color, board[i][j]);
				}
			}
			System.out.println();
	
			// print the blank space after the row
			System.out.print(BOLD + "|");
			for (int j = 0; j < 4; j++) {
				System.out.print(BOLD + "      |");
			}
			System.out.println();
	
			// print the bottom separator
			for (int j = 0; j < 4; j++) {
				System.out.print(BOLD + "-------");
			}
			System.out.println();
		}
	}
	
	// This method returns the foreground (number) color
	private String getColorForTile(int value) {
		switch (value) {
			case 2: return "\u001B[31m";  // Red for 2
			case 4: return "\u001B[38;5;214m"; // Orange for 4
			case 8: return "\u001B[33m";   // Yellow for 8
			case 16: return "\u001B[32m";  // Green for 16
			case 32: return "\u001B[34m";  // Blue for 32
			case 64: return "\u001B[38;5;57m"; // Indigo for 64
			case 128: return "\u001B[35m";  // Violet for 128
			case 256: return "\u001B[38;5;213m";  // Pink for 256
			case 512: return "\u001B[38;5;201m"; // Magenta for 512
			case 1024: return "\u001B[36m";  // Cyan for 1024
			case 2048: return "\u001B[38;5;220m";  // Gold for 2048
			default: return "\u001B[37m";  // White for other numbers
		}
	}
	
	// This method returns the background color for each tile
	private String getBackgroundColorForTile(int value) {
		switch (value) {
			case 2: return "\u001B[41m";      // Red background for 2
			case 4: return "\u001B[48;5;214m";  // Orange background for 4
			case 8: return "\u001B[43m";   // Yellow background for 8
			case 16: return "\u001B[42m";    // Green background for 16
			case 32: return "\u001B[44m";     // Blue background for 32
			case 64: return "\u001B[48;5;57m";  // Indigo background for 64
			case 128: return "\u001B[45m";    // Violet background for 128
			case 256: return "\u001B[48;5;213m";  // Pink background for 256
			case 512: return "\u001B[48;5;201m"; // Magenta background for 512
			case 1024: return "\u001B[46m";     // Cyan background for 1024
			case 2048: return "\u001B[48;5;220m"; // Gold background for 2048
			default: return "\u001B[47m";    // White background for others
		}
	}

    public void addRandomDigit(int digit) {
        int i = random.nextInt(SIZE);
        int j = random.nextInt(SIZE);

        // Ensure random position is empty
        while (board[i][j] != 0) {
            i = random.nextInt(SIZE);
            j = random.nextInt(SIZE);
        }
        board[i][j] = digit;
    }

    public int[] processLeftMove(int[] row) {
        int[] newRow = new int[SIZE];
        int j = 0;

        // Copy non-zero values
        for (int value : row) {
            if (value != 0) {
                newRow[j++] = value;
            }
        }

        // Merge adjacent equal values
        for (int i = 0; i < SIZE - 1; i++) {
            if (newRow[i] != 0 && newRow[i] == newRow[i + 1]) {
                newRow[i] *= 2;
                score += newRow[i]; // Update score on merge
                highestTile = Math.max(highestTile, newRow[i]); // Update highest tile
                newRow[i + 1] = 0;
            }
        }

        // Compact again after merging
        int[] compactedRow = new int[SIZE];
        j = 0;
        for (int value : newRow) {
            if (value != 0) {
                compactedRow[j++] = value;
            }
        }

        return compactedRow;
    }

    public int[] reverseArray(int[] arr) {
        int[] reverseArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reverseArr[i] = arr[arr.length - 1 - i];
        }
        return reverseArr;
    }

    public int[] processRightMove(int[] row) {
        return reverseArray(processLeftMove(reverseArray(row)));
    }

    public void processMove(char move) {
        switch (move) {
            case MOVE_LEFT:
                for (int i = 0; i < SIZE; i++) {
                    board[i] = processLeftMove(board[i]);
                }
                break;
            case MOVE_RIGHT:
                for (int i = 0; i < SIZE; i++) {
                    board[i] = processRightMove(board[i]);
                }
                break;
            case MOVE_UP:
                for (int j = 0; j < SIZE; j++) {
                    int[] column = new int[SIZE];
                    for (int i = 0; i < SIZE; i++) {
                        column[i] = board[i][j];
                    }
                    column = processLeftMove(column);
                    for (int i = 0; i < SIZE; i++) {
                        board[i][j] = column[i];
                    }
                }
                break;
            case MOVE_DOWN:
                for (int j = 0; j < SIZE; j++) {
                    int[] column = new int[SIZE];
                    for (int i = 0; i < SIZE; i++) {
                        column[i] = board[i][j];
                    }
                    column = processRightMove(column);
                    for (int i = 0; i < SIZE; i++) {
                        board[i][j] = column[i];
                    }
                }
                break;
        }
    }

    public boolean isGameOver() {
        if (gameWon()) {
            return true;
        }

        if (searchOnBoard(0)) {
            return false;
        }

        return !userCanMakeAMove();
    }

    public boolean gameWon() {
        return searchOnBoard(2048);
    }

    private boolean searchOnBoard(int value) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean userCanMakeAMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i > 0 && board[i][j] == board[i - 1][j]) return true; // Check up
                if (j > 0 && board[i][j] == board[i][j - 1]) return true; // Check left
                if (i < SIZE - 1 && board[i][j] == board[i + 1][j]) return true; // Check down
                if (j < SIZE - 1 && board[i][j] == board[i][j + 1]) return true; // Check right
            }
        }
        return false;
    }

    private void clearScreen() {
        // Check if the operating system is Windows
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            try {
                // Windows uses a different command to clear the console
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Use ANSI escape codes for Unix-based systems
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    
}
