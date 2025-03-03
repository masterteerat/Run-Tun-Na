package SUNTANARUN;

import javax.swing.JFrame;
import java.awt.Color;

public class GameRunner extends JFrame {
    public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;

    public GameRunner() {
        setTitle("Suntana");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setVisible(true);
    }
    public static void main(String[] args) {
        new GameRunner();
    }
}
