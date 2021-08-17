package model.procedure

import model.Board
import model.Player
import model.Token
import java.util.Arrays

abstract class Game(player1: Player, player2: Player) {
    protected var board = Board(7, 6)
    protected var players: Array<Player> = arrayOf(player1, player2)
    private var curPlayerInd: Int = (Math.random() * players.size).toInt()

    protected abstract fun definePlayers()

    private fun throwInColumn(x: Int) {
        if (board.throwInColumn(x, players[curPlayerInd].side)) {
            curPlayerInd = ++curPlayerInd % players.size
        }
    }

    fun play() {
        println(players[0].name + " vs " + players[1].name)
        var winner: Token
        winner = Token.EMPTY
        while (winner === Token.EMPTY) {
            if (ConsoleOutput.printBoard) println(board)
            throwInColumn(players[curPlayerInd].columnOfNextMove)
            if (!board.stillSpace()) break
            winner = board.winner
        }
        //if (ConsoleOutput.printBoard) System.out.println(board);
        if (ConsoleOutput.gameResult) {
            val finalWinner = winner
            Arrays.stream(players).filter { p: Player -> p.side === finalWinner }
                .forEach { p: Player -> println(p.name + " won") }
            if (Arrays.stream(players).noneMatch { p: Player -> p.side === finalWinner }) println("Its a draw")
            println(board)
        }
        reset()
    }

    private fun reset() {
        board = Board(7, 6)
        curPlayerInd = (Math.random() * players.size).toInt()
        definePlayers()
    }
}