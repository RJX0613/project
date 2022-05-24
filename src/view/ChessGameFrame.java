package view;

import controller.GameController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
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
    public String[] backgroundPicture={"D:\\CS102A\\ChessProject\\images\\GBP.jpg","D:\\CS102A\\ChessProject\\images\\Background.jpg","D:\\CS102A\\ChessProject\\images\\Background02.jpg","D:\\CS102A\\ChessProject\\images\\Background03.jpg"};
    public String[] boardPicture={"D:\\CS102A\\ChessProject\\images\\Board1.jpg","D:\\CS102A\\ChessProject\\images\\Board02.jpg","D:\\CS102A\\ChessProject\\images\\Board03.jpg"};
    public String bg=backgroundPicture[0];
    public String cb=boardPicture[0];

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


        addLabel2();
        addChessboard();
        addLabel();
//        addHelloButton();
        addReview();
        addSwitch();
        addReStartButton();
        addLoadButton();
        addPlayBGMButton();
        addUndo();
        addSaveButton();
        addTimer();
        addChessBoardPicture();
        addPicture();

    }

    public void Switch(){
        Random r=new Random();
        int r1=r.nextInt(4);
        int r2=r.nextInt(3);
        bg=backgroundPicture[r1];
        cb=boardPicture[r2];
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

    private  void addLabel2(){
        JLabel label2 = new JLabel("CURRENT PLAYER:");
        label2.setLocation(HEIGTH-10, HEIGTH / 10+10);
        label2.setSize(250, 60);
        label2.setFont(new Font("宋体", Font.BOLD, 25));
        label2.setForeground(Color.CYAN);
        add(label2);
    }


    private void addLabel() {
        statusLabel = new JLabel("White");
        statusLabel.setLocation(HEIGTH+40, HEIGTH / 10+50);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 40));
        statusLabel.setForeground(Color.CYAN);
        add(statusLabel);
    }


    private void addTimer() {
        timerTask.timeLabel.setLocation(HEIGTH, HEIGTH / 15 - 20);
        timerTask.timeLabel.setSize(WIDTH / 15, HEIGTH / 15);
        timerTask.timeLabel.setFont(new Font("Rockwell", Font.BOLD, HEIGTH / 25));
        timerTask.timeLabel.setForeground(Color.GREEN);
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

    private void addReStartButton() {
        JButton button = new JButton("RESTART");
        button.setLocation(280, 10);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.ITALIC, 30));
        button.setForeground(Color.PINK);
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click restart");
            gameController.restart();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("LOAD");
        button.setLocation(HEIGTH, HEIGTH / 10 + 205);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.ITALIC, 30));
        button.setForeground(Color.PINK);
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
        JButton button = new JButton("PLAY MUSIC");
        button.setLocation(HEIGTH, HEIGTH / 10 + 355);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.ITALIC, 28));
        button.setForeground(Color.PINK);
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

    private void addReview(){
        JButton button = new JButton("REVIEW");
        button.setLocation(HEIGTH, HEIGTH / 10 + 430);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.ITALIC, 30));
        button.setForeground(Color.PINK);
        add(button);

        button.addActionListener(e -> {
            gameController.getChessboard().initChessboard();
            gameController.getChessboard().setCurrentColor(ChessColor.WHITE);
            gameController.getChessboard().repaint();
            timeTask timeTask=new timeTask();
            timeTask.timer1.schedule(timeTask,0,500);
            if(statusLabel.getText().equals("White")){
                gameController.getChessboard().setCurrentColor(ChessColor.WHITE);
            }else{
                gameController.getChessboard().setCurrentColor(ChessColor.BLACK);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }

    JLabel background = new JLabel();
    private void addPicture() {
        ImageIcon icon = new ImageIcon(bg);
        background.setIcon(icon);
        background.setBounds(0, 0, WIDTH, HEIGTH);
        add(background);
    }

    JLabel board = new JLabel();
    private void addChessBoardPicture() {
        ImageIcon icon = new ImageIcon(cb);
        board.setIcon(icon);
        board.setBounds(HEIGTH / 10, HEIGTH / 10, 608, 608);
        add(board);
    }

    private void addUndo() {
        JButton button = new JButton("UNDO");
        button.setLocation(HEIGTH, HEIGTH / 10 + 280);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.ITALIC,30));
        button.setForeground(Color.PINK);
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click undo");
            gameController.undo();
        });
    }

    private void addSwitch() {
        JButton button = new JButton("SWITCH");
        button.setLocation(HEIGTH, HEIGTH / 10 + 505);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.ITALIC, 30));
        button.setForeground(Color.PINK);
        add(button);
        button.addActionListener(e -> {
            Switch();
            background.setIcon(new ImageIcon(bg));
            board.setIcon(new ImageIcon(cb));
            System.out.println("Switch");
        });
    }

    private void addSaveButton() {
        JButton button = new JButton("SAVE");
        button.setLocation(HEIGTH, HEIGTH / 10 + 130);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 30));
        button.setForeground(Color.PINK);
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
        Label timeLabel = new Label("32s");
        public void resetTime() {
            time = 31;
        }
        @Override
        public void run() {
            time--;
            if (time>=16){
                timeLabel.setForeground(Color.GREEN);

            }
            if (time == 15) {
                timeLabel.setForeground(Color.orange);
            }
            if (time==10){
                timeLabel.setForeground(Color.red);
            }
            if (time == 0) {
                resetTime();
                timeLabel.setForeground(Color.GREEN);
                gameController.getChessboard().swapColor();
            }

            timeLabel.setText(String.format("%ds", this.time));
            timeLabel.repaint();
        }

    }

    public static class timeTask extends java.util.TimerTask{
        int time1=-4;
        Timer timer1=new Timer();

        @Override
        public void run() {
            time1++;
            if(time1>=gameController.getChessboard().step.size()){
                timer1.cancel();
            }else if(time1>=0){
               // gameController.getChessboard().move(gameController.getChessboard().getChessComponents()[gameController.getChessboard().step.get(time1).charAt(0)-48][gameController.getChessboard().step.get(time1).charAt(1)-48],gameController.getChessboard().getChessComponents()[gameController.getChessboard().step.get(time1).charAt(2)-48][gameController.getChessboard().step.get(time1).charAt(3)-48]);
                ChessComponent chess1 = gameController.getChessboard().getChessComponents()[gameController.getChessboard().step.get(time1).charAt(0) - 48][gameController.getChessboard().step.get(time1).charAt(1) - 48];
                ChessComponent chess2 = gameController.getChessboard().getChessComponents()[gameController.getChessboard().step.get(time1).charAt(2) - 48][gameController.getChessboard().step.get(time1).charAt(3) - 48];
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'B') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'b') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'N') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'n') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'Q') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'q') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'R') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                if (gameController.getChessboard().step.get(time1).charAt(4) == 'r') {
                    gameController.getChessboard().remove(chess1);
                    chess1 = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, gameController.getChessboard().getClickController(), 76);
                    gameController.getChessboard().add(chess1);
                }
                chess1.repaint();
                chess2.repaint();
                gameController.getChessboard().move(chess1, chess2);
            }
        }
    }
}
