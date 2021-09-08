package model

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class HumanPlayer(side: Token, board: Board) : Player(side, board) {
    val nextMove = -1

    override fun getColumnOfNextMove(): Int {
        val br = BufferedReader(InputStreamReader(System.`in`))
        while (true) {
            try {
                val s = br.readLine()
                if (s != null && s.matches(Regex("[1-" + board.WIDTH + "]"))) return Integer.valueOf(s) - 1
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onMovePlayed(x: Int) = Unit

    override fun onGameOver() = Unit

    override val name: String = "Human"
}