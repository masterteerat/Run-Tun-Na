import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private boolean isGameOver = false;
    private GameRunner gameRunner;

    private int playerX = 100, playerY = 375;
    private int velocityY = 0;
    private final int gravity = 1;
    private final int jumpForce = -18;
    private boolean isJumping = false;

    private Image playerImage;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 100;

    private Image cat;

    private JButton retry, backMenu;

    public GameScreen(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setLayout(null); // ต้องกำหนด Layout เป็น null เพื่อใช้ setBounds()

        try {
            playerImage = ImageIO.read(new File("src/sun.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cat = ImageIO.read(new File("src/cat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        if (running) return;
        running = true;
        isGameOver = false;
        removeButtons(); // ลบปุ่มเก่าก่อนเริ่มเกมใหม่
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
        if (isGameOver) return;

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

        retry = new JButton("RETRY");
        retry.setFont(new Font("Arial", Font.BOLD, 30));
        retry.setBounds((GameRunner.SCREEN_WIDTH / 2) - 160, 450, 150, 50);
        retry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        backMenu = new JButton("EXIT");
        backMenu.setFont(new Font("Arial", Font.BOLD, 20));
        backMenu.setBounds((GameRunner.SCREEN_WIDTH / 2) + 10, 450, 180, 50);
        backMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunner.showGameMenu();
            }
        });

        add(retry);
        add(backMenu);
        repaint();
    }

    public void restartGame() {
        removeButtons(); // ลบปุ่มก่อนเริ่มใหม่
        requestFocusInWindow(); // ทำให้ JPanel รับ Key Event อีกครั้ง
    
        // รีเซ็ตค่าทั้งหมด
        playerX = 100;
        playerY = 375;
        velocityY = 0;
        isJumping = false;
        isGameOver = false;
        running = false; // ตั้งค่าเป็น false ก่อนเริ่มใหม่
    
        addKeyListener(this); // เพิ่ม KeyListener กลับมาใหม่
        startGame(); // เรียก startGame() เพื่อเริ่ม Thread ใหม่
    }
    
    private void removeButtons() {
        if (retry != null) {
            remove(retry);
            retry = null;
        }
        if (backMenu != null) {
            remove(backMenu);
            backMenu = null;
        }
        repaint();
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playerImage != null) {
            g.drawImage(playerImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);
            g.drawImage(cat, playerX+200, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        }

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) return;

        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!isJumping) {
                isJumping = true;
                velocityY = jumpForce;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameRunner.showGameMenu();
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
            gameOver();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
