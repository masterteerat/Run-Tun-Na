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

    private static int ENEMY_SPEED = -7;
        private Enemy currentEnemy = null;
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
    
            try {
                floor = ImageIO.read(new File("src/Elements/floor.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            scoreLabel = new JLabel("Score: " + score);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
            scoreLabel.setForeground(Color.BLACK);
            scoreLabel.setBounds(20, 20, 300, 30);
            add(scoreLabel);
    
            debug = new JLabel("Press B to Toggle Debug");
            debug.setFont(new Font("Arial", Font.BOLD, 20));
            debug.setForeground(Color.BLACK);
            debug.setBounds(20, 60, 300, 30);
            add(debug);
    
            player = new Player(100, 475, "src/sun.png");
        }
    
        public void startGame() {
            if (running) return;
            running = true;
            isGameOver = false;
            removeButtons();
            gameThread = new Thread(this);
            gameThread.start();
            spawnEnemy();
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
    
            player.update();
    
            if (player.getY() > 500) {
                gameOver();
            }
    
            if (currentEnemy != null) {
                currentEnemy.update(ENEMY_SPEED);
    
                if (player.getBounds().intersects(currentEnemy.getBounds())) {
                    gameOver();
                }
    
                if (currentEnemy.getX() <= -100) {
                    currentEnemy = null;
                }
    
                if (currentEnemy != null && currentEnemy.getX() < player.getX() && !currentEnemy.isScored()) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    if (score % 5 == 0 && ENEMY_SPEED > -25) {
                        ENEMY_SPEED = Math.max(ENEMY_SPEED - 3, -25);
                }
                currentEnemy.setScored(true);
            }
        } else {
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        int enemyType = random.nextInt(4);
        switch (enemyType) {
            case 0:
                currentEnemy = new Enemy(1300, 480, "src/student.png");
                break;
            case 1:
                currentEnemy = new Enemy(1300, 200, "src/bird.png");
                break;
            case 2:
                currentEnemy = new Enemy(1300, 450,130, 130, "src/harns.png");
                break;
            default:
                currentEnemy = new Enemy(1300, 440, 140, 140, "src/harnsF.png");
                break;
        }
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
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Enemy Speed: " + ENEMY_SPEED, 1000, 50);
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
