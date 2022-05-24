import view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame=new JFrame("ChessGame");
        jFrame.setSize(718   ,447);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setLayout(null);

        JButton start=new JButton("Start Playing!");
        start.setLocation(250,150);
        start.setSize(200,100);
        start.setFont(new Font("Rockwell",Font.BOLD,20));

        jFrame.add(start);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel background=new JLabel();
        ImageIcon icon = new ImageIcon("D:\\CS102A\\ChessProject\\images\\Picture.jpg");
        background.setIcon(icon);
        background.setBounds(0 ,0, 700, 400);
//        background.setLayout(null);
        jFrame.add(background);

        start.addActionListener(e -> {
//            SwingUtilities.invokeLater(() -> {
                ChessGameFrame mainFrame = new ChessGameFrame(1000, 760);
                mainFrame.setVisible(true);
//            });
            jFrame.dispose();
        });
    }
}
