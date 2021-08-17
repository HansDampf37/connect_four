package bot.Ruediger

import model.Board
import model.HumanPlayer
import model.Token
import org.junit.Before
import org.junit.Test

class IRuedigerTest {
    private val b = Board(7, 6)
    private val r: IRuediger = RuedigerDerBot(3, Token.PLAYER_2, b)
    private val h = HumanPlayer(Token.PLAYER_1, b)
    @Before
    fun setup() {
        r.board = b
        h.board = b
        r.side = Token.PLAYER_1
        h.side = Token.PLAYER_2

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
    }

    @Test
    fun testEval() {
        println(b)
        println(r.rate(b))
        println(print(r.ownThreatMap))
        println(print(r.opponentThreatMap))
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