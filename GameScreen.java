import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GameScreen extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private boolean isGameOver = false;
    private GameRunner gameRunner;

    private Player player;
    private Image cat;

    private JButton retry, backMenu;

    public GameScreen(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setLayout(null); // ต้องกำหนด Layout เป็น null เพื่อใช้ setBounds()

        // สร้าง player object
        player = new Player(100, 375, "src/sun.png");

        // โหลดภาพแมว
        try {
            cat = ImageIO.read(new File("src/cat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        if (player.getY() > 500) {
            gameOver();
        }
    }

    public void gameOver() {
        running = false;
        isGameOver = true;

        retry = new JButton("RETRY");
        retry.setFont(new Font("Arial", Font.BOLD, 30));
        retry.setBounds((GameRunner.SCREEN_WIDTH / 2) - 160, 450, 150, 50);
        retry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
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

    public void restartGame() {
        removeButtons();
        requestFocusInWindow();

        player.resetPosition(); // รีเซ็ตตำแหน่งของ Player

        isGameOver = false;
        running = false;

        addKeyListener(this);
        startGame();
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
        
        // วาดตัวผู้เล่น
        player.paint(g);

        // วาดแมว
        if (cat != null) {
            g.drawImage(cat, player.getX() + 200, player.getY(), 100, 100, this);
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

        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            player.jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameRunner.showGameMenu();
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
            gameOver();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
