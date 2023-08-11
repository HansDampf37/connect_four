package model

import model.procedure.Game

/**
 * Player provides some events that get triggered in-game.
 * @see [onMovePlayed]
 * @see [onGameOver]
 * @see [onNewGameStarted]
 * @param side Player_1 or Player_2
 */
abstract class Player(var side: Token) {

    init {
        assert(side != Token.EMPTY)
    }
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
    open fun onMovePlayed(x: Int) = Unit

    /**
     * Gets invoked when the game has finished
     */
    open fun onGameOver() = Unit

    /**
     * Gets invoked if a new Game has been started and this player is part of it
     */
    open fun onNewGameStarted(game: Game) = Unit

    abstract val name: String
}