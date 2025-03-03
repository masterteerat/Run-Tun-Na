import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;

    private int playerX = 100, playerY = 375;
    private int velocityY = 0;
    private final int gravity = 1;
    private final int jumpForce = -18;
    private boolean isJumping = false;

    private Image playerImage;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 100;

    public GameScreen() {
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        try {
            playerImage = ImageIO.read(new File("src/sun.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (isJumping) {
            playerY += velocityY;
            velocityY += gravity;
            if (playerY >= 375) {
                playerY = 375;
                isJumping = false;
                velocityY = 0;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playerImage != null) {
            g.drawImage(playerImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!isJumping) {
                isJumping = true;
                velocityY = jumpForce;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}