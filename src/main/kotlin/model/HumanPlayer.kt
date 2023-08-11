package model

import model.procedure.Game
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class HumanPlayer(side: Token) : Player(side) {

    private var width: Int = -1

    override fun getColumnOfNextMove(): Int {
        val br = BufferedReader(InputStreamReader(System.`in`))
        while (true) {
            try {
                val s = br.readLine()
                if (s != null && s.matches(Regex("[1-$width]"))) return Integer.valueOf(s) - 1
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onNewGameStarted(game: Game) {
        width = game.width
    }

    override fun onMovePlayed(x: Int) = Unit

    override fun onGameOver() = Unit

    override val name: String = "Human"
}