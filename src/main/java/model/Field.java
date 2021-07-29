package model;

/**
 * represents a field on a 4-gewinnt board
 */
public class Field {
    /**
     * the field's value
     */
    private Token token = Token.EMPTY;

    /**
     * getter for the player
     * 
     * @return the field's value
     */
    public Token getPlayer() {
        return token;
    }

    /**
     * setter for the field
     * 
     * @param player field's new value
     */
    public void setPlayer(Token player) {
        token = player;
    }

    /**
     * Returns if field is empty
     */
    public boolean isEmpty() {
        return token == Token.EMPTY;
    }
}