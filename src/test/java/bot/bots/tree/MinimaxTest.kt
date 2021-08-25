package bot.bots.tree

import junit.framework.TestCase
import model.Token

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
        for (i in 0..20) {
            val t = Tree(listOf(4, 5).random(), listOf(2, 3).random(), GameState(nextPlayer = Token.PLAYER_1))  { _: Int, _: Int, _: Node? -> GameState(nextPlayer = Token.PLAYER_1) }
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