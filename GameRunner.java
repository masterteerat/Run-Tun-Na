import javax.swing.*;
import java.awt.*;

public class GameRunner extends JFrame {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private GameScreen gameScreen;

    public GameRunner() {
        setTitle("Suntana");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the game screen
        gameScreen = new GameScreen();
        add(gameScreen);

        // Make the window visible
        setVisible(true);
        
        // Show the menu
        gameScreen.startGame();
    }

    public static void main(String[] args) {
        new GameRunner();
    }
}