package model;

public abstract class Player {
    /**
     * Player_1 or Player_2
     */
    protected Token side;
    /**
     * the board
     */
    protected Board board;

    /**
     * Constructor
     */
    public Player(Token side, Board board) {
        this.side = side;
        this.board = board;
    }
    
    /**
     * Depending on {@link #board the boards} state, this method returns in which column the next token is going to be.
     * 
     * @return the columns index
     */
    public abstract int getColumnOfNextMove();

    public abstract String getName();

    public Token getSide() {
        return side;
    }
}