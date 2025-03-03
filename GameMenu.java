import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class GameMenu extends JPanel {
    private GameRunner gameRunner;
    
    private JLabel benchLabel, treesLabel, cloud1Label, cloud2Label, castlesLabel, 
                   floorLabel, mushroomLabel, starLabel;

    public GameMenu(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null); 

        // โหลดและเพิ่มรูปภาพ
        try {
            benchLabel = createImageLabel("src/Elements/bench.png", 100, 300, 100, 50);
            treesLabel = createImageLabel("src/Elements/trees.png", 200, 250, 150, 100);
            cloud1Label = createImageLabel("src/Elements/cloud1.png", 50, 50, 120, 60);
            cloud2Label = createImageLabel("src/Elements/cloud2.png", 400, 80, 120, 60);
            castlesLabel = createImageLabel("src/Elements/castles.png", 500, 200, 200, 150);
            floorLabel = createImageLabel("src/Elements/floor.png", 0, 400, GameRunner.SCREEN_WIDTH, 100);
            mushroomLabel = createImageLabel("src/Elements/mushroom.png", 350, 350, 50, 50);
            starLabel = createImageLabel("src/Elements/star.png", 600, 100, 40, 40);

            add(benchLabel);
            add(treesLabel);
            add(cloud1Label);
            add(cloud2Label);
            add(castlesLabel);
            add(floorLabel);
            add(mushroomLabel);
            add(starLabel);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // เพิ่ม Title
        JLabel titleLabel = new JLabel("ARJ RUN!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds((GameRunner.SCREEN_WIDTH - 370) / 2, 50, 370, 45);
        add(titleLabel);

        // ปุ่มเริ่มเกม
        JButton startButton = new JButton("RUN!");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBounds((GameRunner.SCREEN_WIDTH - 78) / 2, 450, 100, 45);
        startButton.addActionListener((ActionEvent e) -> gameRunner.showGameScreen());
        add(startButton);
    }

    // ฟังก์ชันสร้าง JLabel พร้อมใส่รูปภาพ
    private JLabel createImageLabel(String filePath, int x, int y, int width, int height) throws IOException {
        ImageIcon icon = new ImageIcon(ImageIO.read(new File(filePath)).getScaledInstance(width, height, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        return label;
    }
}
