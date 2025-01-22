package com.clgame;

import java.util.List;

public interface SoundManager {
    void playBackgroundMusic(List<String> musicFilePaths);
    void stopBackgroundMusic();
    void playSound(String fileName);
    void playRandomSound();
}
