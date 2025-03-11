import Enemy;

public abstract class Item extends Enemy {
    public Item(int startX, int startY, String imagePath) {
        super(startX, startY, imagePath);
    }
    public Item(int startX, int startY, int width, int height, String imagePath) {
        super(startX, startY, width, height, imagePath);
    }
}