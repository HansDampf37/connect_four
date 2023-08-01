package bot.bots

import bot.bots.tree.AlphaBetaPruning
import bot.bots.tree.GameState
import bot.bots.tree.TreeBuilder
import bot.ratingfunctions.ruediger.RuedigerDerBot
import model.Board
import model.Player
import model.Token
import model.procedure.ConsoleOutput
import model.procedure.Game
import kotlin.concurrent.withLock

/**
 * A rating-function rates a [Board] - - high return values are associated with a good rating
 */
interface RatingFunction : (Board) -> Int {
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
class PonderingBot(
    side: Token,
    private val ratingFunction: RatingFunction = RuedigerDerBot(side)
) : Player(side) {
    /**
     * The treeBuilder builds the Tree while idle
     */
    lateinit var treeBuilder: TreeBuilder

    private lateinit var threadForTreeBuilder: Thread

    override fun onNewGameStarted(game: Game) {
        treeBuilder = TreeBuilder(ratingFunction, TreeBuilder.SimpleScheduler(1000), game.width, game.height)
        threadForTreeBuilder = Thread(treeBuilder)
        threadForTreeBuilder.start()
    }

    /**
     * the tree built by the [treeBuilder]
     */
    val tree get() = treeBuilder.tree

    override fun getColumnOfNextMove(): Int {
        if (tree.root.board.isEmpty) return tree.root.board.WIDTH / 2
        var index: Int
        // if there is a row that can be thrown into but the tree is not sufficiently calculated wait
        while (tree.root.size < IntRange(0, 6).filter { tree.root.board.stillSpaceIn(it) }.size) {
            treeBuilder.lock.withLock { treeBuilder.levelOneCalculated.await() }
        }
        treeBuilder.lock.withLock {
            index = AlphaBetaPruning.run(tree)
            val rating = tree.root.first { (it as GameState).lastMoveWasColumn == index }.value
            if (rating >= Integer.MAX_VALUE - tree.root.board.HEIGHT) tree.minimaxRequired = false
            if (ConsoleOutput.treeTraversal) {
                println("Rating: $rating Boards: ${tree.leaves.size}")
                println("NextMoves: ${tree.root.joinToString { node -> "[${(node as GameState).lastMoveWasColumn}]: ${node.value}" }}")
            }
        }
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
}

