package bot.tree

import bot.GameState
import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep

class TreeBuilderTest : TestCase() {

    private lateinit var tb: TreeBuilder

    override fun setUp() {
        tb = TreeBuilder()
    }

    fun testTestRun() {}

    fun testStart() {
        val size = tb.tree.size
        Thread(tb).start()
        tb.start()
        sleep(2000)
        assertTrue(tb.tree.size > size)
    }

    fun testPause() {
        val thread = Thread(tb).apply { start() }
        tb.start()
        var blocked = true
        Thread {
            run {
                sleep(3000)
                if (blocked) fail("took too long to pause")
            }
        }.start()
        tb.pause()
        blocked = false
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
}