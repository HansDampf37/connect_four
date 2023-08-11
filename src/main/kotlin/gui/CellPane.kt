package gui

import Controller
import model.Token
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

open class CellPane(private val xV: Int, private val yV: Int, private val controller: Controller) : JPanel() {
    private val size: Int = 50
    var marked: Boolean = false

    open val isEmpty: Boolean get() = controller.board.fields[xV][yV] == Token.EMPTY

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
        background = Gui.playerColors[if (controller == null) Token.EMPTY else controller.board.fields[xV][yV]]
    }

    override fun paint(g: Graphics?) {
        g!!
        repaint()
        if (background != Color.white) {
            g.color = background.darker()
            g.fillOval(size / 10, size / 10, size * 4 / 5, size * 4 / 5)
            g.color = background
            g.fillOval(size * 4 / 20, size * 4 / 20, size * 6 / 10, size * 6 / 10)
        } else {
            g.color = background
            g.fillOval(size / 10, size / 10, size * 4 / 5, size * 4 / 5)
        }
        if (marked) {
            g.color = Color.darkGray
            g.drawOval(size / 10, size / 10, size * 4 / 5, size * 4 / 5)
        }
    }
}