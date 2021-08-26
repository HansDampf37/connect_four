package bot.bots.tree

import junit.framework.TestCase

class MinimaxTest : TestCase() {
    private lateinit var tree: Tree<GameState>

    public override fun setUp() {
        tree = Tree(GameState())
        TestUtils.buildSmallTree(tree)
    }

    fun testMinimax() {
        assertEquals(1, Minimax.run(tree))
    }

    fun testSameResultAsAlphaBeta() {
        AlphaBetaPruningTest().testSameResultAsMinimax()
    }
}