package enemies;

import java.awt.Image;

import javax.swing.JLabel;

public abstract class AbsEnemy extends JLabel {
    protected Image img;
    protected int speed;

    public AbsEnemy(Image img, int speed) {
        this.img = img;
        this.speed = speed;
    }
}
