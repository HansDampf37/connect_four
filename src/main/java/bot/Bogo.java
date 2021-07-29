package bot;

import model.*;
import model.Player;

public class Bogo extends Player {

    public Bogo(Token side, Board board) {
        super(side, board);
    }

    @Override
    public int getColumnOfNextMove() {
        return (int)(Math.random() * board.WIDTH);
    }

    @Override
    public String getName() {
        return "Bogo";
    }
}