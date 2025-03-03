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

    private int playerX = 100, playerY = 375; // Player position (on the floor by default)
    private int velocityY = 0; // Vertical velocity for jumping
    private final int gravity = 1; // Gravity effect
    private final int jumpForce = -18; // Jump force
    private boolean isJumping = false; // Check if the player is jumping

    private Image playerImage; // Player image for the character
    private final int PLAYER_WIDTH = 100;  // Player width
    private final int PLAYER_HEIGHT = 100; // Player height


    public GameScreen() {
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.CYAN); // Set the background color
        setFocusable(true);
        addKeyListener(this);

        // Load the player image
        try {
            playerImage = ImageIO.read(new File("src/sun.png")); // Load the image file (make sure it's in the correct path)
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
        // Jumping physics
        if (isJumping) {
            playerY += velocityY; // Move player based on velocity
            velocityY += gravity; // Apply gravity (fall down)

            // Check if the player lands on the ground
            if (playerY >= 375) {
                playerY = 375; // Set player back to the ground level
                isJumping = false; // Stop jumping
                velocityY = 0; // Reset velocity
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the player image (standing on the floor or jumping)
        if (playerImage != null) {
            g.drawImage(playerImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this); // Resize player image to match the new size
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Make the player jump when the spacebar is pressed and not already jumping
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == 38) {
            if (!isJumping) {
                isJumping = true;
                velocityY = jumpForce; // Set the jump velocity
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}