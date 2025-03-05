import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Player {
    private int x, y;
    private int velocityY = 0;
    private final double gravity = 1;
    private final int jumpForce = -30;
    private boolean isJumping = false;
    private Image playerImage;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 100;

    public Player(int startX, int startY, String imagePath) {
        this.x = startX;
        this.y = startY;
        try {
            this.playerImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (playerImage != null) {
            g.drawImage(playerImage, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }
}
