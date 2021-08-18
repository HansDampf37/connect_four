package bot

import bot.bots.PonderingBot
import bot.ratingfunctions.RandomRating
import junit.framework.TestCase
import model.Board
import model.Player
import model.Token
import model.procedure.Game

class PonderingBotTest : TestCase() {
    private val board = Board()
    private val bot = PonderingBot(Token.PLAYER_2, board, RandomRating(0..100))
    private val dummy = object : Player(Token.PLAYER_1, board) {
        override fun getColumnOfNextMove(): Int {
            return (0 until board.WIDTH).filter { board.stillSpaceIn(it) }.random()
        }

        override fun onMovePlayed(x: Int) {
            assertEquals(board, bot.tree.root.board)
        }

        override fun onGameOver() = Unit

        override val name = "dummy"
    }

    fun testPlay() {
        Game(dummy, bot).play()
    }

    public override fun setUp() {
        super.setUp()
    }
}