package com.clgame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManagerImpl implements SoundManager {
    private Clip backgroundMusicClip;

    @Override
    public void playBackgroundMusic(String musicFilePath) {
        try {
            if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
                backgroundMusicClip.stop();
            }

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
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
