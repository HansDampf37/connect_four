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

    /**
     * Gets invoked when one of the two players is choosing a legal column to play
     * @param x the chosen column
     */
    abstract fun onMovePlayed(x: Int)

    /**
     * Gets invoked when the game has finished
     */
    abstract fun onGameOver()

    abstract val name: String

}