package bot.ratingfunctions.ruediger

import TestUtils
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import model.Token
import model.procedure.ConsoleOutput
import org.junit.After
import org.junit.Before
import org.junit.Test

class RuedigerDerBotTests {

    @Before
    fun setup() {
        ConsoleOutput.predicamentSearch = true
    }

    @After
    fun tearDown() {
        ConsoleOutput.predicamentSearch = false
    }

    @Test
    fun testPredicament1() {
        println(TestUtils.predicament1)
        // because of the predicament this board is rated high
        assertEquals(Integer.MAX_VALUE - 1, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.predicament1))
    }

    @Test
    fun testPredicament2() {
        println(TestUtils.createPredicamentForP1)
        val board = TestUtils.createPredicamentForP1.clone().apply { throwInColumn(2, Token.PLAYER_1) }
        // because of the predicament this board is rated high
        assertEquals(Integer.MAX_VALUE - 4, RuedigerDerBot(Token.PLAYER_1).invoke(board))
    }

    @Test
    fun testNoPredicament() {
        println(TestUtils.noPredicament)
        // since the predicament is blocked by thread by player 2 it can't be used
        assertTrue(RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.noPredicament) < 10000000)
    }

    @Test
    fun testDraw() {
        println(TestUtils.gameDraw)
        //game is draw -> rating should be 0
        assertEquals(0, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.gameDraw))
    }

    @Test
    fun testProgressed() {
        val ruedigerDerBot = RuedigerDerBot(Token.PLAYER_2)
        ruedigerDerBot.invoke(TestUtils.gameProgressed)
        assertEquals(3, ruedigerDerBot.ownThreatMap[1][1])
        assertEquals(3, ruedigerDerBot.ownThreatMap[4][1])
        assertEquals(3, ruedigerDerBot.ownThreatMap[4][3])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[1][1])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[1][2])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[1][3])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[4][3])
        assertEquals(3, ruedigerDerBot.opponentThreatMap[4][4])
        assertEquals(-123218, ruedigerDerBot.invoke(TestUtils.gameProgressed))
        println(ruedigerDerBot.invoke(TestUtils.gameProgressed))
        println(TestUtils.gameProgressed)
        println(print(ruedigerDerBot.ownThreatMap))
        println(print(ruedigerDerBot.opponentThreatMap))
    }

    @Test
    fun testWin() {
        println(TestUtils.wonP1)
        // because of the 4 in a row this board is rated high
        assertEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.wonP1))
        assertEquals(Integer.MIN_VALUE, RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.wonP1))

        println(TestUtils.wonP1_second)
        // because of the 4 in a row this board is rated high
        assertEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.wonP1_second))
        assertEquals(Integer.MIN_VALUE, RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.wonP1_second))

        println(TestUtils.wonP2)
        // because of the 4 in a row this board is rated high
        assertEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.wonP2))
        assertEquals(Integer.MIN_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.wonP2))

        println(TestUtils.wonP2_lastMove)
        // because of the 4 in a row this board is rated high
        assertEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.wonP2_lastMove))
        assertEquals(Integer.MIN_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.wonP2_lastMove))
    }

    @Test
    fun testMirroredBoardSameResult() {
        assertEquals(RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.gameProgressedMirrored), RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.gameProgressed))
        assertEquals(RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.gameProgressedMirrored), RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.gameProgressed))
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

