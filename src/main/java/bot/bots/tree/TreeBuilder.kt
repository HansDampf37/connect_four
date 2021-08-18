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
            if (decideIfCalculating()) {
                val leaf = tree.leaves.first()
                if (running) {
                    val futureStates = leaf.getFutureGameStates()
                    futureStates.forEach {
                        tree.addChild(leaf, it)
                        it.value = ratingFunction(it.board)
                        lock.withLock { readyToStep.signal() }
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
                    return
                }
            }
        }
    }

    private fun decideIfCalculating(): Boolean {
        when {
            tree.size >= targetSize.getSize() -> {
                sleep(50)
                return false
            }
            tree.size >= 0.9 * targetSize.getSize() -> {
                sleep(50)
            }
            tree.size >= 0.7 * targetSize.getSize() -> {
                sleep(35)
            }
            tree.size >= 0.5 * targetSize.getSize() -> {
                sleep(5)
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
            override fun getSize() = 200000
        },
        Large {
            override fun getSize() = 100000
        },
        Medium {
            override fun getSize() = 50000
        },
        Small {
            override fun getSize() = 20000
        },
        VerySmall {
            override fun getSize() = 2000
        };

        abstract fun getSize(): Int
    }
}