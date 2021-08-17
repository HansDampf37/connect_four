package bot.tree

import bot.GameState
import model.Token
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class TreeBuilder : Runnable {
    private var running: Boolean = true
    private var exit: Boolean = false
    private val lock = ReentrantLock()
    private val lock2 = ReentrantLock()
    private val waitingLock: Condition = lock.newCondition()
    private val waitingConfirmedLock: Condition = lock2.newCondition()
    var tree: Tree<GameState> = Tree(GameState(nextPlayer = Token.PLAYER_1))

    override fun run() {
        start()
        while (true) {
            for (leaf in tree.leaves.filter{ !it.finished }) {
                if (!running) {
                    lock.withLock {
                        lock2.withLock { waitingConfirmedLock.signal() }
                        println("Blocked...")
                        waitingLock.await()
                        println("Unblocked...")
                    }
                    break
                }
                println("Working...")
                val futureStates = leaf.getFutureGameStates()
                futureStates.forEach { tree.addChild(leaf, it) }
                if (exit) return
            }
        }
    }

    fun start() {
        running = true
        lock.withLock {
            waitingLock.signal()
        }
    }

    fun pause() {
        lock2.withLock {
            running = false
            waitingConfirmedLock.await()
        }
    }

    fun stop() {
        exit = true
    }

    fun moveMade(move: Int) {
        pause()
        tree.step(move)
        start()
    }
}