import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

public class Player {
    private int x, y;
    private int velocityY = 0;
    private final double gravity = 1;
    private final int jumpForce = -25;
    private boolean isJumping = false;
    private boolean isShield = false;
    private boolean isCat = false;

    private Image[] walkFrames = new Image[3];
    private int currentFrame = 0;
    private Timer animationTimer;
    private int frameDelay;

    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 100;

    public Player(int startX, int startY, String imagePath1, String imagePath2, String imagePath3) {
        this.x = startX;
        this.y = startY;
        frameDelay = 200;

        try {
            walkFrames[0] = ImageIO.read(new File(imagePath1)); 
            walkFrames[1] = ImageIO.read(new File(imagePath2)); 
            walkFrames[2] = ImageIO.read(new File(imagePath3)); 
        } catch (IOException e) {
            e.printStackTrace();
        }

            animationTimer = new Timer(frameDelay, e -> {
            currentFrame = (currentFrame + 1) % 3;
        });
    }

    public void update() {
        if (isJumping) {
            y += velocityY;
            velocityY += gravity;
            if (y >= 475) {
                y = 475;
                isJumping = false;
                velocityY = 0;
            }
        }
    }

    public void startWalking() {
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    public void stopWalking() {
        animationTimer.stop();
        currentFrame = 2;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            velocityY = jumpForce;
        }
    }

    public void resetPosition() {
        this.x = 100;
        this.y = 375;
        this.velocityY = 0;
        this.isJumping = false;
    }

    public void paint(Graphics g) {
        if (walkFrames[currentFrame] != null) {
            g.drawImage(walkFrames[currentFrame], x, y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public boolean isJumping() { return isJumping; }
    public void setJumping(boolean jumping) { isJumping = jumping; }
    public boolean isShield() { return isShield; }
    public void setShield(boolean shield) { isShield = shield; }
    public boolean isCat() { return isCat; }
    public void setCat(boolean cat) { isCat = cat; }

    public Rectangle getBounds() {
        return new Rectangle(x + 20, y, PLAYER_WIDTH - 40, PLAYER_HEIGHT);
    }

    public void setFrameDelay(int delay) {
        this.frameDelay = delay;
        if (animationTimer != null) {
            animationTimer.setDelay(delay);
        }
    }
    public int getFrameDelay() { return frameDelay; }
}
