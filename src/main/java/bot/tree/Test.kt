package bot.tree

import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Test {
    val lock : ReentrantLock = ReentrantLock()
    val cond = lock.newCondition()

    fun main() {
        Thread{
            lock.withLock {
                sleep(2000)
                print("ich will zuerst")
            }
        }.start()
        Thread{
            sleep(1000)
            lock.withLock {
                print("Hallo Welt")
                cond.signal()
            }
        }.start()
    }
}

fun main() = Test().main()