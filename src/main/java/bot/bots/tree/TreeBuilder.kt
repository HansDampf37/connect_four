package bot.bots.tree

import bot.bots.RatingFunction
import model.Board
import model.Token
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class TreeBuilder(val ratingFunction: RatingFunction, private val targetSize: Size = Size.Medium) : Runnable {

    val tree: Tree<GameState> = Tree(GameState(Board(), nextPlayer = Token.PLAYER_1))

    private val lock: ReentrantLock = ReentrantLock()
    private val continueWork: Condition = lock.newCondition()
    private val workPaused: Condition = lock.newCondition()
    private val readyToStep: Condition = lock.newCondition()
    private val thread = Thread(this)
    private var treeBuilderState: StateOfTreeBuilder = StateOfTreeBuilder.Ready
    private var masterState: StateOfMaster = StateOfMaster.Running

    private val queue: Queue<GameState> = LinkedBlockingQueue()

    override fun run() {
        treeBuilderState = StateOfTreeBuilder.Running
        for (leaf in tree.leaves) queue.add(leaf)
        while (true) {
            if (treeBuilderState == StateOfTreeBuilder.Running) {
                assert(masterState == StateOfMaster.Running)
                if (decideIfCalculating()) {
                    if (queue.isEmpty()) {
                        println("Slave -> Master i finished all my tasks ill stop now")
                        treeBuilderState = StateOfTreeBuilder.Terminated
                        return
                    }
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
                        lock.withLock { readyToStep.signal() }
                    }
                    if (Math.random() > 0.9999) println("Slave -> Master I am still expanding the tree")
                } else {
                    // if the scheduler decides, we pause for 50 ms and signal that enough futureStates have been created
                    println("Tree full")
                    lock.withLock { readyToStep.signal() }
                    sleep(50)
                }
            } else if (treeBuilderState == StateOfTreeBuilder.Blocking) {
                // blocks the current thread until it gets signal to continue its work
                assert(masterState == StateOfMaster.WaitingForBlock)
                lock.withLock {
                    masterState = StateOfMaster.Running
                    println("Slave -> Master I stopped working")
                    workPaused.signal()
                    println("Slave -> Master tell me when i shall continue to work")
                    treeBuilderState = StateOfTreeBuilder.Blocking
                    while (treeBuilderState == StateOfTreeBuilder.Blocking) {
                        continueWork.await()
                        if (treeBuilderState == StateOfTreeBuilder.Blocking) println("Slave -> Master i got woken up bot should still sleep because of state = $treeBuilderState")
                        // TODO somehow treeBuilderState is blocking when await gets called
                    }
                    println("Slave -> Master ill continue now")
                }
            } else if (treeBuilderState == StateOfTreeBuilder.Terminated) {
                println("Master -> Slave you can quit for today")
                return
            }
        }
    }

    fun start() {
        thread.start()
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

    fun resume() {
        assert(treeBuilderState != StateOfTreeBuilder.Running)
        if (treeBuilderState == StateOfTreeBuilder.Terminated) return
        lock.withLock {
            println("Master -> Slave resume working")
            treeBuilderState = StateOfTreeBuilder.Running
            continueWork.signal()
        }
    }

    fun pause() {
        assert(treeBuilderState != StateOfTreeBuilder.Blocking)
        if (treeBuilderState == StateOfTreeBuilder.Terminated) return
        lock.withLock {
            if (treeBuilderState != StateOfTreeBuilder.Running) return
            println("Master -> Slave pause working")
            // blocks the thread
            treeBuilderState = StateOfTreeBuilder.Blocking
            // waits for the thread to unblock him
            masterState = StateOfMaster.WaitingForBlock
            println("Master -> Slave tell me when you can pause")
            while (masterState == StateOfMaster.WaitingForBlock) workPaused.await()
            println("Master -> ill continue working on my task")
        }
    }

    fun exit() {
        treeBuilderState = StateOfTreeBuilder.Terminated
        resume()
        thread.join()
    }

    fun moveMade(move: Int) {
        while (!tree.root.any { (it as GameState).lastMoveWasColumn == move }) {
            println(
                "Critical: Tree generation is too slow, want to make move $move but board \n${tree.root.board}\nhas no future states"
            )
            lock.withLock { readyToStep.await() }
            println(
                "after being woken up again children for root \n${tree.root.board}\nare \n${
                    (tree.root as Collection<GameState>).joinToString("\n-----------------\n") { it.board.toString() }
                }"
            )
        }
        pause()
        tree.step(move)
        queue.clear()
        for (leaf in tree.leaves) queue.add(leaf)
        resume()
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

    private enum class StateOfTreeBuilder {
        Ready,
        Running,
        Blocking,
        Terminated
    }

    private enum class StateOfMaster {
        WaitingForBlock,
        Running,
    }
}