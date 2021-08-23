package bot

import bot.bots.PonderingBot
import bot.ratingfunctions.RandomRating
import junit.framework.TestCase
import model.Board
import model.Player
import model.Token
import model.procedure.Game
import java.lang.Thread.sleep

class PonderingBotTest : TestCase() {
    private val board = Board()
    private val bot = PonderingBot(Token.PLAYER_2, board, RandomRating(0..100))
    private val dummy = object : Player(Token.PLAYER_1, board) {
        override fun getColumnOfNextMove(): Int {
            sleep(1000)
            return (0 until board.WIDTH).filter { board.stillSpaceIn(it) }.random()
        }

        override fun onMovePlayed(x: Int) {
            assertEquals(board, bot.tree.root.board)
            println("amount of nodes ${bot.tree.size}")
            if (bot.tree.root.size < 7) println ("NÄCHSTES BOARD HAT NICHT 7 FOLGEZUSTÄNDE \n$board")
        }

        override fun onGameOver() = Unit

        override val name = "dummy"
    }

    fun testPlay() {
        Game(bot, dummy).play()
    }

    public override fun setUp() {
        super.setUp()
    }
}