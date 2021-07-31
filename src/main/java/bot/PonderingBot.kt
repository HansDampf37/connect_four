package bot

import bot.tree.AlphaBetaPruning
import bot.tree.Node
import bot.tree.Tree
import model.Board
import model.Player
import model.Token

abstract class PonderingBot protected constructor(side: Token?, board: Board?, forecast: Int) : Player(side, board) {
    /**
     * The amount of moves this bot plans ahead
     */
    var forecast: Int
        protected set

    /**
     * The player that did the first move
     */
    @JvmField
    protected var beginner: Token? = null

    override fun getColumnOfNextMove(): Int {
        if (beginner == null) {
            for (f in board) if (!f.isEmpty) beginner = if (Token.PLAYER_1 == side) Token.PLAYER_2 else Token.PLAYER_1
        }
        beginner = side
        val states = Tree(forecast, board.WIDTH)
        traverse(forecast, 0, states.root, side)
        AlphaBetaPruning().run(states)
        val result: Int = states.root.filter{child -> !child.invisible}.map { c -> c.value }.indexOf(states.root.maxOf { c -> c.value })
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
    protected fun traverse(forecast: Int, lvl: Int, currentNode: Node, player: Token) {
        val winner = board.winner
        if (winner != Token.EMPTY) {
            currentNode.makeLeave()
            if (winner == side) {
                currentNode.value = Int.MAX_VALUE
            } else {
                currentNode.value = Int.MIN_VALUE
            }
        } else if (lvl == forecast) {
            currentNode.makeLeave()
            currentNode.value = rateState()
        } else {
            for (i in 0 until board.WIDTH) {
                if (board.throwInColumn(i, player)) {
                    traverse(
                        forecast, lvl + 1, currentNode.getChild(i),
                        if (player == Token.PLAYER_1) Token.PLAYER_2 else Token.PLAYER_1
                    )
                    board.removeOfColumn(i)
                } else {
                    currentNode.getChild(i).setInvisible()
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

class GameState(t: Tree, private val moves: List<Int>): Node(t) {
    fun possible(board: Board): Boolean {
        if (moves.any { it >= board.WIDTH || it < 0 }) return false
        for (i in 0 until board.WIDTH) if (moves.count{it == i} + colHeight(board, i) > board.HEIGHT) return false
        return true
    }

    fun possibleOnEmptyBoard(width: Int = 7, height: Int = 6): Boolean {
        if (moves.any { it >= width || it < 0 }) return false
        for (i in 0 until width) if (moves.count{it == i} > height) return false
        return true
    }

    private fun colHeight(board: Board, col: Int): Int {
         for (y in 0 until board.HEIGHT) {
             if (board[col, y].isEmpty) return y
         }
        return board.HEIGHT
    }
}