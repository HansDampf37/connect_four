package model

abstract class Player {
    /**
     * Player_1 or Player_2
     */
    lateinit var side: Token

    /**
     * the board
     */
    lateinit var board: Board
    /**
     * Depending on [the boards][.board] state, this method returns in which column the next token is going to be.
     *
     * @return the columns index
     */
    abstract fun getColumnOfNextMove(): Int

    abstract val name: String

}