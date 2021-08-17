package bot.tree

import junit.framework.TestCase
import model.Board
import model.Token

class GameStateTest : TestCase() {
    private var gs: GameState = GameState(Board(), Token.PLAYER_1)

    fun testFutureGameState() {
        val board = Board()
        var player = Token.PLAYER_1

        for (y in 0 until board.HEIGHT) {
            var count = 0
            player = player.other()
            for (x in 0 until board.WIDTH) {
                if ((x % 2) != 0 || y != board.HEIGHT - 1) {
                    if ((count++ % 2) == 0) player = player.other()
                    board.throwInColumn(x, player)
                }
            }
        }
        println(board)
        gs = GameState(board, Token.PLAYER_1)
        assertFalse(gs.finished)
        val futureStates = gs.getFutureGameStates()
        assertEquals(4, futureStates.size)
    }

    fun testFutureGameStateOnFinished() {
        val b = Board()
        b.throwInColumn(3, Token.PLAYER_1)
        b.throwInColumn(3, Token.PLAYER_2)
        b.throwInColumn(4, Token.PLAYER_1)
        b.throwInColumn(3, Token.PLAYER_2)
        b.throwInColumn(2, Token.PLAYER_1)
        b.throwInColumn(1, Token.PLAYER_2)
        b.throwInColumn(5, Token.PLAYER_1)
        println(b.toString())
        gs = GameState(b, Token.PLAYER_1)
        // because player 1 won the game the GameState must not replicate
        assertEquals(0, gs.getFutureGameStates().size)
    }
}