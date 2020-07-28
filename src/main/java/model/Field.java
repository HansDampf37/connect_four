package model;

/**
 * represents a field on a 4-gewinnt board
 */
public class Field {
    /**
     * the field's value
     */
    Identifier value = Identifier.EMPTY;

    /**
     * getter for the player
     * 
     * @return the field's value
     */
    public Identifier getPlayer() {
        return value;
    }

    /**
     * setter for the field
     * 
     * @param player field's new value
     */
    public void setPlayer(Identifier player) {
        value = player;
    }

    /**
     * Returns if field is empty
     */
    public boolean isEmpty() {
        return value == Identifier.EMPTY;
    }
}