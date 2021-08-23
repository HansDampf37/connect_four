package bot.bots.tree

import bot.ratingfunctions.RandomRating
import bot.ratingfunctions.ruediger.RuedigerDerBot
import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep
import kotlin.math.abs

class TreeBuilderTest : TestCase() {

    private lateinit var tb: TreeBuilder

    override fun setUp() {
        tb = TreeBuilder(RandomRating(0..100), TreeBuilder.Size.Small)
        val field = tb::class.java.getDeclaredField("tree")
        field.isAccessible = true
        field.set(tb, Tree(GameState(Board(), Token.PLAYER_1)))
    }

    fun testTreeCorrectness() {
        tb.start()
        sleep(2000)
        tb.exit()
        println("checking tree with ${tb.tree.size} nodes")
        for (node in tb.tree) {
            assertTrue(abs(node.board.count { it == Token.PLAYER_1 } -
                    node.board.count { it == Token.PLAYER_2 }) <= 1)
        }
        for (node in tb.tree.filter { !it.isLeaf }) {
            for (child in node as Collection<GameState>) {
                val token = child.board.removeOfColumn(child.lastMoveWasColumn)
                assertEquals(child.board, node.board)
                child.board.throwInColumn(child.lastMoveWasColumn, token)
            }
        }
    }

    fun testResumeStopStepOnRepeat() {
        tb.start()
        val thread = tb::class.java.getDeclaredField("thread")
        thread.isAccessible = true
        while (!tb.tree.root.finished && tb.tree.root.board.stillSpace()) {
            val index = IntRange(0, 6).filter { tb.tree.root.board.stillSpaceIn(it) }.random()
            tb.moveMade(index)
            println(tb.tree.root.board)
            sleep(80)
        }
        tb.exit()
        println(tb.tree)
    }

    fun testStart() {
        val dt: Long = 2000
        val size = tb.tree.size
        tb.start()
        sleep(dt)
        tb.exit()
        assertTrue(tb.tree.size > size)
        println("Tree grew from $size to ${tb.tree.size} nodes in $dt milliseconds")
    }

    fun testPause() {
        tb.start()
        var blocked = true
        Thread {
            run {
                sleep(3000)
                if (blocked) fail("took too long to pause")
            }
        }.start()
        tb.pause()
        blocked = false
        tb.exit()
    }

    fun testMoveMade() {
        for (i in 0 until 3) {
            val t = Tree(3, 3) { _: Int, x: Int, parent: GameState? ->
                if (parent == null) GameState(Board(), Token.PLAYER_1) else GameState(
                    parent.board.clone().apply { throwInColumn(x, parent.nextPlayer) }, parent.nextPlayer.other()
                ).apply { value = (0..100).random() }
            }
            val field = tb::class.java.getDeclaredField("tree")
            field.isAccessible = true
            field.set(tb, t)
            println(t)
            val checkNode = t.root[i]
            assertEquals(40, t.size)
            assertEquals(27, t.leaves.size)
            val pruned = tb.tree.toMutableList()
            tb.moveMade(i)
            pruned.removeAll(tb.tree)
            println(t)
            assertEquals(13, t.size)
            assertTrue(pruned.all { !t.root.isParentOf(it) })
            assertEquals(9, t.leaves.size)
            assertEquals(t.root, checkNode)
        }
    }

    fun testDeepStep() {
        val treeBuilder = TreeBuilder(RandomRating(0..100))
        tb.start()
        for (i in 0 until treeBuilder.tree.root.board.HEIGHT) {
            sleep(100)
            treeBuilder.moveMade(6)
            println(treeBuilder.tree.root.board)
        }
        treeBuilder.exit()
        assertEquals(6, treeBuilder.tree.root.size)
    }

    fun testTreeVarianceAndDepth() {
        for (i in 0 until 2) {
            val treeBuilder = TreeBuilder(RandomRating(0..100), TreeBuilder.Size.Large)
            if (i == 1) treeBuilder.tree.root = GameState(TestUtils.gameProgressed, Token.PLAYER_1)
            treeBuilder.start()
            var oldDepth = -1f
            for (j in 0 until 50) {
                treeBuilder.pause()
                assertTrue(
                    "tree is not growing, before: $oldDepth, now: ${treeBuilder.tree.meanDepth}",
                    treeBuilder.tree.meanDepth >= oldDepth
                )
                oldDepth = treeBuilder.tree.meanDepth
                val varianceInDepth = treeBuilder.tree.varianceInDepth
                //assertTrue("tree is not balanced: $varianceInDepth", varianceInDepth < 0.4)
                println("success in iteration $j, depth: $oldDepth, variance: $varianceInDepth, ${treeBuilder.tree.size()} nodes")
                treeBuilder.resume()
                sleep(2)
            }
        }
    }

    fun testMoveMadeInLargeTree() {
        val treeBuilder = TreeBuilder(RuedigerDerBot(Token.PLAYER_1), TreeBuilder.Size.VerySmall)
        val field = treeBuilder::class.java.getDeclaredField("tree")
        field.isAccessible = true
        field.set(treeBuilder, Tree(GameState(Board(), Token.PLAYER_1)))
        treeBuilder.start()
        sleep(2000)
        treeBuilder.exit()
        val root = treeBuilder.tree.root
        val newRoot = root[3]
        val size = treeBuilder.tree.size
        println("initializing expectations")
        val sizeToBeRemoved = listOf(0, 1, 2, 4, 5, 6).sumOf { treeBuilder.tree.root[it].amountDescendants + 1 } + 1
        val toBeKept =
            HashSet<GameState>(treeBuilder.tree.filter { it.isDescendantOf(treeBuilder.tree.root[3]) }).apply {
                add(
                    treeBuilder.tree.root[3] as GameState
                )
            }
        val toBePruned =
            HashSet<GameState>(treeBuilder.tree.filter { !it.isDescendantOf(treeBuilder.tree.root[3]) }).apply {
                remove(
                    treeBuilder.tree.root[3] as GameState
                )
            }
        println("Prune")
        treeBuilder.moveMade(3)
        assertEquals(newRoot, treeBuilder.tree.root)
        println("Verifying expectations")
        assertTrue(treeBuilder.tree.leaves.all { newRoot.isParentOf(it) })
        assertTrue(treeBuilder.tree.leaves.all { !root.isParentOf(it) })
        assertEquals(size - sizeToBeRemoved, treeBuilder.tree.size)
        assertEquals(toBeKept.size, treeBuilder.tree.size)
        for (n in treeBuilder.tree) {
            toBeKept.remove(n)
            assertFalse(toBePruned.contains(n))
        }
        assertEquals(0, toBeKept.size)
    }
}