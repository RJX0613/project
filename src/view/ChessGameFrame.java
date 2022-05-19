package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    public static GameController gameController;
    private JLabel statusLabel;
    public String BGMState = "OFF";
    private String path;
    TimerTask timerTask = new TimerTask();

    public TimerTask getTimerTask() {
        return timerTask;
    }

    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();
        addTimer();
        addLabel();
//        addHelloButton();
        addReStartButton();
        addLoadButton();
        addPlayBGMButton();
        addUndo();
        addSaveButton();
        addChessBoardPicture();
        addPicture();
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setGameController(gameController);
        gameController.setChessGameFrame(this);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        statusLabel = new JLabel("White");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 40));
        add(statusLabel);
    }


    private void addTimer() {
        timerTask.timeLabel.setLocation(HEIGTH, HEIGTH / 15 - 20);
        timerTask.timeLabel.setSize(WIDTH / 18, HEIGTH / 17);
        timerTask.timeLabel.setFont(new Font("Rockwell", Font.BOLD, HEIGTH / 39));
        add(timerTask.timeLabel);
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changePlayer(String str) {
        statusLabel.setText(str);
        statusLabel.repaint();
    }

    public void changeBGMState() {
        if (BGMState.equals("ON")) {
            BGMState = "OFF";
        } else {
            BGMState = "ON";
        }
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

//    private void addHelloButton() {
//        JButton button = new JButton("Show Hello Here");
//        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
//        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//    }
    private void addReStartButton() {
        JButton button = new JButton("RESTART");
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click restart");
            gameController.restart();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Load");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setMultiSelectionEnabled(false);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                path = file.getAbsolutePath();
                String[] part = path.split("\\.");
                if (part[part.length - 1].equals("txt")) {
                    try {
                        List<String> chessData = Files.readAllLines(Paths.get(path));
                        for (int i = 0; i < 8; i++) {
                            if (chessData.get(i).length() != 8) {
                                JOptionPane.showConfirmDialog(null, "错误代码:101", "提示", JOptionPane.DEFAULT_OPTION);
                                return;
                            } else {
                                for (int j = 0; j < 8; j++) {
                                    char chess = chessData.get(i).charAt(j);
                                    if (chess != 'B' && chess != 'N' && chess != 'P' && chess != 'R' && chess != 'Q' && chess != 'K' && chess != '_' && chess != 'b' && chess != 'n' && chess != 'p' && chess != 'r' && chess != 'q' && chess != 'k') {
                                        JOptionPane.showConfirmDialog(null, "错误代码:102", "提示", JOptionPane.DEFAULT_OPTION);
                                        return;
                                    }
                                }
                            }
                        }
                        if (!chessData.get(8).equals("w") && !chessData.get(8).equals("b")) {
                            JOptionPane.showConfirmDialog(null, "错误代码:103", "提示", JOptionPane.DEFAULT_OPTION);
                            return;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    gameController.loadGameFromFile(path);
                } else {
                    JOptionPane.showConfirmDialog(null, "错误代码:104", "提示", JOptionPane.DEFAULT_OPTION);
                }
            }
            if (result == JFileChooser.CANCEL_OPTION) {
                fileChooser.setVisible(false);
            }
        });
    }

    private void addPlayBGMButton() {
        JButton button = new JButton("Play");
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(100, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(button);

        button.addActionListener(e -> {
            if (BGMState.equals("OFF")) {
                gameController.playBGM();
                System.out.printf("BGM: %s\n", BGMState);
            } else if (BGMState.equals("ON")) {
                gameController.stop();
                System.out.printf("BGM: %s\n", BGMState);
            }
        });
    }

    private void addPicture() {
        JLabel background = new JLabel();
        ImageIcon icon = new ImageIcon("D:\\CS102A\\ChessProject\\images\\GBP.jpg");
        background.setIcon(icon);
        background.setBounds(0, 0, WIDTH, HEIGTH);
        add(background);
    }

    private void addChessBoardPicture() {
        JLabel background = new JLabel();
        ImageIcon icon = new ImageIcon("D:\\CS102A\\ChessProject\\images\\Board1.jpg");
        background.setIcon(icon);
        background.setBounds(HEIGTH / 10, HEIGTH / 10, 608, 608);
        add(background);
    }

    private void addUndo() {
        JButton button = new JButton("Undo");
        button.setLocation(HEIGTH + 100, HEIGTH / 10 + 360);
        button.setSize(100, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click undo");
            gameController.undo();
        });
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Save");
            try {
                gameController.saveGame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static class TimerTask extends java.util.TimerTask {
        int time = 32;
        Label timeLabel = new Label("33s");

        public void resetTime() {
            time = 31;
        }

        @Override
        public void run() {
            time--;
            if (time == 0) {
                resetTime();
                gameController.getChessboard().swapColor();
            }
            timeLabel.setText(String.format("%ds", this.time));
            timeLabel.repaint();
        }
    }


//    private void showFileSaveDialog(Component component, JTextArea jTextArea) {
//        JFileChooser fileChooser = new JFileChooser();
//
//        fileChooser.setSelectedFile(new File("save.txt"));
//
//        int result = fileChooser.showSaveDialog(component);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            jTextArea.append("保存到文件：" + file.getAbsolutePath() + "\n\n");
//        }
//    }

//    private void showFileOpenDialog(Component component) {
//        JFileChooser fileChooser = new JFileChooser();
//
//        fileChooser.setCurrentDirectory(new File("."));
//
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//        fileChooser.setMultiSelectionEnabled(false);
//
////        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
////        fileChooser.setFileFilter(new FileNameExtensionFilter("text(*.txt)", "txt"));
//        int result = fileChooser.showOpenDialog(component);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
////            jTextArea.append("打开文件：" + file.getAbsolutePath() + "\n\n");
//            path = file.getAbsolutePath();
//        }
//    }

//    private void openFile() {
//        final JFrame jFrame = new JFrame("加载存档");
//        jFrame.setSize(400, 250);
//        jFrame.setLocationRelativeTo(null);
//        jFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        final JTextArea msgTextArea = new JTextArea(10, 30);
//        msgTextArea.setLineWrap(true);
//        panel.add(msgTextArea);
//
//        JButton openBtn = new JButton("打开");
//        JButton sure = new JButton("确认");
//        openBtn.addActionListener(e -> showFileOpenDialog(jFrame));
//        sure.addActionListener(e -> {
//            gameController.loadGameFromFile(path);
//            jFrame.setVisible(false);
//        });
//        panel.add(openBtn);
//        panel.add(sure);
//
//        jFrame.setContentPane(panel);
//        jFrame.setVisible(true);
//    }

//    private void saveFile(){
//        final JFrame jFrame=new JFrame("保存存档");
//        jFrame.setSize(400,250);
//        jFrame.setLocationRelativeTo(null);
//        jFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//
//        JPanel panel=new JPanel();
//        final JTextArea msgTextArea=new JTextArea(10,30);
//        msgTextArea.setLineWrap(true);
//        panel.add(msgTextArea);
//
//        JButton openBtn=new JButton("打开");
//        JButton sure=new JButton("确认");
//        openBtn.addActionListener(e->showFileSaveDialog(jFrame,msgTextArea));
//        sure.addActionListener(e->{
//            try {
//                gameController.saveGame();
//                sure.setVisible(false);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        panel.add(openBtn);
//        panel.add(sure);
//
//        jFrame.setContentPane(panel);
//        jFrame.setVisible(true);
//    }
}
