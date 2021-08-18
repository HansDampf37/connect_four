package bot.bots.tree

import bot.bots.RatingFunction
import model.Board
import model.Token
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class TreeBuilder(val ratingFunction: RatingFunction) : Runnable {
    var tree: Tree<GameState> = Tree(GameState(Board(), nextPlayer = Token.PLAYER_1))
    private var running: Boolean = false
    private var started: Boolean = false
    private var exit: Boolean = false
    private val lock: ReentrantLock = ReentrantLock()
    private val continueWork: Condition = lock.newCondition()
    private val workPaused: Condition = lock.newCondition()

    override fun run() {
        started = true
        running = true
        println("working")
        while (true) {
            tree.leaves.toList().forEach { leaf ->
                if (running) {
                    val futureStates = leaf.getFutureGameStates()
                    futureStates.forEach {
                        tree.addChild(leaf, it)
                        it.value = ratingFunction(it.board)
                    }
                } else {
                    lock.withLock {
                        workPaused.signal()
                        println("blocking")
                        continueWork.await()
                        println("resuming")
                    }
                }
                if (exit) {
                    println("exiting")
                    started = false
                    running = false
                    return
                }
            }
        }
    }

    fun start() {
        running = true
        lock.withLock { continueWork.signal() }
    }

    fun pause() {
        if (!running || !started) return
        lock.withLock {
            running = false
            workPaused.await()
        }
    }

    fun exit() {
        exit = true
        start()
    }

    fun moveMade(move: Int) {
        pause()
        tree.step(move)
        start()
    }
}