package controller;

import model.*;
import view.ChessGameFrame;
import view.Chessboard;

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
            File musicPath = new File("D:\\CS102A\\Project1\\BGM.wav");
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
//            chessboard.loadGame(chessData);
            restart();
            for(int i=0;i<chessData.size()-9;i++){
                chessboard.swapChessComponents(getChessboard().getChessComponents()[chessData.get(i+9).charAt(0)-48][chessData.get(i+9).charAt(1)-48],getChessboard().getChessComponents()[chessData.get(i+9).charAt(2)-48][chessData.get(i+9).charAt(3)-48]);
                chessboard.swapColor();
            }
            if(chessData.get(8).equals("b")){
                chessboard.setCurrentColor(ChessColor.BLACK);
                chessGameFrame.changePlayer("Black");
            }else if(chessData.get(8).equals("w")){
                chessboard.setCurrentColor(ChessColor.WHITE);
                chessGameFrame.changePlayer("White");
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
        getChessboard().step=new ArrayList<>();
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
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("save.txt"));
        int result=fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getAbsolutePath();
            BufferedWriter writer=new BufferedWriter(new FileWriter(path));
            ChessComponent[][] board=getChessboard().getChessComponents();
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(board[i][j] instanceof BishopChessComponent){
                        if(board[i][j].getChessColor()==ChessColor.WHITE){
                            writer.write("b");
                        }else{
                            writer.write("B");
                        }
                    }
                    if(board[i][j] instanceof KingChessComponent){
                        if(board[i][j].getChessColor()==ChessColor.WHITE){
                            writer.write("k");
                        }else{
                            writer.write("K");
                        }
                    }
                    if(board[i][j] instanceof KnightChessComponent){
                        if(board[i][j].getChessColor()==ChessColor.WHITE){
                            writer.write("n");
                        }else{
                            writer.write("N");
                        }
                    }
                    if(board[i][j] instanceof PawnChessComponentBlack){
                        writer.write("P");
                    }
                    if(board[i][j] instanceof PawnChessComponentWhite){
                        writer.write("p");
                    }
                    if(board[i][j] instanceof QueenChessComponent){
                        if(board[i][j].getChessColor()==ChessColor.WHITE){
                            writer.write("q");
                        }else{
                            writer.write("Q");
                        }
                    }
                    if(board[i][j] instanceof RookChessComponent){
                        if(board[i][j].getChessColor()==ChessColor.WHITE){
                            writer.write("r");
                        }else{
                            writer.write("R");
                        }
                    }
                    if(board[i][j] instanceof EmptySlotComponent){
                        writer.write("_");
                    }
                }
                writer.write("\n");
            }
            if(chessboard.getCurrentColor()==ChessColor.WHITE){
                writer.write("w\n");
            }else{
                writer.write("b\n");
            }
            for(int i=0;i<getChessboard().step.size();i++){
                writer.write(String.format("%s\n",getChessboard().step.get(i)));
            }
            writer.close();
        }
        if(result==JFileChooser.CANCEL_OPTION){
            fileChooser.setVisible(false);
        }

    }

    public void undo(){
        int size=getChessboard().step.size();
        if(size>0){
            getChessboard().step.remove(getChessboard().step.get(size-1));
        }
        chessboard.initChessboard();
        chessboard.setCurrentColor(ChessColor.WHITE);
        chessGameFrame.changePlayer(chessboard.getCurrentColor().getName());
        chessboard.repaint();
        for(int i=0;i<getChessboard().step.size();i++){
            chessboard.move(getChessboard().getChessComponents()[getChessboard().step.get(i).charAt(0)-48][getChessboard().step.get(i).charAt(1)-48],getChessboard().getChessComponents()[getChessboard().step.get(i).charAt(2)-48][getChessboard().step.get(i).charAt(3)-48]);
            chessboard.swapColor();
        }
        chessGameFrame.getTimerTask().resetTime();
    }

}
