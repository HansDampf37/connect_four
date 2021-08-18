package bot.bots.tree

import bot.bots.RatingFunction
import model.Board
import model.Token
import java.lang.Thread.sleep
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class TreeBuilder(val ratingFunction: RatingFunction, private val targetSize: Size = Size.Medium) : Runnable {

    var tree: Tree<GameState> = Tree(GameState(Board(), nextPlayer = Token.PLAYER_1))

    //private var deamonRunning: Boolean = true
    /*private val deamonThread = Thread {
        while (deamonRunning) {
            val size = tree.size
            val status = if (blocking) "blocking" else "running"
            pause()
            println("$size nodes, currently $status, depth ~ ${tree.meanDepth}, variance in depth ~ ${tree.varianceInDepth}")
            start()
            sleep(50)
        }
    }*/
    private var running: Boolean = false
    private var blocking: Boolean = false
    private var started: Boolean = false
    private var exit: Boolean = false
    private val lock: ReentrantLock = ReentrantLock()
    private val continueWork: Condition = lock.newCondition()
    private val workPaused: Condition = lock.newCondition()
    private val readyToStep: Condition = lock.newCondition()

    override fun run() {
        started = true
        running = true
        while (true) {
            tree.leaves.toList().forEach { leaf ->
                if (running) {
                    if (decideIfCalculating()) {
                        val futureStates = leaf.getFutureGameStates()
                        futureStates.forEach {
                            tree.addChild(leaf, it)
                            it.value = ratingFunction(it.board)
                            lock.withLock { readyToStep.signal() }
                        }
                    } else {
                        sleep(50)
                    }
                } else {
                    lock.withLock {
                        blocking = true
                        workPaused.signal()
                        while (!running) continueWork.await()
                        blocking = false
                    }
                }
                if (exit) {
                    shutdown()
                    return@run
                }
            }
        }
    }

    private fun decideIfCalculating(): Boolean {
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

    private fun shutdown() {
        println("exiting")
        started = false
        running = false
    }

    fun start() {
        running = true
        lock.withLock { continueWork.signal() }
    }

    fun pause() {
        if (!running || !started) return
        lock.withLock {
            running = false
            while (!blocking) workPaused.await()
        }
    }

    fun exit() {
        exit = true
        start()
    }

    fun moveMade(move: Int) {
        while (tree.root.size <= move) lock.withLock { readyToStep.await() }
        pause()
        tree.step(move)
        start()
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