package model.procedure

import model.Board
import model.Player
import model.Token
import java.util.Arrays

class Game(player1: Player, player2: Player) {
    private var board = Board(7, 6)
    private var players: Array<Player> = arrayOf(player1, player2)
    private var curPlayerInd: Int = (Math.random() * players.size).toInt()

    init {
        player1.side = Token.PLAYER_1
        player2.side = Token.PLAYER_2
        player1.board = board
        player2.board = board
    }

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
            throwInColumn(players[curPlayerInd].getColumnOfNextMove())
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
    }
}