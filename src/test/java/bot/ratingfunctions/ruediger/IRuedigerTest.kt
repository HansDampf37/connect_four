package bot.ratingfunctions.ruediger

import junit.framework.TestCase.assertEquals
import model.Board
import model.HumanPlayer
import model.Token
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertNotEquals

class IRuedigerTest {
    private var b = Board(7, 6)
    private val r = RuedigerDerBot(Token.PLAYER_1)
    private val h = HumanPlayer(Token.PLAYER_2, b)

    @BeforeEach
    fun setUp() {
        b = Board(7,6)
    }

    @Test
    fun testPredicament1() {
        b.throwInColumn(1, Token.PLAYER_1)
        b.throwInColumn(3, Token.PLAYER_1)
        b.throwInColumn(4, Token.PLAYER_1)
        b.throwInColumn(5, Token.PLAYER_2)
        //
        b.throwInColumn(3, Token.PLAYER_2)
        b.throwInColumn(4, Token.PLAYER_2)
        //
        b.throwInColumn(3, Token.PLAYER_1)
        b.throwInColumn(4, Token.PLAYER_2)
        //
        b.throwInColumn(4, Token.PLAYER_1)
        println(b)
        // because of the predicament this board is rated high
        assertEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(b))
    }
    @Test
    fun testPredicament2() {
        b.throwInColumn(1, Token.PLAYER_2)
        b.throwInColumn(3, Token.PLAYER_1)
        b.throwInColumn(4, Token.PLAYER_1)
        b.throwInColumn(5, Token.PLAYER_2)
        //
        b.throwInColumn(1, Token.PLAYER_1)
        b.throwInColumn(3, Token.PLAYER_1)
        b.throwInColumn(4, Token.PLAYER_1)
        b.throwInColumn(5, Token.PLAYER_2)
        //
        b.throwInColumn(1, Token.PLAYER_2)
        b.throwInColumn(3, Token.PLAYER_2)
        b.throwInColumn(4, Token.PLAYER_2)
        //
        b.throwInColumn(3, Token.PLAYER_1)
        b.throwInColumn(4, Token.PLAYER_2)
        //
        b.throwInColumn(4, Token.PLAYER_1)
        println(b)
        // since the predicament is blocked by thread by player 2 it can't be used
        assertNotEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(b))
    }

    @Test
    fun testEval1() {
        b.throwInColumn(0, r.side)
        b.throwInColumn(1, r.side)
        b.throwInColumn(3, r.side)
        b.throwInColumn(6, r.side)
        b.throwInColumn(2, h.side)
        b.throwInColumn(5, h.side)
        //
        b.throwInColumn(0, h.side)
        b.throwInColumn(2, h.side)
        b.throwInColumn(3, h.side)
        b.throwInColumn(5, h.side)
        b.throwInColumn(6, h.side)
        //
        b.throwInColumn(0, r.side)
        b.throwInColumn(2, r.side)
        b.throwInColumn(3, r.side)
        b.throwInColumn(5, h.side)
        b.throwInColumn(6, r.side)
        //
        b.throwInColumn(0, h.side)
        b.throwInColumn(2, r.side)
        b.throwInColumn(3, r.side)
        b.throwInColumn(5, r.side)
        b.throwInColumn(6, h.side)
        //
        b.throwInColumn(0, r.side)
        b.throwInColumn(2, r.side)
        b.throwInColumn(3, h.side)
        //
        b.throwInColumn(0, h.side)
        b.throwInColumn(2, h.side)
        b.throwInColumn(3, r.side)

        val ruedigerDerBot = RuedigerDerBot(Token.PLAYER_2)
        println(ruedigerDerBot.invoke(b))
        assertEquals(3, ruedigerDerBot.ownThreatMap[1][1])
        assertEquals(3, ruedigerDerBot.ownThreatMap[4][1])
        assertEquals(3, ruedigerDerBot.ownThreatMap[4][3])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[1][1])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[1][2])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[1][3])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[4][3])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[4][4])
        assertEquals(-94, ruedigerDerBot.invoke(b))
        println(b)
        println(print(ruedigerDerBot.ownThreatMap))
        println(print(ruedigerDerBot.opponentThreatMap))
    }

    private fun print(map: Array<IntArray>): String {
        val str = StringBuilder().append("|")
        for (y in map[0].size - 1 downTo 0) {
            for (ints in map) str.append(ints[y]).append("|")
            if (y != 0) str.append("\n|")
        }
        str.append("\n")
        for (x in map.indices) str.append("-").append(x + 1)
        return str.append("-").toString()
    }
}

