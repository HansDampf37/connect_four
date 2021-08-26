import bot.bots.tree.GameState
import bot.bots.tree.Tree
import model.Board
import model.Token
import model.Token.EMPTY as e
import model.Token.PLAYER_1 as p1
import model.Token.PLAYER_2 as p2

class TestUtils {
    companion object {
        val predicament1 = Board(
            arrayOf(
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(p1, p2, p1, e, e, e),
                arrayOf(p2, p2, p2, p1, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val noPredicament = Board(
            arrayOf(
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p2, p1, p2, p1, e, e),
                arrayOf(p1, p2, p2, p2, p1, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val wonP1 = Board(
            arrayOf(
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(p1, p1, e, e, e, e),
                arrayOf(p2, p1, p2, p1, e, e),
                arrayOf(p1, p2, p2, p2, p1, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val wonP1_second = Board(
            arrayOf(
                arrayOf(e, e, e, e, e, e),
                arrayOf(p2, e, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, p2, p2, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val wonP2 = Board(
            arrayOf(
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p2, p1, p2, p1, e, e),
                arrayOf(p2, p2, p2, p2, p1, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val gameProgressed = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p2, p2, p1, p1, p1, p2),
                arrayOf(p1, p2, p1, p1, p2, p1),
                arrayOf(e, e, e, e, e, e),
                arrayOf(p2, p2, p2, p1, e, e),
                arrayOf(p1, p2, p1, p2, e, e)
            )
        )

        val gameProgressedMirrored = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, e, e),
                arrayOf(p2, p2, p2, p1, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(p1, p2, p1, p1, p2, p1),
                arrayOf(p2, p2, p1, p1, p1, p2),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, p2, p1, p2, p1, p2),
            )
        )

        val gameAlmostDraw = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, p1, e),
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p2, p1, p2, p1, p2, e),
                arrayOf(p2, p1, p2, p1, p2, p2),
                arrayOf(p1, p2, p1, p2, p1, e),
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p2, p1, p2, p1, p2, e)
            )
        )

        val gameDraw = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, p1, p1),
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p2, p1, p2, p1, p2, p1),
                arrayOf(p2, p1, p2, p1, p2, p2),
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p1, p2, p1, p2, p1, p1),
                arrayOf(p2, p1, p2, p1, p2, p2)
            )
        )

        val wonP2_lastMove = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, p1, p1),
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p2, p1, p2, p1, p2, p2),
                arrayOf(p2, p1, p2, p1, p2, p2),
                arrayOf(p1, p2, p1, p2, p1, p2),
                arrayOf(p1, p2, p1, p2, p1, p1),
                arrayOf(p2, p1, p2, p1, p2, p2)
            )
        )

        val p1CanFinish = Board(
            arrayOf(
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, p2, p2, e, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val p2CanFinish = Board(
            arrayOf(
                arrayOf(p2, p2, p1, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, e, e, e, e, e),
                arrayOf(p1, p2, p1, p2, e, e),
                arrayOf(p2, p1, e, e, e, e),
                arrayOf(e, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e)
            )
        )

        val createPredicamentForP1 = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, p1, e, e, e),
                arrayOf(e, e, e, e, e, e, e, e),
                arrayOf(p2, p1, p2, p1, p2, e, e, e),
                arrayOf(p2, p1, p2, p1, p2, p1, p1, e),
                arrayOf(p1, p2, p1, p1, p1, p2, p1, p1),
                arrayOf(p1, p2, p1, p2, p1, e, e, e),
                arrayOf(p2, p1, p2, e, e, e, e, e)
            )
        )

        val test = Board(
            arrayOf(
                arrayOf(p1, p2, p1, e, e, e, e, e),
                arrayOf(e, e, e, e, e, e, e, e),
                arrayOf(p2, p1, p2, p1, p2, p2, e, e),
                arrayOf(p2, p1, p2, p1, p2, p1, p1, e),
                arrayOf(p1, p2, p1, p1, p1, p2, p1, p1),
                arrayOf(p1, p2, p1, p2, p1, e, e, e),
                arrayOf(p2, p1, p2, e, e, e, e, e)
            )
        )

        val createPredicamentForP2_third = Board(
            arrayOf(
                arrayOf(p2, p1, p1, p1, p2, e, e, e),
                arrayOf(e, e, e, e, e, e, e, e),
                arrayOf(p1, p2, p1, p1, p1, e, e, e),
                arrayOf(p2, p2, p1, p2, p2, e, e, e),
                arrayOf(p1, p1, p2, p2, p1, e, e, e),
                arrayOf(p1, p1, e, e, e, e, e, e),
                arrayOf(p2, p1, e, e, e, e, e, e)
            )
        )

        val createPredicamentForP1_second = Board(
            arrayOf(
                arrayOf(p1, p2, p1, p2, p1, e),
                arrayOf(e,  e,  e,  e,  e,  e),
                arrayOf(p2, p1, p2, p2, p1, e),
                arrayOf(p2, p1, p2, p1, p1, e),
                arrayOf(p1, p2, p1, p2, p1, e),
                arrayOf(p1, p2, e, e, e, e),
                arrayOf(p2, p1, e, e, e, e)
            )
        )

        fun buildSmallTree(tree: Tree<GameState>) {
            val node2 = GameState(Board(), Token.PLAYER_2, lastMoveWasColumn = 0).apply { value = 3 }
            val node3 = GameState(Board(), Token.PLAYER_2, lastMoveWasColumn = 1).apply { value = 2 }
            val node4 = GameState(Board(), Token.PLAYER_1, lastMoveWasColumn = 0).apply { value = 4 }
            tree.addChild(tree.root, node2)
            tree.addChild(tree.root, node3)
            tree.addChild(node3, node4)
        }
    }
}