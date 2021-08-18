package bot.bots.tree

import bot.ratingfunctions.RandomRating
import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep
import kotlin.math.abs

class TreeBuilderTest : TestCase() {

    private lateinit var tb: TreeBuilder

    override fun setUp() {
        tb = TreeBuilder(RandomRating(0..100), TreeBuilder.Size.Small)
        tb.tree = Tree(GameState(Board(), Token.PLAYER_1))
    }

    fun testTreeCorrectness() {
        val thread = Thread(tb)
        thread.start()
        sleep(2000)
        tb.exit()
        println("checking tree with ${tb.tree.size} nodes")
        for (node in tb.tree) {
            assertTrue(abs(node.board.count { it == Token.PLAYER_1 } -
                    node.board.count { it == Token.PLAYER_2 }) <= 1)
        }
        for (node in tb.tree.filter { !it.isLeaf }) {
            for (child in node as Collection<GameState>) {
                assertTrue(((0 until 7).toList().any {
                    val token = child.board.removeOfColumn(it)
                    val result = child.board == node.board
                    child.board.throwInColumn(it, token)
                    result
                }))
            }
        }
    }

    fun testStart() {
        val dt: Long = 2000
        val size = tb.tree.size
        val thread = Thread(tb)
        thread.start()
        sleep(dt)
        tb.exit()
        assertTrue(tb.tree.size > size)
        println("Tree grew from $size to ${tb.tree.size} nodes in $dt milliseconds")
        thread.join()
    }

    fun testPause() {
        val thread = Thread(tb).apply { start() }
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
        thread.join()
    }

    fun testMoveMade() {
        for (i in 0 until 3) {
            val t = Tree(3, 3) { _: Int, x: Int, parent: GameState? ->
                if (parent == null) GameState(Board(), Token.PLAYER_1) else GameState(
                    parent.board.clone().apply { throwInColumn(x, parent.nextPlayer) }, parent.nextPlayer.other()
                ).apply { value = (0..100).random() }
            }
            tb.tree = t
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
        val thread = Thread(treeBuilder)
        thread.start()
        for (i in 0 until treeBuilder.tree.root.board.HEIGHT) {
            sleep(100)
            treeBuilder.moveMade(6)
            println(treeBuilder.tree.root.board)
        }
        treeBuilder.exit()
        assertEquals(6, treeBuilder.tree.root.size)
        thread.join()
    }

    fun testTreeVarianceAndDepth() {
        for (i in 0 until 2) {
            val treeBuilder = TreeBuilder(RandomRating(0..100), TreeBuilder.Size.Large)
            if (i == 1) treeBuilder.tree.root = GameState(TestUtils.gameProgressed, Token.PLAYER_1)
            val thread = Thread(treeBuilder)
            thread.start()
            var oldDepth = 0f
            for (j in 0 until 20) {
                treeBuilder.pause()
                assertTrue(
                    "tree is not growing, before: $oldDepth, now: ${treeBuilder.tree.meanDepth}",
                    treeBuilder.tree.meanDepth >= oldDepth
                )
                oldDepth = treeBuilder.tree.meanDepth
                val varianceInDepth = treeBuilder.tree.varianceInDepth
                //assertTrue("tree is not balanced: $varianceInDepth", varianceInDepth < 0.4)
                println("success in iteration $j, depth: $oldDepth, variance: $varianceInDepth, ${treeBuilder.tree.size} nodes")
                treeBuilder.start()
                sleep(200)
            }
        }
    }

    fun testMoveMadeInLargeTree() {
        val thread = Thread(tb)
        tb.tree = Tree(GameState(Board(), Token.PLAYER_1))
        thread.start()
        sleep(5000)
        tb.exit()
        thread.join()
        val root = tb.tree.root
        val newRoot = root[3]
        val size = tb.tree.size
        println("initializing expectations")
        val sizeToBeRemoved = listOf(0, 1, 2, 4, 5, 6).sumOf { i -> Tree(root = root[i]).size } + 1
        val toBeKept =
            HashSet<GameState>(tb.tree.filter { it.isDescendantOf(tb.tree.root[3]) }).apply { add(tb.tree.root[3] as GameState) }
        val toBePruned =
            HashSet<GameState>(tb.tree.filter { !it.isDescendantOf(tb.tree.root[3]) }).apply { remove(tb.tree.root[3] as GameState) }
        println("Prune")
        tb.moveMade(3)
        assertEquals(newRoot, tb.tree.root)
        println("Verifying expectations")
        assertTrue(tb.tree.leaves.all { newRoot.isParentOf(it) })
        assertTrue(tb.tree.leaves.all { !root.isParentOf(it) })
        assertEquals(size - sizeToBeRemoved, tb.tree.size)
        assertEquals(toBeKept.size, tb.tree.size)
        for (n in tb.tree) {
            toBeKept.remove(n)
            assertFalse(toBePruned.contains(n))
        }
        assertEquals(0, toBeKept.size)
    }
}