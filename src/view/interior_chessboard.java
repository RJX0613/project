package view;

import model.ChessColor;
import model.ChessComponent;
import model.EmptySlotComponent;
import model.KingChessComponent;

public class interior_chessboard {

    public ChessComponent[][] chessComponents ;
    public ChessColor currentColor;







    public interior_chessboard(ChessComponent[][] chessComponents, ChessColor currentColor){
        this.currentColor=currentColor;
        this.chessComponents=chessComponents;

    }
    public boolean ischecked(ChessColor currentColor){
        int x=0;int y=0;
        Loop :for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessComponents[i][j] instanceof KingChessComponent){
                    if (chessComponents[i][j].getChessColor()==currentColor){
                        x=i;
                        y=j;
                        break  Loop;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(chessComponents[i][j].getChessColor()!=currentColor){
                    if (chessComponents[i][j].canMoveTo(this.chessComponents,new ChessboardPoint(x,y))){
                        return true;
                    }
                }


            }
        }return false;

    }

    public void swapChessComponents(ChessboardPoint chessboardPoint1,ChessboardPoint chessboardPoint2) {
            chessComponents[chessboardPoint2.getX()][chessboardPoint2.getY()]=chessComponents[chessboardPoint1.getX()][chessboardPoint1.getY()];
            chessComponents[chessboardPoint2.getX()][chessboardPoint2.getY()].setChessboardPoint(chessboardPoint2);
            chessComponents[chessboardPoint1.getX()][chessboardPoint1.getY()]=new EmptySlotComponent(chessboardPoint1,ChessColor.NONE);
    }

    public ChessComponent[][] getChessComponent(){
        return chessComponents;

    }


    public ChessColor getCurrentColor(){
        return currentColor;
    }

}
