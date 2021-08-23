package bot.bots.tree

import junit.framework.TestCase

class MinimaxTest : TestCase() {
    private lateinit var node: Node

    public override fun setUp() {
        super.setUp()
        node =
            Node(mutableListOf(
                Node(mutableListOf(),3),
                Node(mutableListOf(
                    Node(value = 4)),
                    2)),
                1)
    }

    fun testMinimax() {
        println(Tree(node).toString())
        assertEquals(1, Minimax.run(Tree(node)))
    }

    fun testSameResultAsAlphaBeta() {
        for (i in 0..20) {
            val t = Tree(listOf(4, 5).random(), listOf(2, 3).random(), Node())  { _: Int, _: Int, _: Node? -> Node() }
            t.leaves.forEach{ it.value = IntRange(0, 100).random()}
            val indexMini = Minimax.run(t)
            val valueMini = t.root.value
            val indexAlpha = AlphaBetaPruning.run(t)
            val valueAlpha = t.root.value
            assertEquals(valueMini, valueAlpha)
            if (indexAlpha != indexMini) {
                println("AlphaBeta: $indexAlpha, Minimax: $indexMini")
                println(t)
            }
        }
    }
}