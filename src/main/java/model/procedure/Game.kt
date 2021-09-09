package model.procedure

import gui.Gui
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Board
import gui.GUIHumanPlayer
import model.Player
import model.Token
import java.util.*

class Game(player1: Player, player2: Player, val width: Int, val height: Int) : IGame {
    var board: Board
    val currentPlayer: Player get() = players[curPlayerInd]
    var players: Array<Player> = arrayOf(player1, player2)
    private var curPlayerInd: Int = (Math.random() * players.size).toInt()
    private val gui = Gui(this)

    init {
        if (player1.side == player2.side) throw IllegalArgumentException("sides must be different")
        board = Board(width, height)
        reset()
    }

    private fun throwInColumn(x: Int) {
        if (board.throwInColumn(x, players[curPlayerInd].side)) {
            curPlayerInd = ++curPlayerInd % players.size
            runBlocking {
                launch { players.forEach { it.onMovePlayed(x) } }
            }
            gui.update()
        }
    }

    override fun play(): Token {
        players.forEach { it.onNewGameStarted(this) }
        if (ConsoleOutput.playerGreetings) println(players[0].name + " vs " + players[1].name)
        var winner: Token = Token.EMPTY
        while (winner == Token.EMPTY) {
            if (ConsoleOutput.printBoard) println(board)
            throwInColumn(players[curPlayerInd].getColumnOfNextMove())
            if (!board.stillSpace()) break
            winner = board.winner
        }
        players.forEach { it.onGameOver() }
        //if (ConsoleOutput.printBoard) System.out.println(board);
        if (ConsoleOutput.gameResult) {
            val finalWinner = winner
            Arrays.stream(players).filter { p: Player -> p.side == finalWinner }
                .forEach { p: Player -> println(p.name + " won") }
            if (Arrays.stream(players).noneMatch { p: Player -> p.side == finalWinner }) println("Its a draw")
            println(board)
        }
        return winner
    }

    override fun reset() {
        val height = board.HEIGHT
        val width = board.WIDTH
        board = Board(width, height)
        players.filterIsInstance<GUIHumanPlayer>().forEach { it.nextMove = -1 }
        curPlayerInd = players.indexOfFirst { it.side == Token.PLAYER_1 }
    }
}