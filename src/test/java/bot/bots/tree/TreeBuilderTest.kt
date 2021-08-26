package bot.bots.tree

import TestUtils
import bot.ratingfunctions.ruediger.RuedigerDerBot
import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep
import kotlin.concurrent.withLock
import kotlin.math.abs

class TreeBuilderTest : TestCase() {

    private lateinit var tb: TreeBuilder

    private fun runTreeBuilder(dt: Long) {
        tb.start()
        assertEquals(Thread.State.RUNNABLE, tb.state)
        sleep(dt)
        tb.exit()
        assertEquals(Thread.State.TERMINATED, tb.state)
    }

    override fun setUp() {
        tb = TreeBuilder(RuedigerDerBot(Token.PLAYER_1), TreeBuilder.SizeScheduler(TreeBuilder.SizeScheduler.Size.Large))
        val field = tb::class.java.getDeclaredField("tree")
        field.isAccessible = true
        field.set(tb, Tree(GameState(Board(), Token.PLAYER_1)))
    }

    fun testStartAfterExit() {
        assertEquals(1, tb.tree.size())
        runTreeBuilder(10)
        val size = tb.tree.size()
        runTreeBuilder(100)
        assertTrue("after last invocation of exit() the tree-size was $size and now it is ${tb.tree.size()}", tb.tree.size() > size)
    }

    fun testTreeCorrectness() {
        runTreeBuilder(2000)
        println("checking tree with ${tb.tree.size()} nodes")
        for (node in tb.tree) {
            assertTrue(abs(node.board.count { it == Token.PLAYER_1 } -
                    node.board.count { it == Token.PLAYER_2 }) <= 1)
        }
        for (node in tb.tree.filter { !it.isLeaf }) {
            for (child in node) {
                val token = child.board.removeOfColumn(child.lastMoveWasColumn)
                assertEquals(child.board, node.board)
                child.board.throwInColumn(child.lastMoveWasColumn, token)
            }
        }
        tb.tree.leaves.forEach { assertEquals(RuedigerDerBot(Token.PLAYER_1).invoke(it.board), it.value) }
    }

    fun testResumeStopStepOnRepeat() {
        tb.start()
        assert(tb.state == Thread.State.RUNNABLE)
        while (!tb.tree.root.finished && tb.tree.root.board.stillSpace()) {
            val index = IntRange(0, 6).filter { tb.tree.root.board.stillSpaceIn(it) }.random()
            tb.moveMade(index)
            println(tb.tree.root.board)
            sleep(80)
        }
        tb.exit()
        println(tb.tree)
        println(tb.tree.size())
    }

    fun testIfGrowing() {
        val dt = 2000L
        val size = tb.tree.size()
        runTreeBuilder(dt)
        assertTrue(tb.tree.size() > size)
        println("Tree grew from $size to ${tb.tree.size()} nodes in $dt milliseconds")
    }

    fun testMoveMade() {
        for (i in 0 until 3) {
            val t = Tree(3, 3) { _: Int, x: Int, parent: GameState? ->
                if (parent == null)
                    GameState(Board(), Token.PLAYER_1, -1)
                else
                    GameState(
                        parent.board.clone().apply { throwInColumn(x, parent.nextPlayer) },
                        parent.nextPlayer.other(),
                        x).apply { value = (0..100).random() }
            }
            val field = tb::class.java.getDeclaredField("tree")
            field.isAccessible = true
            field.set(tb, t)
            println(t)
            val checkNode = t.root[i]
            assertEquals(40, t.size())
            assertEquals(27, t.leaves.size)
            val pruned = tb.tree.toMutableList()
            tb.moveMade(i)
            pruned.removeAll(tb.tree)
            println(t)
            assertEquals(13, t.size())
            assertTrue(pruned.all { !t.root.isParentOf(it) })
            assertEquals(9, t.leaves.size)
            assertEquals(t.root, checkNode)
        }
    }

    fun testDeepStep() {
        tb.start()
        repeat (tb.tree.root.board.HEIGHT) {
            tb.moveMade(6)
            println(tb.tree.root.board)
            sleep(100)
        }
        tb.exit()
        assertEquals(6, tb.tree.root.size)
    }

    fun testTreeVarianceAndDepth() {
        for (i in 0 until 2) {
            if (i == 1) tb.tree.root = GameState(TestUtils.gameProgressed, Token.PLAYER_1)
            tb.start()
            var oldDepth = -1f
            for (j in 0 until 50) {
                if (tb.isIdle) break
                tb.lock.withLock {
                    assertTrue(
                        "tree is not growing, before: $oldDepth, now: ${tb.tree.avgDepth}",
                        tb.tree.avgDepth >= oldDepth
                    )
                    oldDepth = tb.tree.avgDepth
                    val varianceInDepth = tb.tree.varianceInDepth
                    //assertTrue("tree is not balanced: $varianceInDepth", varianceInDepth < 0.4)
                    println("success in iteration $j, depth: $oldDepth, variance: $varianceInDepth, ${tb.tree.size()} nodes")
                }
                sleep(2)
            }
        }
    }

    fun testMoveMadeInLargeTree() {
        runTreeBuilder(2000)
        val root = tb.tree.root
        val newRoot = root[3]
        val size = tb.tree.size()
        println("initializing expectations")
        val sizeToBeRemoved = listOf(0, 1, 2, 4, 5, 6).sumOf { tb.tree.root[it].amountDescendants + 1 } + 1
        val toBeKept =
            HashSet<GameState>(tb.tree.filter { it.isDescendantOf(tb.tree.root[3]) }).apply {
                add(
                    tb.tree.root[3]
                )
            }
        val toBePruned =
            HashSet<GameState>(tb.tree.filter { !it.isDescendantOf(tb.tree.root[3]) }).apply {
                remove(
                    tb.tree.root[3]
                )
            }
        println("Prune")
        tb.moveMade(3)
        assertEquals(newRoot, tb.tree.root)
        println("Verifying expectations")
        assertTrue(tb.tree.leaves.all { newRoot.isParentOf(it) })
        assertTrue(tb.tree.leaves.all { !root.isParentOf(it) })
        assertEquals(size - sizeToBeRemoved, tb.tree.size())
        assertEquals(toBeKept.size, tb.tree.size())
        for (n in tb.tree) {
            toBeKept.remove(n)
            assertFalse(toBePruned.contains(n))
        }
        assertEquals(0, toBeKept.size)
    }
}