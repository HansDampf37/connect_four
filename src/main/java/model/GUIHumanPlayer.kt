package model

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class GUIHumanPlayer(side: Token, board: Board): Player(side, board) {
    var nextMove: Int = -1
        set(value) {
            field = value
            if (value >= 0) {
                lock.withLock { moveMade.signal() }
            }
        }
    private val lock = ReentrantLock()
    private val moveMade = lock.newCondition()

    override fun getColumnOfNextMove(): Int {
        while (nextMove == -1) {
            lock.withLock { moveMade.await() }
        }
        val temp = nextMove
        nextMove = -1
        return temp
    }

    override fun onMovePlayed(x: Int) = Unit

    override fun onGameOver() = Unit

    override val name: String
        get() = "Human Player"
}
