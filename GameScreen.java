import javax.swing.*;

import Enemies.AbsEnemy;
import Enemies.Cat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private boolean isGameOver = false;
    private GameRunner gameRunner;
    
    private JButton retry, backMenu;
    public JLabel scoreLabel;

    private int score = 0;
    private ArrayList<AbsEnemy> enemies;

    private Player player;
    private Cat cat;

    private Image floor; 
    
    private JLabel debug;
    private boolean isDebug = false;

    public GameScreen(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setLayout(null); // ต้องกำหนด Layout เป็น null เพื่อใช้ setBounds()
        enemies = new ArrayList<>();

        // สร้าง player object
        player = new Player(100, 475, "src/sun.png");

        //สร้าง แมว
        cat = new Cat(1300, 490, -7, "src/cat.png" );
        enemies.add(cat);

        // โหลดและเพิ่มรูปภาพ

        try {
            floor = ImageIO.read(new File("src/Elements/floor.png")); 
        } catch (IOException e) {
            e.printStackTrace();
        }

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(20, 20, 300,30);
        add(scoreLabel);

        debug = new JLabel("Press B to Toggle Debug");
        debug.setFont(new Font("Arial", Font.BOLD, 20));
        debug.setForeground(Color.BLACK);
        debug.setBounds(20, 60, 300,30);
        add(debug);
    }

    public void startGame() {
        if (running) return;
        running = true;
        isGameOver = false;
        removeButtons();
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
        if (isGameOver) return;

        player.update(); // อัพเดทสถานะของ Player
        cat.update();

        if (player.getY() > 500) {
            gameOver();
        }
        if (player.getBounds().intersects(cat.getBounds())) {
            gameOver();
        }
        for (AbsEnemy enemy : enemies) {  // ลูปผ่านทุกตัวใน List
            if (enemy.getX() < player.getX()) {  // เพิ่ม Speed เมื่อข้าม Enemy
                if (!enemy.isScored()) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    if (enemy.getSpeed() > -25 && score % 5 == 0) {
                        enemy.setSpeed(Math.max(enemy.getSpeed() - 3, -25));
                        System.out.println(enemy.getSpeed());
                    }
                    enemy.setScored(true);
                }
            }
        }
    }

    public void gameOver() {
        running = false;
        isGameOver = true;

        // setHighScore(score);

        retry = new JButton("RETRY");
        retry.setFont(new Font("Arial", Font.BOLD, 30));
        retry.setBounds((GameRunner.SCREEN_WIDTH / 2) - 160, 450, 150, 50);
        retry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunner.restartGame();
            }
        });

        backMenu = new JButton("EXIT");
        backMenu.setFont(new Font("Arial", Font.BOLD, 20));
        backMenu.setBounds((GameRunner.SCREEN_WIDTH / 2) + 10, 450, 180, 50);
        backMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunner.showGameMenu();
            }
        });

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
        // วาดตัวผู้เล่น
        player.paint(g);
        // วาดแมว
        cat.paint(g);

        if (isDebug) {
            g.setColor(Color.RED);
            g.drawRect(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);
        
            g.setColor(Color.BLUE);
            g.drawRect(cat.getBounds().x, cat.getBounds().y, cat.getBounds().width, cat.getBounds().height);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Cat Speed: " + cat.getSpeed(), 1060, 50);

        }
        // แสดงข้อความ GAME OVER
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
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
