package bot.tree

import junit.framework.TestCase
import kotlin.random.Random

class AlphaBetaPruningTest : TestCase() {
    private var t = Tree(2, 2)

    public override fun setUp() {
        t = Tree(3, 2)
        for (i in 0 until t.amountOfLeaves) {
            t.getLeave(i).value = Random.nextInt()
        }
        println(t)
    }

    fun testTestRun() {
        assertEquals(t.root.getChild(t.root.indexOfNodeWithBestExpectationOfHighValue()).value, AlphaBetaPruning().run(t))
        println(AlphaBetaPruning().run(t))
        println(t)
    }
}