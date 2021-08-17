package bot.tree

import model.Board
import model.Token

class GameState(val board: Board = Board(), val nextPlayer: Token) : Node() {

    val finished: Boolean = board.winner != Token.EMPTY

    fun getFutureGameStates(): Array<GameState> {
        if (finished) return arrayOf()
        return IntRange(0, board.WIDTH - 1).filter { row -> board.stillSpaceIn(row) }.map { move ->
            run {
                val newBoard = board.clone()
                newBoard.throwInColumn(move, nextPlayer)
                GameState(newBoard, nextPlayer.other())
            }
        }.toTypedArray()
    }

    /* companion object {
        private val testBoard: Board = Board()
    }

     init {
        for (i in moves.indices) testBoard.throwInColumn(moves[i], if (i % 2 == 0) Token.PLAYER_1 else Token.PLAYER_2)
        finished = testBoard.winner != Token.EMPTY
        for (i in moves.indices.reversed()) testBoard.removeOfColumn(moves[i])
    }*/

    /* fun possible(board: Board, moves: List<Int>): Boolean {
         if (moves.any { it >= board.WIDTH || it < 0 }) return false
         for (i in 0 until board.WIDTH) if (moves.count { it == i } + colHeight(board, i) > board.HEIGHT) return false
         return true
     }*/

    /* fun possibleOnEmptyBoard(width: Int = 7, height: Int = 6, moves: List<Int>): Boolean {
         if (moves.any { it >= width || it < 0 }) return false
         for (i in 0 until width) if (moves.count { it == i } > height) return false
         return true
     }

     fun movePossibleOnEmptyBoard(width: Int = 7, height: Int = 6, move: Int): Boolean {
         return possibleOnEmptyBoard(width, height, moves.toMutableList().apply { add(move) })
     }

     private fun colHeight(board: Board, col: Int): Int {
         for (y in 0 until board.HEIGHT) {
             if (board[col, y].isEmpty) return y
         }
         return board.HEIGHT
     }*/
}