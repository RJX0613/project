package controller;

import model.ChessComponent;
import view.Chessboard;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    private Clip click;

    public void entered(ChessComponent chessComponent) {
        chessComponent.setEntered(true);
        this.chessboard.repaint();
    }
    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                click();
                chessComponent.setSelected(true);
                first = chessComponent;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (handleSecond(chessboard.getChessComponents()[i][j])) {
                            chessboard.getChessComponents()[i][j].setTrace(true);
                        }
                    }
                }
                chessboard.repaint();
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                click();
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
                chessboard.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                click();
                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();
                first.setSelected(false);
                first = null;
                chessboard.repaint();
            }
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }

    private void click(){
        try {
            File musicPath = new File("D:\\CS102A\\ChessProject\\images\\click.wav");
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                click = AudioSystem.getClip();
                click.open(audioInput);
                FloatControl gainControl = (FloatControl) click.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6.0f);
                click.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
