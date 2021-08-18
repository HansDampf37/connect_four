package bot.tree

import junit.framework.TestCase
import java.lang.Thread.sleep
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
        assertEquals(1, AlphaBetaPruning.run(Tree(root = node)))
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
}