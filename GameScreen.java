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
    private boolean isGameOver = false; // เพิ่มตัวแปร Game Over
    private GameRunner gameRunner;

    private int playerX = 100, playerY = 375;
    private int velocityY = 0;
    private final int gravity = 1;
    private final int jumpForce = -18;
    private boolean isJumping = false;

    private Image playerImage;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 100;

    private JButton retry, backMenu;

    public GameScreen(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
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
        if (running) return;
        running = true;
        isGameOver = false; // รีเซ็ตสถานะ Game Over
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (isGameOver) return; // ถ้าเกมจบแล้ว ไม่ต้องอัปเดตอะไร

        if (isJumping) {
            playerY += velocityY;
            velocityY += gravity;
            if (playerY >= 375) {
                playerY = 375;
                isJumping = false;
                velocityY = 0;
            }
        }

        if (playerY > 500) {
            gameOver();
        }
    }

    public void gameOver() {
        running = false;
        isGameOver = true;
        
        retry = new JButton("Retry");
        retry.setFont(new Font("Arial", Font.BOLD, 45));
        retry.setBounds(((GameRunner.SCREEN_WIDTH - 78) / 2) - 100, 450, 150, 45);
        add(retry);

        backMenu = new JButton("Exit");
        backMenu.setFont(new Font("Arial", Font.BOLD, 45));
        backMenu.setBounds(((GameRunner.SCREEN_WIDTH - 78) / 2) + 100, 450, 150, 45);
        add(backMenu);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playerImage != null) {
            g.drawImage(playerImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        }

        // แสดงข้อความ "Game Over" ถ้าเกมจบ
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) return; // ไม่ให้กดปุ่มตอนเกมจบ

        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!isJumping) {
                isJumping = true;
                velocityY = jumpForce;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameRunner.showGameMenu();
        }
        if (e.getKeyCode() == 79) {
            gameOver();
        }
    }
}
