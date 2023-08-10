package model.procedure

import Controller
import gui.GUIHumanPlayer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Board
import model.Player
import model.Token
import rl.DatasetCreation
import java.lang.Thread.sleep
import java.util.*

open class Game(
    player1: Player,
    player2: Player,
    val width: Int,
    val height: Int,
    private val controller: Controller?
) : IGame {
    var board: Board = Board(width, height)
    val currentPlayer: Player get() = players[curPlayerInd]
    var players: Array<Player> = arrayOf(player1, player2)
    var running = false
    protected var curPlayerInd: Int = (Math.random() * players.size).toInt()

    init {
        if (player1.side == player2.side) throw IllegalArgumentException("sides must be different")
        reset()
    }

    protected fun throwInColumn(x: Int) {
        if (board.throwInColumn(x, players[curPlayerInd].side)) {
            curPlayerInd = ++curPlayerInd % players.size
            runBlocking {
                launch { players.forEach { it.onMovePlayed(x) } }
            }
            controller?.updateGui()
        }
    }

    override fun play(): Token {
        val movesPlayed: Map<Token, MutableList<Pair<Board, Int>>> =
            mapOf(Pair(Token.PLAYER_1, mutableListOf()), Pair(Token.PLAYER_2, mutableListOf()))
        running = true
        players.forEach { it.onNewGameStarted(this) }
        if (ConsoleOutput.playerGreetings) println(players[0].name + " vs " + players[1].name)
        var winner: Token = Token.EMPTY
        while (winner == Token.EMPTY && running) {
            sleep(500)
            if (ConsoleOutput.printBoard) println(board)
            val chosenColumn = players[curPlayerInd].getColumnOfNextMove()
            movesPlayed[currentPlayer.side]!!.add(Pair(board.clone(), chosenColumn))
            throwInColumn(chosenColumn)
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
        if (winner != Token.EMPTY) movesPlayed[winner]!!.forEach { DatasetCreation.savePosition(it.first, it.second, winner, "src/main/python/c4set") }
        else {
            movesPlayed[Token.PLAYER_1]!!.forEach { DatasetCreation.savePosition(it.first, it.second, Token.PLAYER_1, "src/main/python/connect4") }
            movesPlayed[Token.PLAYER_2]!!.forEach { DatasetCreation.savePosition(it.first, it.second, Token.PLAYER_2, "src/main/python/connect4") }
        }
        running = false
        return winner
    }

    final override fun reset() {
        val height = board.HEIGHT
        val width = board.WIDTH
        board = Board(width, height)
        players.forEach { it.onNewGameStarted(this) }
        players.filterIsInstance<GUIHumanPlayer>().forEach { it.nextMove = -1 }
        curPlayerInd = players.indexOfFirst { it.side == Token.PLAYER_1 }
    }
}