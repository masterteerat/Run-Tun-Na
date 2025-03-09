package Enemies;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

public abstract class AbsEnemy extends JLabel {
    private Image img;
    private int x, y;
    private final int ENEMY_WIDTH = 100, ENEMY_HEIGHT = 100;
    private boolean scored = false;

    public AbsEnemy(int startX, int startY, String imagePath) {
        this.x = startX;
        this.y = startY;
        try {
            this.img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        if (img != null) {
            g.drawImage(img, x, y, ENEMY_WIDTH, ENEMY_HEIGHT, null);
        }
    }

    public void update(int speed) {
        setX(getX() + speed);

        if (getX() < -100) {
            setX(1300);
            scored = false;
        }
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public Image getImg() { return img; }
    public void setImg(Image img) { this.img = img; }
    public Rectangle getBounds() { return new Rectangle(x, y, ENEMY_WIDTH, ENEMY_HEIGHT); }
    public boolean isScored() { return scored; }
    public void setScored(boolean scored) { this.scored = scored; }
}
