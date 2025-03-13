import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Enemy extends JLabel {
    private Image[] frames = new Image[2];
    private int currentFrame = 0;
    private Timer animationTimer;
    private int frameDelay = 200;
    
    private int x, y;
    private int width = 100, height = 100;
    private boolean scored = false;

    public Enemy(int startX, int startY, String imagePath) {
        this.x = startX;
        this.y = startY;
        try {
            this.frames[0] = ImageIO.read(new File(imagePath));
            this.frames[1] = this.frames[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Enemy(int startX, int startY, int width, int height, String imagePath) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        
        try {
            this.frames[0] = ImageIO.read(new File(imagePath));
            this.frames[1] = this.frames[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Enemy(int startX, int startY, int width, int height, String imagePath1, String imagePath2) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        
        try {
            this.frames[0] = ImageIO.read(new File(imagePath1));
            this.frames[1] = ImageIO.read(new File(imagePath2));
            
            animationTimer = new Timer(frameDelay, e -> {
                currentFrame = (currentFrame + 1) % 2;
            });
            animationTimer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Enemy(int startX, int startY, String imagePath1, String imagePath2) {
        this.x = startX;
        this.y = startY;
        
        try {
            this.frames[0] = ImageIO.read(new File(imagePath1));
            this.frames[1] = ImageIO.read(new File(imagePath2));
            
            animationTimer = new Timer(frameDelay, e -> {
                currentFrame = (currentFrame + 1) % 2;
            });
            animationTimer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        if (frames != null && frames[currentFrame] != null) {
            g.drawImage(frames[currentFrame], x, y, width, height, null);
        }
    }

    public void update(int speed) {
        setX(getX() + speed);
    }
    
    public void startAnimation() {
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }
    
    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
            currentFrame = 0;
        }
    }
    
    public void setFrameDelay(int delay) {
        this.frameDelay = delay;
        if (animationTimer != null) {
            animationTimer.setDelay(delay);
        }
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public Image getImg() { return frames[currentFrame]; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public boolean isScored() { return scored; }
    public void setScored(boolean scored) { this.scored = scored; }
    public int getFrameDelay() { return frameDelay; }
    public Image getImage() { return frames[currentFrame]; }
}
