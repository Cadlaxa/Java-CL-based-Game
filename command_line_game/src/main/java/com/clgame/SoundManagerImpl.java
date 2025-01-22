package com.clgame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoundManagerImpl implements SoundManager {
    private Clip backgroundMusicClip;
    private Random random = new Random();
    private List<String> highTileSounds; // List to store sounds for tiles greater than 2048
    private List<String> randomTileSounds; // List to store random sounds that will be played at intervals

    public SoundManagerImpl() {
        // Initialize random sounds that will be played periodically
        randomTileSounds = new ArrayList<>();
        randomTileSounds.add("command_line_game/src/main/resources/tiles1/one.wav");
        randomTileSounds.add("command_line_game/src/main/resources/tiles1/two.wav");
        randomTileSounds.add("command_line_game/src/main/resources/tiles1/three.wav");
        randomTileSounds.add("command_line_game/src/main/resources/tiles1/four.wav");
        randomTileSounds.add("command_line_game/src/main/resources/tiles1/five.wav");
        randomTileSounds.add("command_line_game/src/main/resources/random1/ate.wav");
        randomTileSounds.add("command_line_game/src/main/resources/random1/do-mi-sol.wav");
        randomTileSounds.add("command_line_game/src/main/resources/random1/keep-it-going.wav");
        randomTileSounds.add("command_line_game/src/main/resources/random1/my-lime.wav");
    }

    @Override
    public void playBackgroundMusic(List<String> musicFilePaths) {
        try {
            if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
                backgroundMusicClip.stop();
            }

            // Randomly select a music file from the list
            String musicFilePath = musicFilePaths.get(random.nextInt(musicFilePaths.size()));
            File musicFile = new File(musicFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioIn);

            // Set volume for the background music
            setVolume(0.7f);  // Set volume level (0.0 - 1.0)

            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            backgroundMusicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
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

    // Method to play random sound for tiles > 2048
    public void playRandomTileSound() {
        if (!highTileSounds.isEmpty()) {
            String randomSound = highTileSounds.get(random.nextInt(highTileSounds.size()));
            playSound(randomSound);
        }
    }

    // Method to play random sound for tiles > 2048
    public void playRandomSound() {
        // Random chance to play a tile sound
        if (random.nextInt(100) < 4) { // 4% chance to play a random tile sound
            String randomSound = randomTileSounds.get(random.nextInt(randomTileSounds.size()));
            playSound(randomSound);
        }
    }
}