package bot.bots.tree

import bot.bots.RatingFunction
import kotlinx.coroutines.*
import model.Board
import model.Token
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.NoSuchElementException
import kotlin.concurrent.withLock


class TreeBuilder(
    val ratingFunction: RatingFunction,
    private val scheduler: Scheduler = SizeScheduler(SizeScheduler.Size.VerySmall)
) : Runnable {

    val state: Thread.State get() = if (!this::thread.isInitialized) Thread.State.NEW else thread.state
    val tree: Tree<GameState> = Tree(GameState(Board(), nextPlayer = Token.PLAYER_1))
    val lock: ReentrantLock = ReentrantLock()
    val add: ReentrantLock = ReentrantLock()
    val isIdle get() = run { lock.withLock { return@run !scheduler.expand(tree) } }

    var running = true
    private val numThreads = 7
    private val readyToStep: Condition = lock.newCondition()
    private lateinit var thread: Thread
    private val queue: Queue<GameState> = ConcurrentLinkedQueue()

    override fun run() {
        queue.clear()
        lock.withLock { tree.leaves.forEach { queue.add(it) } }
        while (running) {
            try {
                lock.withLock {
                    if (scheduler.expand(tree)) {
                        runBlocking {
                            val futures = ArrayList<Deferred<Any>>()
                            lock.withLock {
                                repeat(numThreads) {
                                    futures.add(async(Dispatchers.IO) {
                                        repeat(4) {
                                            if (queue.isNotEmpty()) {
                                                val leaf = queue.remove()
                                                // if the scheduler decides, we add up to 7 future states to tree
                                                val futureStates = leaf.getFutureGameStates()
                                                futureStates.forEach { newChild ->
                                                    // each futureState gets added to the tree and evaluated
                                                    tree.addChild(leaf, newChild)
                                                    newChild.value = ratingFunction(newChild.board)
                                                    // enqueue newly created futureStates
                                                    queue.add(newChild)
                                                }
                                            }
                                        }
                                    })
                                }
                            }
                            futures.forEach { it.await() }
                        }
                    }
                    // signal that new futureStates have been created
                    readyToStep.signal()
                }
            } catch (e: NoSuchElementException) {
                e.printStackTrace()
                // if the queue is empty the tree is completely calculated
                assert(tree.root.board.winner != Token.EMPTY || !tree.root.board.stillSpace())
                // run ABPruning one last time
                lock.withLock { AlphaBetaPruning.run(tree) }
                tree.minimaxRequired = false
                //exit tree building
                println("Queue is empty")
                running = false
                return
            }
        }
    }

    fun moveMade(move: Int) {
        while (tree.root.none { (it as GameState).lastMoveWasColumn == move }) lock.withLock { readyToStep.await() }
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
            val size = tree.size()
            return when {
                size < 0.9 * targetSize.getSize() -> {
                    true
                }
                /*size < 0.95 * targetSize.getSize() -> {
                    if (Math.random() > 0.9) sleep(1)
                    true
                }
                size < targetSize.getSize() -> {
                    if (Math.random() > 0.3) sleep(1)
                    true
                }*/
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

    class DepthScheduler(private val targetDepth: Int = 5) : Scheduler {
        override fun <T : Node> expand(tree: Tree<T>): Boolean {
            // if the root node has un-calculated legal moves always calculate
            if (IntRange(0, 6).filter { index -> (tree.root as GameState).board.stillSpaceIn(index) }
                    .any { index -> !tree.root.any { (it as GameState).lastMoveWasColumn == index } }) return true
            // return depending on depth
            val depthFactor = when {
                tree.leaves.size > 2000 -> 1.0f
                tree.leaves.size > 1000 -> 2.0f
                tree.leaves.size > 500 -> 4.0f
                else -> 10.0f
            }
            return when {
                tree.avgDepth < 0.9f * targetDepth * depthFactor -> {
                    true
                }
                tree.avgDepth < 0.95 * targetDepth * depthFactor -> {
                    if (Math.random() > 0.9) sleep(2)
                    true
                }
                tree.avgDepth < targetDepth * depthFactor -> {
                    if (Math.random() > 0.3) sleep(2)
                    true
                }
                else -> {
                    sleep(10)
                    return false
                }
            }
        }
    }

    class SimpleScheduler(private val targetSize: Int = 10000) : Scheduler {
        override fun <T : Node> expand(tree: Tree<T>): Boolean {
            return tree.leaves.size <= targetSize
        }
    }
}