package bot.tree

import bot.GameState
import model.Board
import model.Token
import java.lang.Thread.sleep
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Test : Runnable {
    var running = true
    var exit = false
    val tree = Tree(GameState(Board(), Token.PLAYER_1))
    private val lock: ReentrantLock = ReentrantLock()
    private val continueWork: Condition = lock.newCondition()
    private val workPaused: Condition = lock.newCondition()

    override fun run() {
        println("Working...")
        while (true) {
            for (leaf in tree.leaves.filter { !it.finished }) {
                if (running) {
                    sleep(500)
                    val futureStates = leaf.getFutureGameStates()
                    futureStates.forEach { tree.addChild(leaf, it) }
                } else {
                    println("Stopping...")
                    lock.withLock {
                        workPaused.signal()
                        continueWork.await()
                    }
                    println("Resuming...")
                }
                if (exit) {
                    println("exiting")
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

        lock.withLock {
            running = false
            workPaused.await()
        }
    }

    fun exit() {
        exit = true
        start()
    }
}

fun main() {
    val test = Test()
    val thread = Thread(test)
    thread.start()
    for (i in 0..100) {
        test.start()
        sleep(400)
        test.pause()
        sleep(200)
    }
    test.start()
    sleep(5000)
    println("pause requested")
    test.pause()
    sleep(2000)
    test.start()
    sleep(4000)
    test.exit()
    thread.join()
}