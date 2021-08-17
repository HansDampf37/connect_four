package bot.tree

import junit.framework.TestCase
import model.Board
import model.Token

class GameStateTest : TestCase() {
    private var gs: GameState = GameState(Board(), Token.PLAYER_1)

}