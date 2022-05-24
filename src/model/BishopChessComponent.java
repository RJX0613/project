package model;

import controller.ClickController;
import view.ChessGameFrame;
import view.ChessboardPoint;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示国际象棋里面的车
 */
public class BishopChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image bishopImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        this.initiateBishopImage(color);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (source.getX() + source.getY() == destination.getX() + destination.getY()) {
            int row1 = Math.max(source.getX(), destination.getX());
            int row2 = Math.min(source.getX(), destination.getX());
            int col1 = Math.max(source.getY(), destination.getY());
            int col2 = Math.min(source.getY(), destination.getY());
            for (int i = row1 - 1, j = col2 + 1; i > row2 && j < col1; i--, j++) {
                if (!(chessComponents[i][j] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getX() - source.getY() == destination.getX() - destination.getY()) {
            int row1 = Math.max(source.getX(), destination.getX());
            int row2 = Math.min(source.getX(), destination.getX());
            int col1 = Math.max(source.getY(), destination.getY());
            int col2 = Math.min(source.getY(), destination.getY());
            for (int i = row2 + 1, j = col2 + 1; i < row1 && j < col1; i++, j++) {
                if (!(chessComponents[i][j] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else { // Not on the same row or the same column.
            return false;
        }
        return true;
    }

    public List<ChessboardPoint> trace() {
        int x = getChessboardPoint().getX();
        int y = getChessboardPoint().getY();
        ChessComponent[][] chessboard= ChessGameFrame.gameController.getChessboard().getChessComponents();
        List<ChessboardPoint> answer = new ArrayList<>();
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        for (int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            if (chessboard[i][j] instanceof EmptySlotComponent) {
                answer.add(new ChessboardPoint(i, j));
            } else if (chessboard[i][j].getChessColor() == ChessColor.WHITE && getChessColor() == ChessColor.BLACK || chessboard[i][j].getChessColor() == ChessColor.BLACK && getChessColor() == ChessColor.WHITE) {
                answer.add(new ChessboardPoint(i, j));
                break;
            } else {
                break;
            }
        }
        return answer;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(bishopImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        if(isTrace()){
            g.setColor(Color.BLUE);
            g.drawOval(0,0,getWidth(),getHeight());
            this.setTrace(false);
        }
    }

}
