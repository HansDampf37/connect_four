package bot.bots

import model.Board
import model.Player
import model.Token
import model.procedure.Game

class Bogo(side: Token) : Player(side) {
    private var width = -1
    private lateinit var board: Board
    private var currentPlayer = Token.PLAYER_1

    override fun getColumnOfNextMove(): Int {
        return IntRange(0, width - 1).filter{ board.stillSpaceIn(it) }.random()
    }

    override val name: String = "Bogo"

    override fun onMovePlayed(x: Int) {
        board.throwInColumn(x, currentPlayer)
        currentPlayer = currentPlayer.other()
    }

    override fun onGameOver() = Unit

    override fun onNewGameStarted(game: Game) {
        board = Board(game.width, game.height)
        width = game.width
    }
}