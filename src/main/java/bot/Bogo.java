package bot;


import model.*;
import model.Player;

public class Bogo extends Player {

    public Bogo(Identifier side, Board board) {
        super(side, board);
    }

    @Override
    public int getColumnOfNextMove() {
        return (int)(Math.random() * board.WIDTH);
    }

    @Override
    public void goodbye(Identifier winner) {
        System.out.println("Ciao");
    }

    @Override
    public String getName() {
        return "Bogo";
    }
}