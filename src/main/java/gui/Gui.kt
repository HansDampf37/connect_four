package gui

import Controller
import model.Player
import model.Token
import java.awt.*
import javax.swing.*

class Gui(controller: Controller) {
    companion object {
        val playerColors = HashMap<Token, Color>().apply {
            put(Token.PLAYER_1, Color.RED)
            put(Token.PLAYER_2, Color.YELLOW)
            put(Token.EMPTY, Color.WHITE)
        }
    }

    val p1Selected: Player? get() = menu.playerChosen1
    val p2Selected: Player? get() = menu.playerChosen2
    val widthSelected get() = -1
    val heightSelected get() = -1
    private var frame = JFrame("4 - gewinnt")
    private var menu: StatusMenu = StatusMenu(controller)
    private var board: BoardPanel = BoardPanel(controller)

    init {
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")
            } catch (ex: ClassNotFoundException) {
            } catch (ex: InstantiationException) {
            } catch (ex: IllegalAccessException) {
            } catch (ex: UnsupportedLookAndFeelException) {
            }
            frame = JFrame("4-Gewinnt")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = BoxLayout(frame.contentPane, BoxLayout.Y_AXIS)
            frame.add(menu)
            frame.add(board)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    fun update() {
        frame.contentPane.validate()
        frame.contentPane.repaint()
    }
}