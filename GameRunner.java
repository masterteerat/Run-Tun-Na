import javax.swing.*;

import java.awt.Font;
import java.io.*;

public class GameRunner extends JFrame {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private BGM bgm;

    private static int highScore;
    private static final String SCORE_FILE = "highscore.txt";

    private Font font;

    private GameMenu gameMenu;
    private GameScreen gameScreen;

    public GameRunner() {
        setTitle("Suntana");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bgm = new BGM("src/Sounds/BlueCat.wav");
        bgm.play();

        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("src/fonts/PressStart2P.ttf");
            if (fontStream == null) {
                throw new IOException();
            }
            font = Font.createFont(Font.PLAIN, fontStream).deriveFont(18f);
        } catch (Exception e) {
            e.printStackTrace();
            font = new Font("Arial", Font.BOLD, 36);
        }

        loadHighScore();

        gameScreen = new GameScreen(this);
        gameMenu = new GameMenu(this);
        showGameMenu();

        setVisible(true);
    }

    public void showGameMenu() {
        remove(gameScreen);
        add(gameMenu);
        gameScreen = new GameScreen(this);
        revalidate();
        repaint();
        gameMenu.requestFocusInWindow();
    }
    
    public void showGameScreen() {
        remove(gameMenu);
        gameMenu = new GameMenu(this);
        add(gameScreen);
        revalidate();
        repaint();
        gameScreen.requestFocusInWindow();
        gameScreen.startGame();
    }

    public void restartGame() {
        remove(gameScreen);
        gameScreen = new GameScreen(this);
        add(gameScreen);
        revalidate();
        repaint();
        gameScreen.requestFocusInWindow();
        gameScreen.startGame();
    }

    public void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    public int getHighScore() {
        return highScore;
    }

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    private void saveHighScore() {
        try (FileWriter writer = new FileWriter(SCORE_FILE)) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
  public void resetHighScore() {
    highScore = 0;
    saveHighScore();
    
    File file = new File(SCORE_FILE);
    if (file.exists()) {
        file.delete();
        repaint();
        revalidate();
    }
}
    public Font getFont() {
        return font;
    }
    
    public static void main(String[] args) {
        new GameRunner();
    }
}
