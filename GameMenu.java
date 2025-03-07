import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class GameMenu extends JPanel {
    private GameRunner gameRunner;

    private Font PixelifySans = new Font("PixelifySans-Bold", Font.BOLD, 36);

    private JLabel benchLabel, treesLabel, cloud1Label, cloud2Label,cloud3Label, castlesLabel,
            floorLabel, mushroomLabel, starLabel;

    private JLabel titleLabel, titleLabe2;
    private JButton startButton;

    private static int highScore;


    public GameMenu(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.decode("#FAFEFF"));
        setLayout(null);

        // โหลดและเพิ่มรูปภาพ
        try {
            benchLabel = createImageLabel("src/Elements/bench.png", 225, 500, 150, 75);
            treesLabel = createImageLabel("src/Elements/trees.png", 820, 325, 500, 250);
            cloud1Label = createImageLabel("src/Elements/cloud1.png", -50, 50, 500, 250);
            cloud2Label = createImageLabel("src/Elements/cloud2.png", 400, 10, 300, 150);
            cloud3Label = createImageLabel("src/Elements/cloud2.png", 800, 20, 500, 250);
            castlesLabel = createImageLabel("src/Elements/castles.png", -115, 307, 475, 275);
            floorLabel = createImageLabel("src/Elements/floor.png", -10, 285, 1300, 680);
            mushroomLabel = createImageLabel("src/Elements/mushroom.png", 1145, 500, 150, 75);
            starLabel = createImageLabel("src/Elements/star.png", 580, 130, 120, 50);

            add(benchLabel);
            add(treesLabel);
            add(cloud1Label);
            add(cloud2Label);
            add(cloud3Label);
            add(castlesLabel);
            add(floorLabel);
            add(mushroomLabel);
            add(starLabel);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File fontFile = new File("src/font/PixelifySans-Bold.ttf");
            
            PixelifySans = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(36f);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        // เพิ่ม Title
        titleLabel = new JLabel("ARJ SUNTANA RUN", SwingConstants.CENTER);
        titleLabe2 = new JLabel("Are You READY!", SwingConstants.CENTER);

        titleLabel.setFont(PixelifySans);
        titleLabel.setFont(new Font("PixelifySans-Bold", Font.BOLD, 36));
        titleLabel.setBounds((GameRunner.SCREEN_WIDTH - 370) / 2, 200, 400, 45);
        add(titleLabel);
        titleLabe2.setFont(new Font("PixelifySans-Bold", Font.BOLD, 36));
        titleLabe2.setBounds((GameRunner.SCREEN_WIDTH - 370) / 2, 300, 400, 45);
        add(titleLabe2);

        // ปุ่มเริ่มเกม
        startButton = new JButton("RUN!");
        startButton.setFont(new Font("\"Arial\"", Font.BOLD, 24));
        startButton.setBounds((GameRunner.SCREEN_WIDTH - 130) / 2, 400, 150, 75);
        startButton.addActionListener((ActionEvent e) -> gameRunner.showGameScreen());
        add(startButton);
    }

    // ฟังก์ชันสร้าง JLabel พร้อมใส่รูปภาพ
    private JLabel createImageLabel(String filePath, int x, int y, int width, int height) throws IOException {
        ImageIcon icon = new ImageIcon(
                ImageIO.read(new File(filePath)).getScaledInstance(width, height, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        return label;
    }
    @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("High Score: " + highScore, 1060, 50);
        }
        public void setHighScore(int score) {
            if (score > highScore) {
                highScore = score;
            }
        }
}