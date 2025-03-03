import java.awt.Image;

import javax.swing.JLabel;

public class Player extends JLabel {
    private int hp;
    private Image img;
    private double velocity;
    private double jumpHeight;


    Player(int hp, image img, double velocity, double jumpHeight){
        this.img = img;
        this.hp = hp;
        this.velocity = velocity;
        this.jumpHeight = jumpHeight;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public double getVelocity() {
        return velocity;
    }
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
    public double getJumpHeight() {
        return jumpHeight;
    }
    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
    public Image getImg() {
        return img;
    }
}
