package bot

import bot.tree.AlphaBetaPruning
import bot.tree.Node
import bot.tree.Tree
import bot.tree.TreeBuilder
import model.Board
import model.Player
import model.Token

abstract class PonderingBot protected constructor(forecast: Int, side: Token, board: Board) : Player(side, board) {
    /**
     * The amount of moves this bot plans ahead
     */
    var forecast: Int
        protected set

    var treeBuilder = TreeBuilder()

    init {
        treeBuilder.start()
    }

    /**
     * The player that did the first move
     */
    protected lateinit var beginner: Token

    override fun getColumnOfNextMove(): Int {
        if (!this::beginner.isInitialized) {
            beginner = if (board.all { it == Token.EMPTY }) side else side.other()
        }
        treeBuilder.pause()
        for (leaf in treeBuilder.tree.leaves) {
            leaf.value = rate(leaf.board)
        }
        val index = AlphaBetaPruning.run(treeBuilder.tree)
        val rating = treeBuilder.tree.root[index].value
        //if (rating > Int.MAX_VALUE - 20) println("Win inevitable")
        //if (rating < Int.MIN_VALUE + 20) println("Loss inevitable")
        println("Rating after own move: $rating")
        treeBuilder.start()
        return index
    }

    /**
     * Recursive method that lets a [Tree] represent the game's states. Every
     * leave's value is set to the according state's rating
     *
     * @param forecast    amount of moves the algorithm is planning ahead
     * @param lvl         the current level
     * @param currentNode the current node
     */
    protected fun traverse(tree: Tree<GameState>, forecast: Int, lvl: Int, currentNode: GameState, player: Token) {
        val winner = board.winner
        if (winner != Token.EMPTY) {
            tree.makeLeave(currentNode)
            if (winner == side) {
                currentNode.value = Int.MAX_VALUE
            } else {
                currentNode.value = Int.MIN_VALUE
            }
        } else if (lvl == forecast) {
            tree.makeLeave(currentNode)
            currentNode.value = rate(currentNode.board)
        } else {
            for (i in 0 until board.WIDTH) {
                if (board.throwInColumn(i, player)) {
                    traverse(
                        tree,
                        forecast, lvl + 1, currentNode.children[i] as GameState,
                        if (player == Token.PLAYER_1) Token.PLAYER_2 else Token.PLAYER_1
                    )
                    board.removeOfColumn(i)
                } else {
                    currentNode[i].setInvisible()
                }
            }
        }
    }

    /**
     * evaluates the state of the current board
     * @return high return values are associated with a good rating
     */
    protected abstract fun rate(board: Board): Int

    init {
        this.forecast = forecast * 2
    }
}

class GameState(val board: Board = Board(), val nextPlayer: Token) : Node() {

    val finished: Boolean = board.winner != Token.EMPTY

    fun movePossible(move: Int): Boolean {
        return board.stillSpaceIn(move)
        // return possible(board, moves.toMutableList().apply { add(move) })
    }

    fun getFutureGameStates(): Array<GameState> {
        if (finished) return arrayOf()
        return IntRange(0, board.WIDTH - 1).filter { movePossible(it) }.map { move ->
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