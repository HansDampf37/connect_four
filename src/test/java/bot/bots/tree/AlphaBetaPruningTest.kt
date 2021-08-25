package bot.bots.tree

import TestUtils
import bot.ratingfunctions.ruediger.RuedigerDerBot
import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep
import kotlin.concurrent.withLock
import kotlin.random.Random

class AlphaBetaPruningTest : TestCase() {
    private lateinit var node: Node

    public override fun setUp() {
        super.setUp()
        node =
            Node(
                mutableListOf(
                    Node(mutableListOf(), 2),
                    Node(
                        mutableListOf(
                            Node(value = 4)
                        ),
                        3
                    )
                ),
                1
            )
    }

    fun testAlphaBetaPruning() {
        val t = Tree(node)
        assertEquals(1, AlphaBetaPruning.run(t))
    }

    fun testSpeed() {
        val t = Tree(6, 7, Node()) { _: Int, _: Int, _: Node? -> Node() }
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
            val t = Tree(listOf(5).random(), listOf(5, 6, 7).random(), Node())  { _: Int, _: Int, _: Node? -> Node() }
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
        buildTreeAndTestMove(TestUtils.noPredicament, Token.PLAYER_1, 100, Integer.MAX_VALUE, listOf(2))
        buildTreeAndTestMove(TestUtils.noPredicament, Token.PLAYER_2, 100, Integer.MAX_VALUE, listOf(2))
    }

    fun testIfFindsPredicament() {
        buildTreeAndTestMove(TestUtils.createPredicamentForP1, Token.PLAYER_1, 300, Integer.MAX_VALUE, listOf(1, 4))
        buildTreeAndTestMove(TestUtils.createPredicamentForP2, Token.PLAYER_2, 300, Integer.MAX_VALUE, listOf(1))
        buildTreeAndTestMove(TestUtils.createPredicamentForP2, Token.PLAYER_1, 300, listOf(1))
        buildTreeAndTestMove(TestUtils.p1CanFinish, Token.PLAYER_1, 300, Integer.MAX_VALUE, listOf(1))
        buildTreeAndTestMove(TestUtils.createPredicamentForP1_second, Token.PLAYER_1, 300, Integer.MAX_VALUE - 3, listOf(3))
    }

    private fun buildTreeAndTestMove(board: Board, player: Token, buildTime: Long, expectedEvaluation: Int, expectedIndices: List<Int>) {
        val eval = buildTreeAndTestMove(board, player, buildTime, expectedIndices)
        assertEquals("$board \nwas evaluated incorrectly:\nexpected $expectedEvaluation,\nactual: $eval", eval, expectedEvaluation)
    }

    private fun buildTreeAndTestMove(board: Board, player: Token, buildTime: Long, expectedIndices: List<Int>): Int {
        val t = Tree(GameState(board, player))
        val tb = TreeBuilder(RuedigerDerBot(player), TreeBuilder.Size.Small)
        val field = tb::class.java.getDeclaredField("tree")
        field.isAccessible = true
        field.set(tb, t)
        tb.start()
        sleep(buildTime)
        val index = tb.lock.withLock { AlphaBetaPruning.run(t) }
        tb.exit()
        assertTrue("chose index $index instead of one in $expectedIndices for \n$board \nwhich leads to a evaluation of ${t.root.value}", expectedIndices.contains(index))
        return t.root.value
    }
}