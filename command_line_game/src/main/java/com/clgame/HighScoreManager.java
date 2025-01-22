package com.clgame;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HighScoreManager {
    private static final String FILE_NAME = "command_line_game/src/main/resources/highscore.yaml";

    public int loadHighScore() {
        Yaml yaml = new Yaml();
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Map<String, Object> data = yaml.load(input);
            return (int) data.getOrDefault("highscore", 0);
        } catch (FileNotFoundException e) {
            return 0; // Default high score if file doesn't exist
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void saveHighScore(int highScore) {
        Yaml yaml = new Yaml();
        Map<String, Object> data = new HashMap<>();
        data.put("highscore", highScore);

        try (Writer writer = new FileWriter(FILE_NAME)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
