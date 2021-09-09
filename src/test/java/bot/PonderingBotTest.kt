package bot

import bot.bots.PonderingBot
import bot.ratingfunctions.ruediger.RuedigerDerBot
import junit.framework.TestCase
import model.Board
import model.Player
import model.Token
import model.procedure.ConsoleOutput
import model.procedure.Game
import java.lang.Thread.sleep

class PonderingBotTest : TestCase() {
    private val board = Board()
    private lateinit var bot: PonderingBot
    private lateinit var dummy: Player

    override fun setUp() {
        bot = PonderingBot(Token.PLAYER_2, RuedigerDerBot(Token.PLAYER_2))

        dummy = object : Player(Token.PLAYER_1) {
            override fun getColumnOfNextMove(): Int {
                sleep(1000)
                return (0 until board.WIDTH).filter { board.stillSpaceIn(it) }.random()
            }

            override fun onMovePlayed(x: Int) {
                assertEquals(board, bot.tree.root.board)
            }

            override fun onGameOver() = Unit

            override val name = "dummy"
        }
    }

    fun testPlay() {
        Game(bot, dummy, 7, 6).play()
    }

    fun testWin() {
        val n = 10
        var i = 0
        repeat (n) {
            ConsoleOutput.setAll(false, false, false, false, false, false, false, false, false)
            assertEquals("Ruediger lost against random", bot.side, Game(bot, dummy, 7, 6).play())
            println("won ${i++ + 1}/$n")
            setUp()
        }
    }
}