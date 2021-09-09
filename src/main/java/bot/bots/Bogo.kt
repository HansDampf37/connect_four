package bot.bots

import model.Board
import model.Player
import model.Token
import model.procedure.Game

class Bogo(side: Token, val width: Int, val height: Int) : Player(side) {
    private val board = Board(width, height)
    private var currentPlayer = Token.PLAYER_1

    override fun getColumnOfNextMove(): Int {
        return IntRange(0, width).filter{ board.stillSpaceIn(it) }.random()
    }

    override val name: String = "Bogo"

    override fun onMovePlayed(x: Int) {
        board.throwInColumn(x, currentPlayer)
        currentPlayer = currentPlayer.other()
    }

    override fun onGameOver() = Unit
}