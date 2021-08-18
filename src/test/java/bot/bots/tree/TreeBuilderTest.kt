package bot.bots.tree

import bot.ratingfunctions.RandomRating
import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep

class TreeBuilderTest : TestCase() {

    private lateinit var tb: TreeBuilder

    override fun setUp() {
        tb = TreeBuilder(RandomRating(0..100))
    }

    fun testTreeCorrectness() {
        val thread = Thread(tb)
        thread.start()
        sleep(2000)
        tb.exit()
        println("checking tree with ${tb.tree.size} nodes")
        for (node in tb.tree.filter { !it.isLeaf }) {
            for (i in node.indices) {
                node.board.throwInColumn(i, node.nextPlayer)
                assertEquals((node[i] as GameState).board, node.board)
                node.board.removeOfColumn(i)
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

    fun testMoveMadeInLargeTree() {
        val thread = Thread(tb)
        thread.start()
        sleep(5000)
        tb.exit()
        thread.join()
        val root = tb.tree.root
        val newRoot = root[3]
        val size = tb.tree.size
        val sizeToBeRemoved = listOf(0, 1, 2, 4, 5, 6).sumOf { i -> Tree(root = root[i]).size } + 1
        val toBeKept = HashSet<GameState>(tb.tree.filter { it.isDescendantOf(tb.tree.root[3]) }).apply { add(tb.tree.root[3] as GameState) }
        val toBePruned = HashSet<GameState>(tb.tree.filter { !it.isDescendantOf(tb.tree.root[3]) }).apply { remove(tb.tree.root[3] as GameState) }
        tb.moveMade(3)
        assertEquals(newRoot, tb.tree.root)
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