package bot

import bot.tree.Tree
import junit.framework.TestCase
import model.Board
import model.Token

class GameStateTest : TestCase() {
    var t = Tree(0,0)
    var gs: GameState = GameState(t, listOf(1,1,1,1,2,3,4,5,1,1))

    fun testPossible() {
        val board = Board(7,6)
        assertTrue(gs.possible(board))
        board.throwInColumn(1, Token.PLAYER_1)
        assertFalse(gs.possible(board))
    }
}