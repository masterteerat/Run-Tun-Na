import javax.swing.*;

public class GameRunner extends JFrame {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private GameMenu gameMenu;
    private GameScreen gameScreen;

    public GameRunner() {
        setTitle("Suntana");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gameMenu = new GameMenu(this);
        gameScreen = new GameScreen();

        add(gameMenu);
        setVisible(true);
    }
    public void showGameScreen() {
        remove(gameMenu);
        add(gameScreen);
        revalidate();
        repaint();
        gameScreen.requestFocusInWindow();
        gameScreen.startGame();
    }
    

    public static void main(String[] args) {
        new GameRunner();
    }
}