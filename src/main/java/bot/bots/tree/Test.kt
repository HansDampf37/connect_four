package bot.bots.tree

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import model.Board
import model.Token
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Test : Runnable {
    val lock: ReentrantLock = ReentrantLock()
    val counter = AtomicInteger(0)

    private var running = true
    private val numThreads = 14
    private lateinit var thread: Thread

    override fun run() {
        while (running) {
            lock.withLock {
                runBlocking {
                    val futures = ArrayList<Deferred<Any>>()
                    repeat (numThreads) {
                        futures.add(async (Dispatchers.IO) {
                            counter.incrementAndGet()
                        })
                    }
                    for (f in futures) {
                        f.await()
                    }
                    println("finished ${counter.toInt() / numThreads}")
                }
            }
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
}

fun main() {
    val test = Test()
    test.start()
    sleep(3000)
    test.lock.withLock {
        val controll = test.counter.toInt()
        println("I want to calculate something")
        sleep(1000)
        println("finished calculations")
        assert(controll == test.counter.toInt())
    }
    sleep(2000)
    test.exit()
}