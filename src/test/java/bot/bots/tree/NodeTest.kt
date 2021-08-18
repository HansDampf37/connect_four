package bot.bots.tree

import junit.framework.TestCase

class NodeTest : TestCase() {
    private lateinit var node: Node

    public override fun setUp() {
        super.setUp()
        node =
            Node(mutableListOf(
                Node(mutableListOf(),2),
                Node(mutableListOf(
                    Node(value = 4)),
                    3)),
                1)
    }

    fun testIsLeave() {
        assertFalse(node.isLeaf)
        assertFalse(node[1].isLeaf)
        assertTrue(node[0].isLeaf)
        assertTrue(node[1][0].isLeaf)
    }

    fun testGetAmountDescendants() {
        assertEquals(3, node.amountDescendants)
        assertEquals(1, node[1].amountDescendants)
        assertEquals(0, node[0].amountDescendants)
        assertEquals(0, node[1][0].amountDescendants)
    }

    fun testGetChildren() {
        val n = Node()
        val m = Node()
        n.add(m)
        assertEquals(1, n.children.size)
        assertEquals(m, n.children[0])
        n.remove(m)
        assertTrue(n.children.isEmpty())
    }

    fun testIsParentOf() {
        assertTrue(node.isParentOf(node[0]))
        assertTrue(node.isParentOf(node[1]))
        assertTrue(node.isParentOf(node[1][0]))

        assertFalse(node[0].isParentOf(node))
        assertFalse(node[1].isParentOf(node))
        assertFalse(node[1][0].isParentOf(node))
    }
}