package bot.bots

import bot.bots.tree.AlphaBetaPruning
import bot.bots.tree.TreeBuilder
import model.Board
import model.Player
import model.Token

/**
 * A rating-function rates a [Board] - - high return values are associated with a good rating
 */
interface RatingFunction: (Board) -> Int {
    fun name(): String
}

/**
 * A PonderingBot is a bot that considers future GameStates. When idle he builds a tree out of GameStates. When forced to choose a
 * move he pauses building the tree. Then he evaluates the GameStates at the tree's nodes using the given [ratingFunction].
 * He then uses AlphaBeta Pruning or minimax to calculate the best move. After this he updates the trees root to the current
 * GameState and resumes building the tree.
 *
 * @param ratingFunction rates a [Board] - high return values are associated with a good rating
 */
class PonderingBot (
    side: Token,
    board: Board,
    private val ratingFunction: RatingFunction
) : Player(side, board) {
    /**
     * The treeBuilder builds the Tree while idle
     */
    private var treeBuilder = TreeBuilder(ratingFunction)

    /**
     * the tree built by the [treeBuilder]
     */
    val tree get() = treeBuilder.tree

    private val threadForTreeBuilder: Thread = Thread(treeBuilder)

    /**
     * Start building the tree
     */
    init {
        threadForTreeBuilder.start()
    }

    override fun getColumnOfNextMove(): Int {
        /*if (!this::beginner.isInitialized) {
            beginner = if (board.all { it == Token.EMPTY }) side else side.other()
        }*/
        if (tree.root.board.isEmpty) return tree.root.board.WIDTH / 2
        treeBuilder.pause()
        //for (leaf in tree.leaves) {
        //    leaf.value = ratingFunction(leaf.board)
        //}
        val index = AlphaBetaPruning.run(tree)
        val rating = tree.root[index].value
        //if (rating > Int.MAX_VALUE - 20) println("Win inevitable")
        //if (rating < Int.MIN_VALUE + 20) println("Loss inevitable")
        println("Rating: $rating")
        treeBuilder.start()
        return index
    }

    override fun onMovePlayed(x: Int) {
        treeBuilder.moveMade(x)
    }

    override fun onGameOver() {
        treeBuilder.exit()
        threadForTreeBuilder.join()
    }

    override val name: String
        get() = "Smarty mit Bewertungsfunktion ${ratingFunction.name()}"
/*/**
      * The player that did the first move
      */
     protected lateinit var beginner: Token*/

    /*/**
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
            currentNode.value = ratingFunction(currentNode.board)
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
    }*/

    /*/**
     * evaluates the state of the current board
     * @return high return values are associated with a good rating
     */
    protected abstract fun rate(board: Board): Int*/
}

