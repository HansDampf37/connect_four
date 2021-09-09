package gui

import model.Board
import model.Player
import model.Token
import model.procedure.Game
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.MatteBorder

class Gui(val game: Game) {
    private var frame = JFrame("4 - gewinnt")
    private val board: Board get() = game.board
    private val currentPlayer: Player get() = game.currentPlayer
    private val playerColors = HashMap<Token, Color>()

    init {
        playerColors[game.players[0].side] = Color.red
        playerColors[game.players[1].side] = Color.yellow
        playerColors[Token.EMPTY] = Color.white
        EventQueue.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (ex: ClassNotFoundException) {
            } catch (ex: InstantiationException) {
            } catch (ex: IllegalAccessException) {
            } catch (ex: UnsupportedLookAndFeelException) {
            }
            frame = JFrame("4-Gewinnt")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = BoxLayout(frame.contentPane, BoxLayout.Y_AXIS)
            frame.add(Menu())
            frame.add(BoardPanel(board))
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    fun update() {
        frame.contentPane.validate()
        frame.contentPane.repaint()
    }

    private inner class BoardPanel(board: Board) : JPanel() {
        private val cells: ArrayList<ArrayList<CellPane>> = ArrayList()

        init {
            layout = GridBagLayout()
            val gbc = GridBagConstraints()
            for (x in 0 until board.WIDTH) {
                cells.add(ArrayList())
                for (y in 0 until board.HEIGHT) {
                    gbc.gridx = x
                    gbc.gridy = y
                    val cellPane = CellPane(x, board.HEIGHT - 1 - y)
                    cellPane.addMouseListener(object : MouseAdapter() {
                        override fun mouseReleased(e: MouseEvent?) {
                            if (currentPlayer is GUIHumanPlayer) (currentPlayer as GUIHumanPlayer).nextMove = x
                        }

                        override fun mouseEntered(e: MouseEvent?) {
                            try {
                                cells[x].last { it.isEmpty }.marked = true
                                update()
                            } catch (e: NoSuchElementException) {
                            }
                        }

                        override fun mouseExited(e: MouseEvent?) {
                            cells[x].forEach { it.marked = false }
                            update()
                        }
                    })
                    val border: Border = if (y < board.HEIGHT) {
                        if (x < board.HEIGHT) {
                            MatteBorder(1, 1, 0, 0, Color.GRAY)
                        } else {
                            MatteBorder(1, 1, 0, 1, Color.GRAY)
                        }
                    } else {
                        if (x < board.HEIGHT) {
                            MatteBorder(1, 1, 1, 0, Color.GRAY)
                        } else {
                            MatteBorder(1, 1, 1, 1, Color.GRAY)
                        }
                    }
                    cellPane.border = border
                    add(cellPane, gbc)
                    cells[x].add(cellPane)
                }
            }
        }
    }

    private open inner class CellPane(val xV: Int, val yV: Int) : JPanel() {
        private val size: Int = 50
        var marked: Boolean = false

        val isEmpty: Boolean get() = board.fields[xV][yV] == Token.EMPTY

        override fun getPreferredSize(): Dimension {
            return Dimension(size, size)
        }

        override fun getMaximumSize(): Dimension {
            return Dimension(size, size)
        }

        override fun getMinimumSize(): Dimension {
            return Dimension(size, size)
        }

        override fun repaint() {
            background = playerColors[board.fields[xV][yV]]
        }

        override fun paint(g: Graphics?) {
            repaint()
            g!!.color = background
            g.fillOval(size / 10, size / 10, size * 4 / 5, size * 4 / 5)
            g.color = Color.darkGray
            if (marked) {
                g.drawOval(size / 10, size / 10, size * 4 / 5, size * 4 / 5)
            }
        }
    }

    private inner class Menu : JPanel() {
        private val currentPlayerLabel = object : CellPane(-1, -1) {
            override fun repaint() {
                background = if (game.board.winner == Token.EMPTY) playerColors[currentPlayer.side] else playerColors[Token.EMPTY]
            }
        }

        private val winnerLabel = object : CellPane(-1, -1) {
            override fun repaint() {
                background = playerColors[game.board.winner]
            }
        }

        private val reset = JButton("Reset").apply {
            addActionListener {
                game.reset()
                update()
                Thread {game.play()}.start()
            }
        }

        init {
            background = Color.lightGray
            this.layout = BoxLayout(this, BoxLayout.X_AXIS)
            this.add(JLabel("Next Player:"))
            this.add(currentPlayerLabel)
            this.add(JLabel("Winner:"))
            this.add(winnerLabel)
            this.add(reset)
        }
    }
}