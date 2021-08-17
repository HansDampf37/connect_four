package bot

import bot.tree.GameState
import junit.framework.TestCase
import model.Board
import model.Token

class GameStateTest : TestCase() {
    private var gs: GameState = GameState(Board(), Token.PLAYER_1)

    fun testPossible() {
        (0 until 7).forEach { assertTrue(gs.movePossible(it)) }
        val board = Board()
        mutableListOf(1,1,1,1,2,3,4,5,1,1).forEach { board.throwInColumn(it, Token.PLAYER_1) }
        val gameState = GameState(board, Token.PLAYER_1)
        assertFalse(gameState.movePossible(1))
        assertTrue(gameState.finished)
    }
}