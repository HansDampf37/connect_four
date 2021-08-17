package bot

import bot.tree.AlphaBetaPruning
import bot.tree.Node
import bot.tree.Tree
import model.Board
import model.Player
import model.Token

abstract class PonderingBot protected constructor(forecast: Int) : Player() {
    /**
     * The amount of moves this bot plans ahead
     */
    var forecast: Int
        protected set

    /**
     * The player that did the first move
     */
    protected var beginner: Token? = null

    override fun getColumnOfNextMove(): Int {
        if (beginner == null) {
            for (f in board) if (!f.isEmpty) beginner = if (Token.PLAYER_1 == side) Token.PLAYER_2 else Token.PLAYER_1
        }
        beginner = side
        val states: Tree<Node> = Tree(forecast, board.WIDTH)
        traverse(states, forecast, 0, states.root, side)
        AlphaBetaPruning.run(states)
        val result: Int = states.root.filter { child -> !child.invisible }.map { c -> c.value }
            .indexOf(states.root.maxOf { c -> c.value })
        val rating = states.root.value
        if (rating > Int.MAX_VALUE - 20) println("Win inevitable")
        if (rating < Int.MIN_VALUE + 20) println("Loss inevitable")
        println("Rating after own move: $rating")
        return result
    }

    /**
     * Recursive method that lets a [Tree] represent the game's states. Every
     * leave's value is set to the according state's rating
     *
     * @param forecast    amount of moves the algorithm is planning ahead
     * @param lvl         the current level
     * @param currentNode the current node
     */
    protected fun traverse(tree: Tree<Node>, forecast: Int, lvl: Int, currentNode: Node, player: Token) {
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
            currentNode.value = rateState()
        } else {
            for (i in 0 until board.WIDTH) {
                if (board.throwInColumn(i, player)) {
                    traverse(
                        tree,
                        forecast, lvl + 1, currentNode.children[i],
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
    protected abstract fun rateState(): Int

    init {
        this.forecast = forecast * 2
    }
}

class GameState(private val board: Board = Board(), private val nextPlayer: Token) : Node() {

    val finished: Boolean = board.winner != Token.EMPTY

    fun movePossible(move: Int): Boolean {
        return board.stillSpaceIn(move)
        // return possible(board, moves.toMutableList().apply { add(move) })
    }

    fun getFutureGameStates(): Array<GameState> {
        if (finished) return arrayOf()
        return IntRange(0, board.WIDTH - 1).filter { movePossible(it) }.map { move ->
            GameState(board.clone().apply { throwInColumn(move, nextPlayer) }, nextPlayer.other())
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