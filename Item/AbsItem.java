package Item;

import Enemies.AbsEnemy;
import Enemies.enemy;

public abstract class AbsItem extends AbsEnemy {
 public AbsItem(int startX, int startY, int speed, String imagePath) {
        super(startX, startY, speed, imagePath);
    }
}
