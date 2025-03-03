import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;

    private int playerX = 100, playerY = 480; // Player position
    private int velocityY = 0; // Jump velocity
    private final int gravity = 1; // Gravity
    private final int jumpForce = -18; // Jump force
    private boolean isJumping = false; // Check if jumping
    
    private int backgroundX = 0; // Background position for scrolling effect
    private ArrayList<Rectangle> obstacles; // List of obstacles
    private Random rand = new Random();

    public GameScreen() {
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        obstacles = new ArrayList<>();
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
        // Move background to simulate running
        backgroundX -= 5;
        if (backgroundX <= -GameRunner.SCREEN_WIDTH) {
            backgroundX = 0;
        }

        // Move obstacles
        Iterator<Rectangle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Rectangle obs = iterator.next();
            obs.x -= 5; // Move left
            if (obs.x + obs.width < 0) {
                iterator.remove(); // Remove off-screen obstacles
            }
        }

        // Spawn new obstacles randomly
        if (rand.nextInt(100) < 2) { // 2% chance to spawn per frame
            obstacles.add(new Rectangle(GameRunner.SCREEN_WIDTH, 480, 50, 50));
        }

        // Jumping physics
        if (isJumping) {
            playerY += velocityY;
            velocityY += gravity;
        }

        // Check if player lands on the ground
        if (playerY >= 480) {
            playerY = 480;
            isJumping = false;
            velocityY = 0;
        }

        // Check for collisions
        for (Rectangle obs : obstacles) {
            if (obs.intersects(new Rectangle(playerX, playerY, 50, 50))) {
                running = false; // Stop game on collision
                System.out.println("Game Over!");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw scrolling background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(backgroundX, 550, GameRunner.SCREEN_WIDTH, 50);
        g.fillRect(backgroundX + GameRunner.SCREEN_WIDTH, 550, GameRunner.SCREEN_WIDTH, 50);

        // Draw player
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, 50, 50);

        // Draw obstacles
        g.setColor(Color.YELLOW);
        for (Rectangle obs : obstacles) {
            g.fillRect(obs.x, obs.y, obs.width, obs.height);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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