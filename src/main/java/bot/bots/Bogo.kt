package bot.bots

import model.Board
import model.Player
import model.Token

class Bogo(side: Token, board: Board) : Player(side, board) {
    override fun getColumnOfNextMove(): Int {
        return (Math.random() * board.WIDTH).toInt()
    }

    override val name: String = "Bogo"

    override fun onMovePlayed(x: Int) = Unit

    override fun onGameOver() = Unit
}