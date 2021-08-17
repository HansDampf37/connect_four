package bot.tree

import junit.framework.TestCase
import kotlin.math.pow
import kotlin.test.assertTrue

class TreeTest : TestCase() {
    private val spread = 3
    private val depth = 3
    private val tree: Tree<Node> =
        Tree(
            depth,
            spread,
        ) { _: Int, _: Int, _: Node? -> Node(value = (0..100).random()) }

    fun testAddChild() {
        val n = Node()
        val m = Node()
        val t = Tree(n)
        assertTrue(n.isLeaf)
        assertTrue(t.leaves.contains(n))
        assertTrue(t.leaves.size == 1)
        t.addChild(n, m)
        assertTrue(n.isParentOf(m))
        assertTrue(n.children.contains(m))
        assertFalse(n.isLeaf)
        assertFalse(t.leaves.contains(n))
        assertTrue(m.isLeaf)
        assertTrue(t.leaves.contains(m))
        assertTrue(t.leaves.size == 1)
        t.removeChild(n, m)
        assertFalse(n.isParentOf(m))
        assertFalse(n.children.contains(m))
        assertTrue(n.isLeaf)
        assertTrue(t.leaves.contains(n))
        assertTrue(t.leaves.size == 1)
    }

    fun testMakeLeaf() {
        val n = Node()
        val m = Node()
        val t = Tree(n)
        t.addChild(n, m)
        assertTrue(n.isParentOf(m))
        assertTrue(n.children.contains(m))
        t.removeChild(n, m)
        assertFalse(n.isParentOf(m))
        assertFalse(n.children.contains(m))
    }

    fun testDFS() {
        var actualAmount = 0
        for (n in tree) {
            actualAmount++
        }
        var expectedAmount = 0
        for (j in 0..spread) expectedAmount += spread.toDouble().pow(j.toDouble()).toInt()
        assertEquals(actualAmount, expectedAmount)
    }

    fun testGetSize() {
        assertEquals(IntRange(0, depth).sumOf { spread.toFloat().pow(it).toInt() }, tree.size)
    }

    fun testLeaves() {
        assertEquals(tree.leaves.size, spread.toFloat().pow(depth).toInt())
        tree.leaves.forEach { assertTrue { tree.root.isParentOf(it) } }
        val t = Tree(0, 0) { _: Int, _: Int, _: Node? -> Node(value = (0..100).random()) }
        assertTrue { t.leaves.contains(t.root) }
        assertEquals(1, t.leaves.size)
        t.addChild(t.root, Node())
        assertTrue { t.leaves.contains(t.root[0]) }
        assertEquals(1, t.leaves.size)
        t.addChild(t.root, Node())
        assertTrue { t.leaves.contains(t.root[1]) }
        assertEquals(2, t.leaves.size)
    }
}
