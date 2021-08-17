package model

/**
 * @param side Player_1 or Player_2
 * @param board the board which is played on
 */
abstract class Player(var side: Token, var board: Board) {
    /**
     * Depending on [the boards][.board] state, this method returns in which column the next token is going to be.
     *
     * @return the columns index
     */
    abstract fun getColumnOfNextMove(): Int

    abstract val name: String

}