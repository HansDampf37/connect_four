package bot.bots.tree

import TestUtils
import TestUtils.Companion.buildSmallTree
import bot.ratingfunctions.ruediger.RuedigerDerBot
import junit.framework.TestCase
import model.Board
import model.Token
import model.procedure.ConsoleOutput
import java.lang.Thread.sleep
import kotlin.random.Random

class AlphaBetaPruningTest : TestCase() {
    private lateinit var tree: Tree<GameState>

    public override fun setUp() {
        super.setUp()
        val gs = GameState(Board(2, 2), nextPlayer = Token.PLAYER_1)
        tree = Tree(gs)
        buildSmallTree(tree)
    }

    fun testTrivialTree() {
        assertEquals(1, AlphaBetaPruning.run(tree))
    }

    fun testSpeed() {
        val t = Tree(6, 7, GameState(Board(), Token.PLAYER_1)) { _: Int, _: Int, _: GameState? -> GameState(Board(), Token.PLAYER_1) }
        t.leaves.forEach { it.value = Random.nextInt() }
        println("building tree finished!")
        var finished = false
        val thread = Thread {
            run {
                sleep(1000)
                if (!finished) fail()
            }
        }.apply { start() }
        AlphaBetaPruning.run(t)
        finished = true
        thread.join()
    }

    fun testSameResultAsMinimax() {
        for (i in 0..20) {
            val t = Tree(listOf(5, 6, 7).random(), listOf(5).random(), GameState(Board(5), Token.PLAYER_1))  { _: Int, x: Int, _: GameState? -> GameState(Board(5), Token.PLAYER_1, x) }
            t.leaves.forEach { it.value = Random.nextInt() }
            val indexMini = Minimax.run(t)
            val valueMini = t.root.value
            val indexAlpha = AlphaBetaPruning.run(t)
            val valueAlpha = t.root.value
            assertEquals(indexMini, indexAlpha)
            assertEquals(valueMini, valueAlpha)
            assertTrue("returned index must be in bounds", indexAlpha >= 0 && indexAlpha < t.root.size)
        }
    }

    fun testProgressedGame() {
        buildTreeAndTestMove(TestUtils.noPredicament, Token.PLAYER_1, 100, Integer.MAX_VALUE - 1, listOf(2))
        buildTreeAndTestMove(TestUtils.noPredicament, Token.PLAYER_2, 100, Integer.MAX_VALUE - 1, listOf(2))
    }

    fun testPredicament1() {
        val board = TestUtils.createPredicamentForP1_2.clone()
        board.throwInColumn(1, Token.PLAYER_1)
        println(RuedigerDerBot(Token.PLAYER_1).invoke(board))
        buildTreeAndTestMove(TestUtils.createPredicamentForP1_2, Token.PLAYER_2, 1000, listOf(1))
        buildTreeAndTestMove(TestUtils.createPredicamentForP1_2, Token.PLAYER_1, 1000, Integer.MAX_VALUE - 6, listOf(1, 2))
    }

    fun testPredicament2() {
        buildTreeAndTestMove(TestUtils.createPredicamentForP1, Token.PLAYER_2, 1000, listOf(2))
        buildTreeAndTestMove(TestUtils.createPredicamentForP1, Token.PLAYER_1, 1000, Integer.MAX_VALUE - 10, listOf(2))
    }

    fun testPredicament3() {
        buildTreeAndTestMove(TestUtils.createPredicamentForP1_second, Token.PLAYER_1, 1000, Integer.MAX_VALUE - 10, listOf(3))
    }

    fun testFinish1() {
        buildTreeAndTestMove(TestUtils.p1CanFinish, Token.PLAYER_2, 1000, listOf(1, 3, 4))
        buildTreeAndTestMove(TestUtils.p1CanFinish, Token.PLAYER_1, 1000, Integer.MAX_VALUE - 3, listOf(1, 4))
    }

    fun testFinish2() {
        buildTreeAndTestMove(TestUtils.p2CanFinish, Token.PLAYER_1, 1000, listOf(1, 2))
        buildTreeAndTestMove(TestUtils.p2CanFinish, Token.PLAYER_2, 1000, Integer.MAX_VALUE - 3, listOf(1))
    }

    fun testFinish3() {
        buildTreeAndTestMove(TestUtils.p2CanFinish_2, Token.PLAYER_1, 1000, listOf(2, 5))
        buildTreeAndTestMove(TestUtils.p2CanFinish_2, Token.PLAYER_2, 1000, Integer.MAX_VALUE - 3, listOf(2, 5))
    }

    private fun buildTreeAndTestMove(board: Board, player: Token, buildTime: Long, expectedEvaluation: Int, expectedIndices: List<Int>) {
        val eval = buildTreeAndTestMove(board, player, buildTime, expectedIndices)
        assertEquals("$board \nwas evaluated incorrectly:\nexpected $expectedEvaluation,\nactual: $eval", expectedEvaluation, eval)
    }

    private fun buildTreeAndTestMove(board: Board, player: Token, buildTime: Long, expectedIndices: List<Int>): Int {
        val t = Tree(GameState(board, player))
        val tb = TreeBuilder(RuedigerDerBot(player), TreeBuilder.SizeScheduler(TreeBuilder.SizeScheduler.Size.Small))
        val field = tb::class.java.getDeclaredField("tree")
        field.isAccessible = true
        field.set(tb, t)
        tb.start()
        sleep(buildTime)
        tb.exit()
        val index = AlphaBetaPruning.run(t)
        assertTrue("chose index $index instead of one in $expectedIndices for \n$board \nwhich leads to a evaluation of ${t.root.value}", expectedIndices.contains(index))
        println(board)
        println("correct column for $player: $index")
        return t.root.value
    }
}