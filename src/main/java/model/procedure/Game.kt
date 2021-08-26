package model.procedure

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Board
import model.Player
import model.Token
import java.util.Arrays

class Game(player1: Player, player2: Player) {
    private var board: Board
    private var players: Array<Player> = arrayOf(player1, player2)
    private var curPlayerInd: Int = (Math.random() * players.size).toInt()

    init {
        if (player1.board != player2.board) throw IllegalArgumentException("Boards must be equal")
        if (player1.side == player2.side) throw IllegalArgumentException("sides must be different")
        board = player1.board
        reset()
    }

    private fun throwInColumn(x: Int) {
        if (board.throwInColumn(x, players[curPlayerInd].side)) {
            curPlayerInd = ++curPlayerInd % players.size
            runBlocking {
                launch { players.forEach { it.onMovePlayed(x) } }
            }
        }
    }

    fun play(): Token {
        if (ConsoleOutput.playerGreetings) println(players[0].name + " vs " + players[1].name)
        var winner: Token = Token.EMPTY
        while (winner === Token.EMPTY) {
            if (ConsoleOutput.printBoard) println(board)
            throwInColumn(players[curPlayerInd].getColumnOfNextMove())
            if (!board.stillSpace()) break
            winner = board.winner
        }
        players.forEach { it.onGameOver() }
        //if (ConsoleOutput.printBoard) System.out.println(board);
        if (ConsoleOutput.gameResult) {
            val finalWinner = winner
            Arrays.stream(players).filter { p: Player -> p.side === finalWinner }
                .forEach { p: Player -> println(p.name + " won") }
            if (Arrays.stream(players).noneMatch { p: Player -> p.side === finalWinner }) println("Its a draw")
            println(board)
        }
        reset()
        return winner
    }

    private fun reset() {
        board = Board(7, 6)
        players.forEach { it.board = board }
        curPlayerInd = players.indexOfFirst { it.side == Token.PLAYER_1 }
    }
}