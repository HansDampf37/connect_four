package bot.bots

import model.Board
import model.Player
import model.Token
import model.procedure.Game
import java.lang.Thread.sleep

class Ricardo(val token: Token, private val url: String = "http://127.0.0.1:5000/predict_column") : Player(token) {
    private val board = Board()
    private var currentPlayer = Token.PLAYER_1
    override fun getColumnOfNextMove(): Int {
        fun playerToDouble(p: Token): Double {
            return when (p) {
                Token.EMPTY -> 0.5
                Token.PLAYER_1 -> 1.0
                Token.PLAYER_2 -> 0.0
            }
        }

        val par: Array<Array<Double>> =
            Array(board.HEIGHT) { y -> Array(board.WIDTH) { x -> playerToDouble(board.fields[x][board.HEIGHT - 1 - y]) } }
        val headers = mapOf("Content-Type" to "application/json")
        val data = mapOf("board" to par, "current_player" to 1)  // Update current player accordingly
        val response = khttp.post("http://127.0.0.1:5000/predict_column", headers = headers, json = data)
        if (response.statusCode == 200) {
            val jsonResponse = response.jsonObject
            return jsonResponse.getInt("predicted_column")
        } else {
            sleep(200)
            return getColumnOfNextMove()
        }
    }

    override fun onMovePlayed(x: Int) {
        board.throwInColumn(x, currentPlayer)
        currentPlayer = currentPlayer.other()
    }

    override fun onNewGameStarted(game: Game) {
        super.onNewGameStarted(game)
        currentPlayer = game.currentPlayer.side
    }

    override val name: String
        get() = "Ricardo"
}