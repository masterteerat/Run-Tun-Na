import javax.swing.*;
import java.io.*;

public class GameRunner extends JFrame {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private static int highScore;
    private static final String SCORE_FILE = "highscore.txt"; // กำหนดชื่อไฟล์สำหรับเก็บคะแนนสูงสุด

    private GameMenu gameMenu;
    private GameScreen gameScreen;

    public GameRunner() {
        setTitle("Suntana");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadHighScore(); // โหลด high score ตอนเริ่มเกม

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
            saveHighScore(); // บันทึกคะแนนเมื่อมีการทำลายสถิติ
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
            highScore = 0; // ถ้าไฟล์ไม่มีหรือเกิดข้อผิดพลาด ให้เริ่มต้นที่ 0
        }
    }

    private void saveHighScore() {
        try (FileWriter writer = new FileWriter(SCORE_FILE)) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดในการบันทึกคะแนนสูงสุด: " + e.getMessage());
        }
    }
    public void resetHighScore() {
        highScore = 0;
        saveHighScore(); // อัพเดทไฟล์ให้เป็น 0
    }
    

    public static void main(String[] args) {
        new GameRunner();
    }
}
