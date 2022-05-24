package view;


import controller.ClickController;
import controller.GameController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;
    private ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private GameController gameController;
    public ArrayList<String> step=new ArrayList<>();

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public ClickController getClickController() {
        return clickController;
    }

    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);
        initChessboard();
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        int win = 0;
        if(chess2 instanceof KingChessComponent){
            if(chess2.getChessColor()==ChessColor.BLACK){
                win=1;
            }else{
                win=2;
            }
        }
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
//        chess1.repaint();
        chess2.repaint();
        if(chess1 instanceof PawnChessComponentBlack){
            if(chess1.getChessboardPoint().getX()==7){
                remove(chess1);
                JFrame jFrame=new JFrame("升级！");
                jFrame.setSize(500,500);
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
                jFrame.setLayout(null);

                JButton bishop=new JButton("Bishop");
                bishop.setLocation(50,50);
                bishop.setSize(200,100);
                bishop.setFont(new Font("Rockwell", Font.ITALIC, 15));
                jFrame.add(bishop);
                ChessComponent chess3=new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, clickController, CHESS_SIZE);
                bishop.addActionListener(e -> {
                    //remove(chess1);
                    add(chess3);
                    chessComponents[chess3.getChessboardPoint().getX()][chess3.getChessboardPoint().getY()]=chess3;
                    chess3.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"B"));
                    jFrame.dispose();
                });

                JButton knight=new JButton("Knight");
                knight.setLocation(250,50);
                knight.setSize(200,100);
                knight.setFont(new Font("Rockwell", Font.ITALIC, 15));
                jFrame.add(knight);
                ChessComponent chess4=new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.BLACK, clickController, CHESS_SIZE);
                knight.addActionListener(e -> {
                   // remove(chess1);
                    add(chess4);
                    chessComponents[chess4.getChessboardPoint().getX()][chess4.getChessboardPoint().getY()]=chess4;
                    chess4.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"N"));
                    jFrame.dispose();
                });

                JButton Queen=new JButton("Queen");
                Queen.setLocation(50,250);
                Queen.setSize(200,100);
                Queen.setFont(new Font("Rockwell",Font.ITALIC, 15));
                jFrame.add(Queen);
                ChessComponent chess5=new QueenChessComponent(chess1.getChessboardPoint(),chess1.getLocation(),ChessColor.BLACK,clickController,CHESS_SIZE);
                Queen.addActionListener(e -> {
                    //remove(chess1);
                    add(chess5);
                    chessComponents[chess5.getChessboardPoint().getX()][chess5.getChessboardPoint().getY()]=chess5;
                    chess5.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"Q"));
                    jFrame.dispose();
                });

                JButton Rook=new JButton("Rook");
                Rook.setLocation(250,250);
                Rook.setSize(200,100);
                Rook.setFont(new Font("Rockwell",Font.ITALIC, 15));
                jFrame.add(Rook);
                ChessComponent chess6=new RookChessComponent(chess1.getChessboardPoint(),chess1.getLocation(),ChessColor.BLACK,clickController,CHESS_SIZE);
                Rook.addActionListener(e -> {
                    //remove(chess1);
                    add(chess6);
                    chessComponents[chess6.getChessboardPoint().getX()][chess6.getChessboardPoint().getY()]=chess6;
                    chess6.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"R"));
                    jFrame.dispose();
                });
                jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }
        }
        if(chess1 instanceof PawnChessComponentWhite){
            if(chess1.getChessboardPoint().getX()==0){
                remove(chess1);
                JFrame jFrame=new JFrame();
                jFrame.setSize(500,500);
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
                jFrame.setLayout(null);

                JButton bishop=new JButton("Bishop");
                bishop.setLocation(50,50);
                bishop.setSize(200,100);
                bishop.setFont(new Font("Rockwell", Font.ITALIC, 15));
                jFrame.add(bishop);
                ChessComponent chess3=new BishopChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, clickController, CHESS_SIZE);
                bishop.addActionListener(e -> {
                    //remove(chess1);
                    add(chess3);
                    chessComponents[chess3.getChessboardPoint().getX()][chess3.getChessboardPoint().getY()]=chess3;
                    chess3.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"b"));
                    jFrame.dispose();
                });

                JButton knight=new JButton("Knight");
                knight.setLocation(250,50);
                knight.setSize(200,100);
                knight.setFont(new Font("Rockwell", Font.ITALIC, 15));
                jFrame.add(knight);
                ChessComponent chess4=new KnightChessComponent(chess1.getChessboardPoint(), chess1.getLocation(), ChessColor.WHITE, clickController, CHESS_SIZE);
                knight.addActionListener(e -> {
                   // remove(chess1);
                    add(chess4);
                    chessComponents[chess4.getChessboardPoint().getX()][chess4.getChessboardPoint().getY()]=chess4;
                    chess4.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"n"));
                    jFrame.dispose();
                });

                JButton Queen=new JButton("Queen");
                Queen.setLocation(50,250);
                Queen.setSize(200,100);
                Queen.setFont(new Font("Rockwell",Font.ITALIC, 15));
                jFrame.add(Queen);
                ChessComponent chess5=new QueenChessComponent(chess1.getChessboardPoint(),chess1.getLocation(),ChessColor.WHITE,clickController,CHESS_SIZE);
                Queen.addActionListener(e -> {
                    //remove(chess1);
                    add(chess5);
                    chessComponents[chess5.getChessboardPoint().getX()][chess5.getChessboardPoint().getY()]=chess5;
                    chess5.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"q"));
                    jFrame.dispose();
                });

                JButton Rook=new JButton("Rook");
                Rook.setLocation(250,250);
                Rook.setSize(200,100);
                Rook.setFont(new Font("Rockwell",Font.ITALIC, 15));
                jFrame.add(Rook);
                ChessComponent chess6=new RookChessComponent(chess1.getChessboardPoint(),chess1.getLocation(),ChessColor.WHITE,clickController,CHESS_SIZE);
                Rook.addActionListener(e -> {
                   // remove(chess1);
                    add(chess6);
                    chessComponents[chess6.getChessboardPoint().getX()][chess6.getChessboardPoint().getY()]=chess6;
                    chess6.repaint();
                    step.set(step.size()-1,String.format("%d%d%d%d%s",row2,col2,row1,col1,"r"));
                    jFrame.dispose();
                });
            }
        }
        if(win==1){
            JOptionPane.showConfirmDialog(null, "白方获胜", "提示", JOptionPane.DEFAULT_OPTION);
        }else if(win==2){
            JOptionPane.showConfirmDialog(null, "黑方获胜", "提示", JOptionPane.DEFAULT_OPTION);
        }
        step.add(String.format("%d%d%d%d%s",row2,col2,row1,col1,"_"));
        gameController.chessGameFrame.getTimerTask().resetTime();
    }

    public void move(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
//                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
                ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE);
                chessComponent.setVisible(true);
                putChessOnBoard(chessComponent);
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        gameController.chessGameFrame.changePlayer(currentColor.getName());
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBlackPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponentBlack(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initWhitePawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponentWhite(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initChessboard() {
        initiateEmptyChessboard();
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initQueenOnBoard(0, CHESSBOARD_SIZE - 4, ChessColor.BLACK);
        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKingOnBoard(0, 3, ChessColor.BLACK);
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);


        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 4, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);

        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initBlackPawnOnBoard(1, i, ChessColor.BLACK);
            initWhitePawnOnBoard(CHESSBOARD_SIZE - 2, i, ChessColor.WHITE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

}
