package model.procedure

import model.Token

interface IGame {
    /**
     * Play a game of 4-gewinnt.
     * @return Token of winner
     */
    fun play(): Token

    /**
     * Reset internal variables. Invoking play after reset() must always be possible.
     */
    fun reset()
}