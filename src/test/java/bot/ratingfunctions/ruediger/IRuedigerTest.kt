package bot.ratingfunctions.ruediger

import bot.TestUtils
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import model.Token
import org.junit.Test

class IRuedigerTest {
    @Test
    fun testPredicament1() {
        println(TestUtils.predicament1)
        // because of the predicament this board is rated high
        assertEquals(Integer.MAX_VALUE - 1, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.predicament1))
    }
    @Test
    fun testPredicament2() {
        println(TestUtils.noPredicament)
        // since the predicament is blocked by thread by player 2 it can't be used
        assertTrue(RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.noPredicament) < 1000)
    }

    @Test
    fun testEval1() {

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
        assertEquals(-94, ruedigerDerBot.invoke(TestUtils.gameProgressed))
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

        println(TestUtils.wonP2)
        // because of the 4 in a row this board is rated high
        assertEquals(Integer.MAX_VALUE, RuedigerDerBot(Token.PLAYER_2).invoke(TestUtils.wonP2))
        assertEquals(Integer.MIN_VALUE, RuedigerDerBot(Token.PLAYER_1).invoke(TestUtils.wonP2))
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

