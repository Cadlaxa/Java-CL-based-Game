package com.clgame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoundManagerImpl implements SoundManager {
    private Clip backgroundMusicClip;
    private Clip audioClip;
    private Random random = new Random();
    private Map<Integer, List<String>> tileSounds;
    private List<String> highTileSounds; // List to store sounds for tiles greater than 2048
    private List<String> randomTileSounds; // List to store random sounds that will be played at intervals
    final String TILES1 = "command_line_game/src/main/resources/tiles1/";
    final String RAND1 = "command_line_game/src/main/resources/random1/";
    final String TILES2 = "command_line_game/src/main/resources/tiles2/";


    public SoundManagerImpl() {
        // Initialize random sounds that will be played periodically
        randomTileSounds = new ArrayList<>();
        randomTileSounds.add(TILES1 + "one.wav");
        randomTileSounds.add(TILES1 + "two.wav");
        randomTileSounds.add(TILES1 + "three.wav");
        randomTileSounds.add(TILES1 + "four.wav");
        randomTileSounds.add(TILES1 + "five.wav");
        randomTileSounds.add(TILES1 + "256.wav");
        randomTileSounds.add(RAND1 + "ate.wav");
        randomTileSounds.add(RAND1 + "do-mi-sol.wav");
        randomTileSounds.add(RAND1 + "keep-it-going.wav");
        randomTileSounds.add(RAND1 + "my-lime.wav");
        randomTileSounds.add(TILES2 + "512.wav");
        randomTileSounds.add(TILES2 + "2048.wav");
    }

    @Override
    public void playBackgroundMusic(List<String> musicFilePaths) {
        // Stop any currently playing background music
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
        // Start playing the music, looping through the list of music files
        playNextTrack(musicFilePaths, 0); // Start with the first track
    }

    private void playNextTrack(List<String> musicFilePaths, int currentIndex) {
        try {
            // Randomly select the next track
            int nextIndex = random.nextInt(musicFilePaths.size()); // Randomly pick the next track index
            String musicFilePath = musicFilePaths.get(nextIndex);
            File musicFile = new File(musicFilePath);
    
            // Check if the file exists before attempting to play it
            if (!musicFile.exists()) {
                System.out.println("Music file does not exist: " + musicFilePath);
                return;
            }
    
            // Create an audio input stream from the selected file
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioIn);
            setVolume(0.4f);  // Set volume level (0.0 - 1.0)
            backgroundMusicClip.start();
    
            // Use a listener to play the next track when the current one finishes
            backgroundMusicClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playNextTrack(musicFilePaths, nextIndex);
                }
            });
    
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("Error playing next track: " + e.getMessage());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    @Override
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }

    @Override
    public void playSound(String fileName) {
        try {
            File soundFile = new File(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip soundClip = AudioSystem.getClip();
            soundClip.open(audioIn);
            soundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    // Method to set volume
    private void setVolume(float volume) {
        if (backgroundMusicClip != null) {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert volume from a range of 0.0 to 1.0 to dB scale
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);  // Set the volume in decibels
        }
    }

    @Override
    public void playTileSound(int tileValue) {
        if (tileSounds.containsKey(tileValue)) {
            List<String> sounds = tileSounds.get(tileValue);
            String randomSound = sounds.get(random.nextInt(sounds.size())); // Pick a random sound
            playSound(randomSound);
        }
    }

    // Method to play random sound for tiles > 2048
    public void playRandomTileSound() {
        if (!highTileSounds.isEmpty()) {
            String randomSound = highTileSounds.get(random.nextInt(highTileSounds.size()));
            playSound(randomSound);
        }
    }

    @Override
    public void playRandomSound() {
        // Random chance to play a tile sound based on the calculated probability
        if (random.nextInt(100) < 8) {
            String randomSound = randomTileSounds.get(random.nextInt(randomTileSounds.size()));
            // Check if another sound is playing and stop it
            if (audioClip != null && audioClip.isRunning()) {
                audioClip.stop(); // Stop the currently playing audio
            }
            playSound(randomSound);
        }
    }
}