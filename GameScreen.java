import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private boolean isGameOver = false;
    private GameRunner gameRunner;

    private JButton retry, backMenu;
    public JLabel scoreLabel;
    private int score = 0;

    private int ENEMY_SPEED = -7;
    private int speedLimit = -28;
    private Enemy currentEnemy;
    private Item currentItem;
    private Player player;
    private int catScoreCount = 0;
    
    private Image floor;
    private Image cloud1;
    private Image cloud2;
    private Image cloud3;

    private JLabel debug;
    private boolean isDebug = false;

    private ArrayList<Enemy> enemies;
    private ArrayList<Item> items;
    
    // Track what's currently on screen
    private boolean isEnemyActive = false;
    private boolean isItemActive = false;
    
    public GameScreen(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.decode("#E8FEFF"));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setLayout(null);
        
        try {
            floor = ImageIO.read(new File("src/Elements/floor.png"));
            cloud1 = ImageIO.read(new File("src/Elements/cloud2.png"));
            cloud2 = ImageIO.read(new File("src/Elements/cloud3.png")); 
            cloud3 = ImageIO.read(new File("src/Elements/cloud3.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ENEMY_SPEED = -7;
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(gameRunner.getFont());
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(20, 20, 300, 30);
        add(scoreLabel);

        debug = new JLabel("Press B to Toggle Debuger");
        debug.setFont(gameRunner.getFont());
        debug.setForeground(Color.BLACK);
        debug.setBounds(20, 60, 500, 30);
        add(debug);

        player = new Player(100, 475, "src/sun1.png", "src/sun2.png", "src/sun3.png");
        
        enemies = new ArrayList<>();
        enemies.add(new Enemy(1300, 480, "src/student.png", "src/student2.png"));
        enemies.add(new Enemy(1300, 250, "src/bird.png", "src/bird2.png"));
        enemies.add(new Enemy(1300, 450,130, 130, "src/harns.png"));
        enemies.add(new Enemy(1300, 440, 140, 140, "src/harnsF.png"));

        items = new ArrayList<>();
        items.add(new Cat(1300, 490, "src/cat.png"));
        items.add(new Shield(1300, 480, "src/shield.png"));
    }

    public void startGame() {
        if (running) return;
        running = true;
        isGameOver = false;
        score = 0;
        scoreLabel.setText("Score: " + score);
        ENEMY_SPEED = -7;
        removeButtons();
        
        // Reset active flags
        isEnemyActive = false;
        isItemActive = false;
        currentEnemy = null;
        currentItem = null;
        
        // Spawn first object
        spawning();
        
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (isGameOver) {return;}

        updateBackground();
        player.update();
        
        if (!player.isJumping()) {
            player.startWalking();
        } else {
            player.stopWalking();
        }

        if (player.getY() > 500) {
            gameOver();
        }

        if (player.isCat() && catScoreCount >= 5) {
            player.setCat(false);
            catScoreCount = 0;
        }
        // Check if neither enemy nor item is active, spawn something new
        if (!isEnemyActive && !isItemActive) {
            spawning();
        }
        
        // Update enemy if it exists
        // Update enemy if it exists
        if (isEnemyActive && currentEnemy != null) {
            currentEnemy.update(ENEMY_SPEED);
            
            checkCollision();
            
            // We need to check again if currentEnemy is null
            // since checkCollision might have set it to null
            if (currentEnemy != null) {
                // Scoring logic
                if (currentEnemy.getX() < player.getX() - 150 && !currentEnemy.isScored()) {
                    if (player.isCat()) {
                        score += 2;
                        catScoreCount++;
                    }
                    else {score++;}
                    scoreLabel.setText("Score: " + score);
                    if (score % 5 == 0 && ENEMY_SPEED > speedLimit) {
                        System.out.println("Speed got increased");
                        ENEMY_SPEED = Math.max(ENEMY_SPEED - 3, speedLimit);
                    }
                    currentEnemy.setScored(true);
                }
                
                // If enemy moved off screen, deactivate it to spawn something new
                if (currentEnemy.getX() <= -100) {
                    System.out.println("Enemy exited, spawning new object");
                    isEnemyActive = false;
                    currentEnemy = null;
                }
            }
        }
        
        // Update item if it exists
        if (isItemActive && currentItem != null) {
            currentItem.update(ENEMY_SPEED);
            
            // Check for collision with item
            if (player.getBounds().intersects(currentItem.getBounds())) {
                System.out.println("Item collected!");
                if (currentItem instanceof Shield) {
                    player.setShield(true);
                    System.out.println("SHILEDDD!!!");
                }
                else if (currentItem instanceof Cat) {
                    player.setCat(true);
                    System.out.println("CAT!!!");
                }
                isItemActive = false;
                currentItem = null;
            }
            
            // If item moved off screen, deactivate it to spawn something new
            if (currentItem != null && currentItem.getX() <= -100) {
                System.out.println("Item exited, spawning new object");
                isItemActive = false;
                currentItem = null;
            }
        }
    }

    public void spawning() {
        int rand = ThreadLocalRandom.current().nextInt(10);
        if (rand > 1) {
            spawnEnemy();
        } else {
            spawnItem();
        }
    }
    
    private void spawnEnemy() {
        if (isEnemyActive || isItemActive) return; // Don't spawn if something is active
        
        int enemyType = ThreadLocalRandom.current().nextInt(0, enemies.size());
        currentEnemy = enemies.get(enemyType);
        currentEnemy.setX(1300);
        currentEnemy.setScored(false);
        isEnemyActive = true;
        System.out.println("Enemy spawned: " + enemyType);
    }
    
    private void spawnItem() {
        if (isEnemyActive || isItemActive) return; // Don't spawn if something is active
        
        int itemType = ThreadLocalRandom.current().nextInt(0, items.size());
        currentItem = items.get(itemType);
        currentItem.setX(1300);
        isItemActive = true;
        System.out.println("Item spawned: " + itemType);
    }
    public void checkCollision() {
        if (player.isShield()) {
            if (player.getBounds().intersects(currentEnemy.getBounds())) {
                System.out.println("Shield hit!");
                currentEnemy = null;
                isEnemyActive = false;
                score++;
                scoreLabel.setText("Score: " + score);
                player.setShield(false);
            }
        }
        else {
            if (player.getBounds().intersects(currentEnemy.getBounds())) {
                gameOver();
            }
        }
    }
    
    public void gameOver() {
        running = false;
        isGameOver = true;

        retry = new JButton("RETRY");
        retry.setFont(gameRunner.getFont());
        retry.setBounds((GameRunner.SCREEN_WIDTH / 2) - 160, 450, 150, 50);
        retry.addActionListener(e -> gameRunner.restartGame());

        backMenu = new JButton("EXIT");
        backMenu.setFont(gameRunner.getFont());
        backMenu.setBounds((GameRunner.SCREEN_WIDTH / 2) + 10, 450, 180, 50);
        backMenu.addActionListener(e -> gameRunner.showGameMenu());

        add(retry);
        add(backMenu);
        repaint();
    }

    private void removeButtons() {
        if (retry != null) {
            remove(retry);
            retry = null;
        }
        if (backMenu != null) {
            remove(backMenu);
            backMenu = null;
        }
        repaint();
        revalidate();
    }

    private int cloudX1 = 500, cloudX2 = 100, cloudX3 = 1100;
    private int cloudSpeed = -2;

    private void bgMoving(Graphics g) {
        if (cloud1 != null && cloud2 != null && cloud3 != null) {
            g.drawImage(cloud1, cloudX1, -100, 600, 400, this);
            g.drawImage(cloud2, cloudX2, 30, 500, 300, this);
            g.drawImage(cloud3, cloudX3, 70, 400, 220, this);
        }
    }
    
    private void updateBackground() {
        cloudX1 += cloudSpeed;
        cloudX2 += cloudSpeed;
        cloudX3 += cloudSpeed;
    
        if (cloudX1 < -600){cloudX1 = 1280;}
        if (cloudX2 < -450){cloudX2 = 1280;}
        if (cloudX3 < -400){cloudX3 = 1280;}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (floor != null) {g.drawImage(floor, 0, 295, getWidth(), getHeight(), this);}
        bgMoving(g);

        player.paint(g);

        if (currentEnemy != null) {
            currentEnemy.paint(g);
        }
        
        if (currentItem != null) {
            currentItem.paint(g);
        }
        if (player.isShield()) {
            Image shield = new ImageIcon("src/shield.png").getImage();
            g.drawImage(shield, player.getX() + 25, player.getY() - 60, 50, 50, this);
        }
        if (player.isCat()) {
            Image cat = new ImageIcon("src/catFliped.png").getImage();
            g.drawImage(cat, player.getX() -50, player.getY() + 30, 70, 83, this);
        }
        if (isDebug) {
            g.setColor(Color.RED);
            g.drawRect(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

            if (currentEnemy != null) {
                g.setColor(Color.BLUE);
                g.drawRect(currentEnemy.getBounds().x, currentEnemy.getBounds().y, currentEnemy.getBounds().width, currentEnemy.getBounds().height);
            }
            
            if (currentItem != null) {
                g.setColor(Color.GREEN);
                g.drawRect(currentItem.getBounds().x, currentItem.getBounds().y, currentItem.getBounds().width, currentItem.getBounds().height);
            }
            
            g.setColor(Color.BLACK);
            g.setFont(gameRunner.getFont());
            g.drawString("Enemy Speed: " + Math.abs(ENEMY_SPEED), 970, 50);
        }

        if (isGameOver) {
            gameRunner.setHighScore(score);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) return;

        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameRunner.setHighScore(score);
            gameRunner.showGameMenu();
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
            gameOver();
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            isDebug = !isDebug;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}