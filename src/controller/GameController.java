package controller;

import model.*;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameController extends Component {
    private Chessboard chessboard;
    public ChessGameFrame chessGameFrame;
    private Clip bgm;

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public GameController(Chessboard chessboard) {
        try {
            File musicPath = new File("D:\\CS102A\\ChessProject\\BGM.wav");
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                bgm = AudioSystem.getClip();
                bgm.open(audioInput);
                FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6.0f);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        this.chessboard = chessboard;
    }

    public void loadGameFromFile(String path) {
        try {
            List<String> chessData = Files.readAllLines(Paths.get(path));
            restart();
            for (int i = 0; i < chessData.size() - 9; i++) {
                ChessComponent chess1 = getChessboard().getChessComponents()[chessData.get(i + 9).charAt(0) - 48][chessData.get(i + 9).charAt(1) - 48];
                ChessComponent chess2 = getChessboard().getChessComponents()[chessData.get(i + 9).charAt(2) - 48][chessData.get(i + 9).charAt(3) - 48];
                if (chessData.get(i + 9).charAt(4) == 'B') {
                    chessboard.remove(chess1);
                    chess1 = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'b') {
                    chessboard.remove(chess1);
                    chess1 = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'N') {
                    chessboard.remove(chess1);
                    chess1 = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'n') {
                    chessboard.remove(chess1);
                    chess1 = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'Q') {
                    chessboard.remove(chess1);
                    chess1 = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'q') {
                    chessboard.remove(chess1);
                    chess1 = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'R') {
                    chessboard.remove(chess1);
                    chess1 = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                if (chessData.get(i + 9).charAt(4) == 'r') {
                    chessboard.remove(chess1);
                    chess1 = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                    chessboard.add(chess1);
                }
                chess1.repaint();
                chess2.repaint();
                chessboard.move(chess1, chess2);
                chessboard.swapColor();
            }
            if (chessData.get(8).equals("b")) {
                chessboard.setCurrentColor(ChessColor.BLACK);
                chessGameFrame.changePlayer("Black");
            } else if (chessData.get(8).equals("w")) {
                chessboard.setCurrentColor(ChessColor.WHITE);
                chessGameFrame.changePlayer("White");
            }
            for(int i=0;i<chessData.size()-9;i++){
                chessboard.step.add(chessData.get(i+9));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        chessboard.initChessboard();
        chessboard.setCurrentColor(ChessColor.WHITE);
        chessGameFrame.changePlayer(chessboard.getCurrentColor().getName());
        chessboard.repaint();
        getChessboard().step = new ArrayList<>();
        chessGameFrame.getTimerTask().resetTime();
    }

    public void playBGM() {
        if (bgm != null) {
            bgm.start();
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
            chessGameFrame.changeBGMState();
        }
    }

    public void stop() {
        bgm.stop();
        chessGameFrame.changeBGMState();
    }

    public void saveGame() throws IOException {
        String path;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("save.txt"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getAbsolutePath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            ChessComponent[][] board = getChessboard().getChessComponents();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] instanceof BishopChessComponent) {
                        if (board[i][j].getChessColor() == ChessColor.WHITE) {
                            writer.write("b");
                        } else {
                            writer.write("B");
                        }
                    }
                    if (board[i][j] instanceof KingChessComponent) {
                        if (board[i][j].getChessColor() == ChessColor.WHITE) {
                            writer.write("k");
                        } else {
                            writer.write("K");
                        }
                    }
                    if (board[i][j] instanceof KnightChessComponent) {
                        if (board[i][j].getChessColor() == ChessColor.WHITE) {
                            writer.write("n");
                        } else {
                            writer.write("N");
                        }
                    }
                    if (board[i][j] instanceof PawnChessComponentBlack) {
                        writer.write("P");
                    }
                    if (board[i][j] instanceof PawnChessComponentWhite) {
                        writer.write("p");
                    }
                    if (board[i][j] instanceof QueenChessComponent) {
                        if (board[i][j].getChessColor() == ChessColor.WHITE) {
                            writer.write("q");
                        } else {
                            writer.write("Q");
                        }
                    }
                    if (board[i][j] instanceof RookChessComponent) {
                        if (board[i][j].getChessColor() == ChessColor.WHITE) {
                            writer.write("r");
                        } else {
                            writer.write("R");
                        }
                    }
                    if (board[i][j] instanceof EmptySlotComponent) {
                        writer.write("_");
                    }
                }
                writer.write("\n");
            }
            if (chessboard.getCurrentColor() == ChessColor.WHITE) {
                writer.write("w\n");
            } else {
                writer.write("b\n");
            }
            for (int i = 0; i < getChessboard().step.size(); i++) {
                writer.write(String.format("%s\n", getChessboard().step.get(i)));
            }
            writer.close();
        }
        if (result == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        }

    }

    public void undo() {
        int size = getChessboard().step.size();
        if (size > 0) {
            getChessboard().step.remove(getChessboard().step.get(size - 1));
        }
        chessboard.initChessboard();
        chessboard.setCurrentColor(ChessColor.WHITE);
        chessGameFrame.changePlayer(chessboard.getCurrentColor().getName());
        chessboard.repaint();

        for (int i = 0; i < getChessboard().step.size(); i++) {
            ChessComponent chess1 = getChessboard().getChessComponents()[getChessboard().step.get(i).charAt(0) - 48][getChessboard().step.get(i).charAt(1) - 48];
            ChessComponent chess2 = getChessboard().getChessComponents()[getChessboard().step.get(i).charAt(2) - 48][getChessboard().step.get(i).charAt(3) - 48];
            if (getChessboard().step.get(i).charAt(4) == 'B') {
                chessboard.remove(chess1);
                chess1 = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'b') {
                chessboard.remove(chess1);
                chess1 = new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'N') {
                chessboard.remove(chess1);
                chess1 = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'n') {
                chessboard.remove(chess1);
                chess1 = new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'Q') {
                chessboard.remove(chess1);
                chess1 = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'q') {
                chessboard.remove(chess1);
                chess1 = new QueenChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'R') {
                chessboard.remove(chess1);
                chess1 = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            if (getChessboard().step.get(i).charAt(4) == 'r') {
                chessboard.remove(chess1);
                chess1 = new RookChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, chessboard.getClickController(), 76);
                chessboard.add(chess1);
            }
            chess1.repaint();
            chess2.repaint();
            chessboard.move(chess1, chess2);
            chessboard.swapColor();
        }
        chessGameFrame.getTimerTask().resetTime();
    }

}
