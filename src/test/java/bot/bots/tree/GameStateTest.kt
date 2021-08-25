package bot.bots.tree

import junit.framework.TestCase
import model.Board
import model.Token

class GameStateTest : TestCase() {
    private var gs: GameState = GameState(Board(), Token.PLAYER_1, -1)

    fun testFutureGameState() {
        gs = GameState(TestUtils.noPredicament, Token.PLAYER_1)
        assertFalse(gs.finished)
        val futureStates = gs.getFutureGameStates()
        assertEquals(7, futureStates.size)
        for (i in futureStates.indices) {
            val token = futureStates[i].board.removeOfColumn(i)
            assertEquals(futureStates[i].board, gs.board)
            futureStates[i].board.throwInColumn(i, token)
        }
        assertEquals(4, GameState(TestUtils.gameAlmostDraw, Token.PLAYER_1).getFutureGameStates().size)
        assertEquals(4, GameState(TestUtils.gameProgressed, Token.PLAYER_1).getFutureGameStates().size)
        assertEquals(7, GameState(TestUtils.predicament1, Token.PLAYER_1).getFutureGameStates().size)
    }

    fun testFutureGameStateOnFull() {
        gs = GameState(TestUtils.gameDraw, Token.PLAYER_1)
        assertFalse(gs.finished)
        val futureStates = gs.getFutureGameStates()
        assertEquals(0, futureStates.size)
    }

    fun testFutureGameStateOnFinished() {
        val list = listOf(
            GameState(TestUtils.wonP1, Token.PLAYER_1),
            GameState(TestUtils.wonP2, Token.PLAYER_1),
            GameState(TestUtils.wonP1_second, Token.PLAYER_1),
            GameState(TestUtils.wonP2_lastMove, Token.PLAYER_1)
        )
        // because player 1 won the game the GameState must not replicate
        list.forEach { assertEquals(0, it.getFutureGameStates().size) }
    }
}