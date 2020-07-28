package model.procedure;


import model.*;

public class Bogo extends PlayerType {

    public Bogo(Identifier side, Board board) {
        super(side, board);
    }

    @Override
    public int getColumnOfNextMove() {
        return (int)(Math.random() * Board.WIDTH);
    }

    @Override
    public void goodbye(Identifier winner) {
        System.out.println("ciau");
    }
}