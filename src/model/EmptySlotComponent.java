package model;

import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    @Override
    public void loadResource() throws IOException {
    }

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size,Chessboard chessboard) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size, chessboard);
    }
    public EmptySlotComponent(ChessboardPoint chessboardPoint,ChessColor color){
        super(chessboardPoint,color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public List<ChessboardPoint> trace() {
        return new ArrayList<>();
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(null, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        if (isTrace()) {
            g.setColor(Color.BLUE);
            g.drawOval(0, 0, getWidth(), getHeight());
            this.setTrace(false);

        }
        if (isEntered()){
            g.setColor(Color.green);
            g.fillOval(0, 0, getWidth(), getHeight());
            this.setEntered(false);
        }

    }
}
