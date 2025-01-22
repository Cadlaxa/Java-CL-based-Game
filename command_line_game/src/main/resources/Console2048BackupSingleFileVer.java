package com.clgame;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Console2048BackupSingleFileVer {

	// Field to store the background music clip
    private Clip backgroundMusicClip;
	public static final int SIZE = 4;
	public static final char MOVE_LEFT = 'A';
	public static final char MOVE_RIGHT = 'D';
	public static final char MOVE_UP = 'W';
	public static final char MOVE_DOWN = 'S';

	private int board[][];
	private Random random;
	private Scanner scanner;

	public Console2048BackupSingleFileVer() {
		// all locations of this 2D array is 0
		board = new int[4][4];

		// create a random object
		random = new Random();

		// create the Scanner object to read from the user
		scanner = new Scanner(System.in);
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

		System.out.println(BOLD + YELLOWT + "\r\n" + //
						"   ____   ___  _  _    ___  \r\n" + //
						"  |___ \\ / _ \\| || |  ( _ ) \r\n" + //
						"    __) | | | | || |_ / _ \\ \r\n" + //
						"   / __/| |_| |__   _| (_) |\r\n" + //
						"  |_____|\\___/   |_|  \\___/ \r\n" + //
						"" + RESET);
	
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
		// add random digit on the board

		// generate a pair of i,j
		int i = random.nextInt(4);
		int j = random.nextInt(4);

		// generate i,j as long as this location on the board is occupied
		while( board[i][j] != 0 ) {
			i = random.nextInt(4);;
			j = random.nextInt(4);
		}
		
		// set the digit at this location
		board[i][j] = digit;
	}

	public boolean searchOnBoard(int x) {
		for( int i=0; i<4; i++ ) {
			for( int j=0; j<4; j++ ) {
				if( board[i][j] == x ) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean gameWon() {
		return searchOnBoard(2048);
	}

	public boolean userCanMakeAMove() {
		// check 3x3 board
		for( int i=0; i<3; i++ ) {
			for( int j=0; j<3; j++ ){
				// if two adjacent locations have equal value, return true
				if( board[i][j] == board[i][j+1] || 
					board[i][j] == board[i+1][j] 
					) {
					return true;
				}
			}
		}
		// check if two equal adjacent values in the last row
		for( int j=0; j<3; j++ ) {
			if( board[3][j] == board[3][j+1] ) {
				return true;
			}
		}

		// check if two equal adjacent values in the last column
		for( int i=0; i<3; i++ ) {
			if( board[i][3] == board[i+1][3] ) {
				return true;
			}
		}

		// finally, return false
		return false;
	}

	public boolean isGameOver() {
		// game is over is there is a 2048 tile on the board
		if( gameWon() ) {
			return true;
		}

		// game is not over, if there is a blank tile on the board
		if( searchOnBoard(0) ) {
			return false;
		}

		// finally, game is not over if user can make a move
		return !userCanMakeAMove();
	}

	public char getUserMove() {

		// ANSI escape codes for bold, foreground and background colors
		final String RESET = "\u001B[0m";          // Reset
		final String BOLD = "\u001B[1m";           // Bold
		final String GREEN = "\u001B[32m";
		final String BLUE = "\u001B[34m";
		final String CYAN = "\u001B[36m";
		final String PURPLE = "\u001B[35m";
		final String YELLOW = "\u001B[33m";

	
		// show all possible moves
		System.out.println(BOLD + GREEN + "\nCHOOSE A MOVE: " + RESET);
		System.out.println(BOLD + YELLOW + "W/w: Up" + RESET);
		System.out.println(BOLD + CYAN + "S/s: Down" + RESET);
		System.out.println(BOLD + BLUE + "A/a: Left" + RESET);
		System.out.println(BOLD + PURPLE + "D/d: Right" + RESET);
		System.out.print(BOLD + "Enter move: ");

		// read the move from the user
		String moveInput = scanner.nextLine();
		if( moveInput.equalsIgnoreCase("a") || 
		moveInput.equalsIgnoreCase("w") || 
		moveInput.equalsIgnoreCase("s") || 
		moveInput.equalsIgnoreCase("d")
			) { 
			return moveInput.toUpperCase().charAt(0);
		}

		// if the input is invalid
		final String RED = "\u001B[41m";
		System.out.println(BOLD + RED + "\nInvalid Input!" + RESET);
		playSound("command_line_game/src/main/resources/alert.wav");
		System.out.println();

		// Wait for 3 seconds before continuing
		try {
			Thread.sleep(2000);  // 2000 milliseconds = 2 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// show the board 
		showBoard(); 

		// recur
		return getUserMove();
	}

	public int[] processLeftMove(int row[]) {
		// copy non-0 values
		int newRow[] = new int[4];
		int j = 0;
		for( int i=0; i<4; i++ ) {
			if( row[i]!=0 ) {
				newRow[j++] = row[i];
			}
		}

		// merge the values in this new row
		for( int i=0; i<3; i++ ) {
			if( newRow[i]!=0 && newRow[i]==newRow[i+1]) {
				newRow[i] = 2*newRow[i];	// a)
				// copy the remaining values  // b)
				for( j=i+1; j<3; j++ ) {
					newRow[j] = newRow[j+1];
				}
				// c) set the last location of this row to 0
				newRow[3] = 0;
			}
		}
		return newRow;
	}

	public int[] reverseArray(int arr[]) {
		int[] reverseArr = new int[arr.length];
		for( int i=arr.length-1; i>=0; i-- ) {
			reverseArr[i] = arr[arr.length - i - 1];
		}
		return reverseArr;
	}

	public int[] processRightMove(int row[]) {
		// copy all the non-0 values 
		int newRow[] = new int[4];
		int j = 0;
		for( int i=0; i<4; i++ ) {
			if( row[i]!=0 ) {
				newRow[j++] = row[i];
			}
		}

		// reverse the row
		newRow = reverseArray(newRow);

		// process left move
		newRow = processLeftMove(newRow);

		// reverse the row
		return reverseArray(newRow);
	}

	public void processMove(char move) {
		switch(move) {
			case MOVE_LEFT:
			{
				// for each row
				for( int i=0; i<4; i++ ){
					// get the new row
					int newRow[] = processLeftMove(board[i]);
					// copy values from the new row to the row
					for( int j=0; j<4; j++ ) {
						board[i][j] = newRow[j];
					}
				}
			}
			break;
			case MOVE_RIGHT:
			{
				// for each row
				for( int i=0; i<4; i++ ){
					// get the new row
					int newRow[] = processRightMove(board[i]);
					// copy values from the new row to the row
					for( int j=0; j<4; j++ ) {
						board[i][j] = newRow[j];
					}
				}
			}
			break;
			case MOVE_UP:
			{
				// for each column
				for( int j=0; j<4; j++ ) {
					// create a row from column values
					int row[] = new int[4];
					for( int i=0; i<4; i++ ) {
						row[i] = board[i][j];
					}

					// process left move on this row
					int newRow[] = processLeftMove(row);

					// copy the values back into the column
					for( int i=0; i<4; i++ ) {
						board[i][j] = newRow[i];
					}
				}
			}
			break;
			case MOVE_DOWN:
			{
				// for each column
				for( int j=0; j<4; j++ ) {
					// create a row from column values
					int row[] = new int[4];
					for( int i=0; i<4; i++ ) {
						row[i] = board[i][j];
					}

					// process right move on this row
					int newRow[] = processRightMove(row);

					// copy the values back into the column
					for( int i=0; i<4; i++ ) {
						board[i][j] = newRow[i];
					}
				}
			}
			break;
		}
	}

	public void play() {
        // play the game

        // setup the board - add random 2 and add random 4
        addRandomDigit(2);
        addRandomDigit(4);

        // Start playing background music
        playBackgroundMusic("command_line_game/src/main/resources/melody-loop.wav");

        // while the game is not over
        while (!isGameOver()) {
            showBoard();

            // ask the user to make a move
            char move = getUserMove();

            // process the move
            processMove(move);
            playSound("command_line_game/src/main/resources/move.wav");

            // add random 2/4
            int r = random.nextInt(100);
            if (r % 2 == 0) {
                addRandomDigit(2);
            } else {
                addRandomDigit(4);
            }
        }

		// ANSI escape codes for bold, foreground and background colors
		final String RESET = "\u001B[0m";
		final String BOLD = "\u001B[1m";
	
		final String MAGENTA = "\u001B[48;5;201m";
		final String CYAN = "\u001B[46m";

        if (gameWon()) {
            System.out.println(BOLD + CYAN + "\nYou WON SLAYED!" + RESET);
            playSound("command_line_game/src/main/resources/win.wav");
        } else {
            System.out.println(BOLD + MAGENTA + "\nYou LOST!" + RESET);
            playSound("command_line_game/src/main/resources/lose.wav");
        }
        // Stop background music when the game ends
        stopBackgroundMusic();
    }

	public void clearScreen() {
        // Clear the console screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

	public void playBackgroundMusic(String musicFilePath) {
        try {
            // If a background music is already playing, stop it
            if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
                backgroundMusicClip.stop();
            }

            // Load and play the background music
            File musicFile = new File(musicFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioIn);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            backgroundMusicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to stop background music
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }

	public void playSound(String fileName) {
        try {
            File soundFile = new File(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

	static void showArray(int arr[]) {
		System.out.print("[");
		for( int i: arr) {
			System.out.print(i + " ");
		}
		System.out.print("]");
	}

	public static void main(String args[]) {
		Console2048BackupSingleFileVer Console2048BackupSingleFileVer = new Console2048BackupSingleFileVer();
		Console2048BackupSingleFileVer.play();
	}
}