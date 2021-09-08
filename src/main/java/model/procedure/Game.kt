package model.procedure

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Arrays
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.MatteBorder

class Game(player1: Player, player2: Player) {
    var board: Board
    private var players: Array<Player> = arrayOf(player1, player2)
    private var curPlayerInd: Int = (Math.random() * players.size).toInt()
    private var frame = JFrame("4 - gewinnt")

    init {
        if (player1.board != player2.board) throw IllegalArgumentException("Boards must be equal")
        if (player1.side == player2.side) throw IllegalArgumentException("sides must be different")
        board = player1.board
        reset()
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
            frame.layout = BorderLayout()
            frame.add(BoardPanel(board))
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    private fun throwInColumn(x: Int) {
        if (board.throwInColumn(x, players[curPlayerInd].side)) {
            curPlayerInd = ++curPlayerInd % players.size
            runBlocking {
                launch { players.forEach { it.onMovePlayed(x) } }
            }
            frame.contentPane.validate()
            frame.contentPane.repaint()
        }
    }

    fun play(): Token {
        if (ConsoleOutput.playerGreetings) println(players[0].name + " vs " + players[1].name)
        var winner: Token = Token.EMPTY
        while (winner === Token.EMPTY) {
            if (ConsoleOutput.printBoard) println(board)
            throwInColumn(players[curPlayerInd].getColumnOfNextMove())
            if (!board.stillSpace()) break
            winner = board.winner
        }
        players.forEach { it.onGameOver() }
        //if (ConsoleOutput.printBoard) System.out.println(board);
        if (ConsoleOutput.gameResult) {
            val finalWinner = winner
            Arrays.stream(players).filter { p: Player -> p.side === finalWinner }
                .forEach { p: Player -> println(p.name + " won") }
            if (Arrays.stream(players).noneMatch { p: Player -> p.side === finalWinner }) println("Its a draw")
            println(board)
        }
        reset()
        return winner
    }

    private fun reset() {
        val height = board.HEIGHT
        val width = board.WIDTH
        board = Board(width, height)
        players.forEach { it.board = board }
        curPlayerInd = players.indexOfFirst { it.side == Token.PLAYER_1 }
    }

    private inner class BoardPanel(board: Board) : JPanel() {
        init {
            layout = GridBagLayout()
            val gbc = GridBagConstraints()
            for (row in 0 until board.HEIGHT) {
                for (col in 0 until board.WIDTH) {
                    gbc.gridx = col
                    gbc.gridy = row
                    val cellPane = CellPane(col, row)
                    val border: Border = if (row < board.HEIGHT) {
                        if (col < board.WIDTH) {
                            MatteBorder(1, 1, 0, 0, Color.GRAY)
                        } else {
                            MatteBorder(1, 1, 0, 1, Color.GRAY)
                        }
                    } else {
                        if (col < board.HEIGHT) {
                            MatteBorder(1, 1, 1, 0, Color.GRAY)
                        } else {
                            MatteBorder(1, 1, 1, 1, Color.GRAY)
                        }
                    }
                    cellPane.border = border
                    add(cellPane, gbc)
                }
            }
        }
    }

    private inner class CellPane(val xV: Int, val yV: Int) : JPanel() {

        override fun getPreferredSize(): Dimension {
            return Dimension(50, 50)
        }

        override fun repaint() {
            background = when (board.fields[xV][board.HEIGHT - 1 - yV]) {
                Token.PLAYER_1 -> Color.yellow
                Token.PLAYER_2 -> Color.red
                else -> Color.white
            }
        }

        override fun paint(g: Graphics?) {
            repaint()
            g!!.color =  background
            g.fillOval(5, 5, width - 10, height - 10)
        }

        init {
            addMouseListener(object : MouseAdapter() {
                override fun mouseReleased(e: MouseEvent?) {
                    if (players[curPlayerInd] is GUIHumanPlayer) (players[curPlayerInd] as GUIHumanPlayer).nextMove = xV
                }
            })
        }
    }
}