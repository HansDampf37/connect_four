package gui

import Controller
import java.awt.Color
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.border.Border
import javax.swing.border.MatteBorder

class BoardPanel(private val controller: Controller) : JPanel() {
    private val cells: ArrayList<ArrayList<CellPane>> = ArrayList()

    init {
        layout = GridBagLayout()
        val gbc = GridBagConstraints()
        for (x in 0 until controller.board.WIDTH) {
            cells.add(ArrayList())
            for (y in 0 until controller.board.HEIGHT) {
                gbc.gridx = x
                gbc.gridy = y
                val cellPane = CellPane(x, controller.board.HEIGHT - 1 - y, controller)
                cellPane.addMouseListener(object : MouseAdapter() {
                    override fun mouseReleased(e: MouseEvent?) {
                        if (controller.currentPlayer is GUIHumanPlayer) (controller.currentPlayer as GUIHumanPlayer).nextMove = x
                    }

                    override fun mouseEntered(e: MouseEvent?) {
                        try {
                            cells[x].last { it.isEmpty }.marked = true
                            controller.updateGui()
                        } catch (e: NoSuchElementException) {
                        }
                    }

                    override fun mouseExited(e: MouseEvent?) {
                        cells[x].forEach { it.marked = false }
                        controller.updateGui()
                    }
                })
                val border: Border = if (y < controller.board.HEIGHT) {
                    if (x < controller.board.HEIGHT) {
                        MatteBorder(1, 1, 0, 0, Color.GRAY)
                    } else {
                        MatteBorder(1, 1, 0, 1, Color.GRAY)
                    }
                } else {
                    if (x < controller.board.HEIGHT) {
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