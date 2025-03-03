import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JPanel {
    private GameRunner gameRunner; // Reference ไปยัง GameRunner

    public GameMenu(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
        setPreferredSize(new Dimension(GameRunner.SCREEN_WIDTH, GameRunner.SCREEN_HEIGHT));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        JLabel titleLabel = new JLabel("ARJ RUN!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds((GameRunner.SCREEN_WIDTH - 370)/2,(GameRunner.SCREEN_HEIGHT - 45)/2,370,45);

        JButton startButton = new JButton("RUN!");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBounds((GameRunner.SCREEN_WIDTH - 78)/2, 450, 70, 45);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunner.showGameScreen(); // เปลี่ยนเป็นหน้าจอเกม
            }
        });

        // เพิ่มองค์ประกอบ
        add(titleLabel);
        add(startButton);
    }
}