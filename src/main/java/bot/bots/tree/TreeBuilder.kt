package bot.bots.tree

import bot.bots.RatingFunction
import model.Board
import model.Token
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentLinkedQueue

class TreeBuilder(
    val ratingFunction: RatingFunction,
    private val scheduler: Scheduler = SizeScheduler(SizeScheduler.Size.Medium)
) : Runnable {

    val state: Thread.State get() = if (!this::thread.isInitialized) Thread.State.NEW else thread.state
    val tree: Tree<GameState> = Tree(GameState(Board(), nextPlayer = Token.PLAYER_1))
    val lock: ReentrantLock = ReentrantLock()

    private var running = true
    private val readyToStep: Condition = lock.newCondition()
    private lateinit var thread: Thread
    private val queue: Queue<GameState> = ConcurrentLinkedQueue()

    override fun run() {
        queue.clear()
        tree.leaves.forEach { queue.add(it) }
        while (running) {
            if (queue.isEmpty()) {
                running = false
                return
            }
            lock.withLock {
                if (scheduler.expand(tree)) {
                    // val burstSize = 10
                    // val nextBurstsLeave = Array(burstSize) {queue.remove()}
                    val leaf = queue.remove()
                    // if the scheduler decides, we add up to 7 future states to tree
                    val futureStates = leaf.getFutureGameStates()
                    futureStates.forEach { future ->
                        // each futureState gets added to the tree and evaluated
                        tree.addChild(leaf, future)
                        future.value = ratingFunction(future.board)
                        // enqueue newly created futureStates
                        queue.add(future)
                        // signal that new futureState has been created
                        readyToStep.signal()
                    }
                } else {
                    // if the scheduler decides, we signal that enough futureStates have been created
                    lock.withLock { readyToStep.signal() }
                }
            }
        }
    }

    fun moveMade(move: Int) {
        lock.withLock { while (!tree.root.any { (it as GameState).lastMoveWasColumn == move }) readyToStep.await() }
        lock.withLock {
            tree.step(move)
            queue.clear()
            for (leaf in tree.leaves) queue.add(leaf)
        }
    }

    fun start() {
        thread = Thread(this)
        thread.start()
    }

    fun exit() {
        running = false
        if (this::thread.isInitialized) thread.join()
    }

    interface Scheduler {
        /**
         * returns true if and only if the tree-builder shall continue to expand the [tree]. Depending on the trees properties
         * this method may use [sleep] to block further execution
         */
        fun <T : Node> expand(tree: Tree<T>): Boolean
    }

    class SizeScheduler(private val targetSize: Size) : Scheduler {
        override fun <T : Node> expand(tree: Tree<T>): Boolean {
            // if the root node has un-calculated legal moves always calculate
            if (IntRange(0, 6).filter { index -> (tree.root as GameState).board.stillSpaceIn(index) }
                    .any { index -> !tree.root.any { (it as GameState).lastMoveWasColumn == index } }) return true
            // return depending on size
            return when {
                tree.size < 0.9 * targetSize.getSize() -> {
                    true
                }
                tree.size < 0.95 * targetSize.getSize() -> {
                    if (Math.random() > 0.9) sleep(2)
                    true
                }
                tree.size < targetSize.getSize() -> {
                    if (Math.random() > 0.3) sleep(2)
                    true
                }
                else -> {
                    sleep(10)
                    return false
                }
            }
        }

        enum class Size {
            VeryLarge {
                override fun getSize() = 5000000
            },
            Large {
                override fun getSize() = 200000
            },
            Medium {
                override fun getSize() = 100000
            },
            Small {
                override fun getSize() = 50000
            },
            VerySmall {
                override fun getSize() = 10000
            };

            abstract fun getSize(): Int
        }
    }
}