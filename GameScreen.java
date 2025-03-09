import javax.swing.*;
import Enemies.*;
import Item.*;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private boolean isGameOver = false;
    private GameRunner gameRunner;

    private JButton retry, backMenu;
    public JLabel scoreLabel;
    private int score = 0;

    private ArrayList<Enemy> enemyPool = new ArrayList<>();
    private Enemy currentEnemy = null; // ศัตรูตัวเดียวในจอ
    private Player player;

    private Image floor;
    private JLabel debug;
    private boolean isDebug = false;

    private Random random = new Random();

    public GameScreen(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.decode("#E8FEFF"));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setLayout(null);

        // สร้าง Player
        player = new Player(100, 475, "src/sun.png");

        // โหลดพื้นหลัง
        try {
            floor = ImageIO.read(new File("src/Elements/floor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // สร้าง Score Label
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(20, 20, 300, 30);
        add(scoreLabel);

        // สร้าง Debug Label
        debug = new JLabel("Press B to Toggle Debug");
        debug.setFont(new Font("Arial", Font.BOLD, 20));
        debug.setForeground(Color.BLACK);
        debug.setBounds(20, 60, 300, 30);
        add(debug);
    }

    public void startGame() {
        if (running) return;
        running = true;
        isGameOver = false;
        removeButtons();
        gameThread = new Thread(this);
        gameThread.start();
        spawnEnemy(); // เริ่มเกมโดยให้ศัตรูตัวแรกเกิดขึ้น
    }

    @Override
    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(16); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (isGameOver) return;
    
        player.update();
    
        if (player.getY() > 500) {
            gameOver();
        }
    
        // อัปเดตศัตรู
        if (currentEnemy != null) {
            currentEnemy.update();
    
            // ถ้าผู้เล่นชนศัตรู -> Game Over
            if (player.getBounds().intersects(currentEnemy.getBounds())) {
                gameOver();
            }
    
            // ถ้าศัตรูออกจากหน้าจอ ให้ลบแล้วสุ่มใหม่
            if (currentEnemy.getX() <= -100) {
                System.out.println("HELLO");
                currentEnemy = null; // ล้างค่าศัตรู
                spawnEnemy(); // สุ่มศัตรูใหม่
            }
    
            // ถ้าผู้เล่นข้ามศัตรูได้ ให้เพิ่มคะแนน
            if (currentEnemy != null && currentEnemy.getX() < player.getX() && !currentEnemy.isScored()) {
                score++;
                scoreLabel.setText("Score: " + score);
                if (currentEnemy.getSpeed() > -25 && score % 5 == 0) {
                    currentEnemy.setSpeed(Math.max(currentEnemy.getSpeed() - 3, -25));
                }
                currentEnemy.setScored(true);
            }
        } else {
            spawnEnemy(); // ถ้าไม่มีศัตรู ให้สร้างใหม่
        }
    }
    
    
    private void spawnEnemy() {
        // สุ่มศัตรูใหม่ทุกครั้งที่เกิด
        int enemyType = random.nextInt(2); // สุ่มระหว่าง 0 หรือ 1
        System.out.println("Spawn enemy type: " + enemyType); // Debug เช็คว่ามันสุ่มจริง
    
        if (enemyType == 0) {
            currentEnemy = new Enemy(1300, 490, -7, "src/student.png");
        } else {
            currentEnemy = new Enemy(1300, 200, -7, "src/bird.png");
        }
    
        currentEnemy.setScored(false);
    }
    
    public void gameOver() {
        running = false;
        isGameOver = true;

        retry = new JButton("RETRY");
        retry.setFont(new Font("Arial", Font.BOLD, 30));
        retry.setBounds((GameRunner.SCREEN_WIDTH / 2) - 160, 450, 150, 50);
        retry.addActionListener(e -> gameRunner.restartGame());

        backMenu = new JButton("EXIT");
        backMenu.setFont(new Font("Arial", Font.BOLD, 20));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (floor != null) {
            g.drawImage(floor, 0, 295, getWidth(), getHeight(), this);
        }

        player.paint(g);

        if (currentEnemy != null) {
            currentEnemy.paint(g);
        }

        if (isDebug) {
            g.setColor(Color.RED);
            g.drawRect(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

            if (currentEnemy != null) {
                g.setColor(Color.BLUE);
                g.drawRect(currentEnemy.getBounds().x, currentEnemy.getBounds().y, currentEnemy.getBounds().width, currentEnemy.getBounds().height);
            }
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
