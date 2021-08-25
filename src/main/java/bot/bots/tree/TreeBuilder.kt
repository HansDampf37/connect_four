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

class TreeBuilder(val ratingFunction: RatingFunction, private val targetSize: Size = Size.Medium) : Runnable {

    val state: Thread.State get() = if (!this::thread.isInitialized) Thread.State.NEW else thread.state
    val tree: Tree<GameState> = Tree(GameState(Board(), nextPlayer = Token.PLAYER_1))
    private var running = true
    val lock: ReentrantLock = ReentrantLock()
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
                if (decideIfCalculating()) {
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
                    // if the scheduler decides, we pause for 50 ms and signal that enough futureStates have been created
                    if (Math.random() > 0.99) println("Tree full (${tree.size})")
                    lock.withLock { readyToStep.signal() }
                    sleep(50)
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
        GlobalScope.launch {
            run()
        }
    }

    private fun decideIfCalculating(): Boolean {
        if (IntRange(0, 6).filter { index -> tree.root.board.stillSpaceIn(index) }
                .any { index -> !tree.root.any { (it as GameState).lastMoveWasColumn == index } }) return true
        if (tree.size < 0.9 * targetSize.getSize()) return true
        when {
            tree.size >= targetSize.getSize() -> {
                return false
            }
            tree.size >= 0.95 * targetSize.getSize() -> {
                if (Math.random() > 0.3) sleep(2)
            }
            tree.size >= 0.9 * targetSize.getSize() -> {
                if (Math.random() > 0.9) sleep(2)
            }
        }
        return true
    }


    fun exit() {
        running = false
        if (this::thread.isInitialized) thread.join()
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