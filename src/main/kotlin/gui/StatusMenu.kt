package gui

import Controller
import bot.bots.Bogo
import bot.bots.PonderingBot
import model.Player
import model.Token
import java.awt.Color
import java.awt.Dimension
import javax.swing.*

class StatusMenu(private var controller: Controller) : JPanel() {
    var playerChosen1: Player? = null
    var playerChosen2: Player? = null
    private val p1Select = PlayerSelect(1)
    private val p2Select = PlayerSelect(2)

    private val currentPlayerLabel = object : CellPane(-1, -1, controller) {
        override fun repaint() {
            background =
                if (controller.board.winner == Token.EMPTY) Gui.playerColors[controller.currentPlayer.side] else Gui.playerColors[Token.EMPTY]
        }
    }
    private val winnerLabel = object : CellPane(-1, -1, controller) {
        override fun repaint() {
            background = Gui.playerColors[controller.board.winner]
        }
    }
    private val reset = JButton("Reset").apply {
        addActionListener {
            controller.stopCurrentGame()
            controller.updateGui()
            controller.startNewGame()
        }
    }

    private inner class PlayerSelect(val player: Int) : JMenuBar() {
        init {
            val menu = JMenu("Spieler wählen")
            add(menu)
            menu.add(PEntry("Mensch", menu))
            menu.add(PEntry("Zufall", menu))
            menu.add(PEntry("Computer", menu))
        }

        override fun getPreferredSize(): Dimension = Dimension(100, 40)
        override fun getMaximumSize(): Dimension = Dimension(100, 40)
        override fun getMinimumSize(): Dimension = Dimension(100, 40)

        private inner class PEntry(val typeName: String, m: JMenu) : JMenuItem(typeName) {
            val human = "Mensch"
            val bogo = "Zufall"
            val bot = "Computer"

            init {
                addActionListener {
                    when (player) {
                        1 -> playerChosen1 = getInstance(Token.PLAYER_1)
                        2 -> playerChosen2 = getInstance(Token.PLAYER_2)
                        else -> throw IllegalArgumentException()
                    }
                    m.text = typeName
                }
            }

            private fun getInstance(side: Token): Player {
                return when (typeName) {
                    human -> GUIHumanPlayer(side)
                    bogo -> Bogo(side)
                    bot -> PonderingBot(side)
                    else -> throw IllegalStateException("Unknown player type")
                }
            }
        }
    }

    init {
        background = Color.lightGray
        this.layout = BoxLayout(this, BoxLayout.X_AXIS)
        this.add(Box.createHorizontalGlue())
        this.add(p1Select)
        this.add(JLabel("vs."))
        this.add(p2Select)
        this.add(JLabel("Next Player:"))
        this.add(currentPlayerLabel)
        this.add(JLabel("Winner:"))
        this.add(winnerLabel)
        this.add(reset)
        this.add(Box.createHorizontalGlue())
    }
}