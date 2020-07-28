package model.procedure;

import model.*;

public abstract class PlayerType {
    /**
     * Player_1 or Player_2
     */
    protected Identifier side;
    /**
     * the board
     */
    protected Board board;

    /**
     * Constructor
     */
    public PlayerType(Identifier side, Board board) {
        this.side = side;
        this.board = board;
    }
    
    /**
     * Depending on {@link #board the boards} state, this method returns in which column the next token is going to be.
     * 
     * @return the columns index
     */
    public abstract int getColumnOfNextMove();

    /**
     * Optional method that gets invoked after the game has finished
     * 
     * @param winner the games winner
     */
    public abstract void goodbye(Identifier winner);
}