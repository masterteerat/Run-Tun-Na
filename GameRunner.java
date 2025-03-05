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
    
    public static void main(String[] args) {
        new GameRunner();
    }
}