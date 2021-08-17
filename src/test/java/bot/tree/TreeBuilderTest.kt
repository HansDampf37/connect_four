package bot.tree

import bot.GameState
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

    fun testTree() {
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
        val t = Tree(2, 2) { _: Int, i: Int, parent: GameState? ->
            if (parent == null) GameState(Board(), Token.PLAYER_1) else GameState(
                parent.board.clone().apply { throwInColumn(i, parent.nextPlayer) }, parent.nextPlayer.other()
            )
        }
        tb.tree = t
        val checkNode = t.root[0]
        assertEquals(7, t.size)
        tb.moveMade(0)
        assertEquals(3, t.size)
        assertEquals(t.root, checkNode)
    }

    fun testTestRun() {}
}