package rl

import bot.bots.PonderingBot
import bot.bots.tree.TreeBuilder
import bot.ratingfunctions.ruediger.RuedigerDerBot
import model.Board
import model.Token
import model.procedure.Game
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Thread.sleep
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO

class DatasetCreation(boardWidth: Int, boardHeight: Int, val basePath: String, val minimumSize: Int) : Game(
    PonderingBot(Token.PLAYER_1, RuedigerDerBot(Token.PLAYER_1)),
    PonderingBot(Token.PLAYER_2, RuedigerDerBot(Token.PLAYER_2)),
    boardWidth, boardHeight, null
) {

    init {
        (players[0] as PonderingBot).treeBuilder.scheduler = TreeBuilder.SimpleScheduler(500000)
        (players[1] as PonderingBot).treeBuilder.scheduler = TreeBuilder.SimpleScheduler(500000)
    }

    private var evaluatedPositions = 0
    private var finished = false

    override fun play(): Token {
        reset()
        curPlayerInd = arrayOf(0, 1).random()
        val moves: Map<Token, MutableList<Pair<BufferedImage, File>>> = mapOf(
            Pair(Token.PLAYER_1, mutableListOf()),
            Pair(Token.PLAYER_2, mutableListOf())
        )
        var winner: Token = Token.EMPTY
        while (winner == Token.EMPTY && board.stillSpace()) {
            while (!(currentPlayer as PonderingBot).treeBuilder.isIdle) sleep(100)
            val index = players[curPlayerInd].getColumnOfNextMove()
            val images = boardToImages(board)
            val player = when (players[curPlayerInd].side) {
                Token.PLAYER_1 -> "black"
                Token.PLAYER_2 -> "white"
                else -> throw IllegalStateException("Must be someones turn")
            }
            val playerother = when (players[curPlayerInd].side) {
                Token.PLAYER_2 -> "black"
                Token.PLAYER_1 -> "white"
                else -> throw IllegalStateException("Must be someones turn")
            }
            val newFile1 = File(Path.of(basePath, player, "$index", "${UUID.randomUUID()}.png").toUri())
            val newFile2 = File(Path.of(basePath, player, "${board.WIDTH - index - 1}", "${UUID.randomUUID()}.png").toUri())
            val newFile3 = File(Path.of(basePath, playerother, "$index", "${UUID.randomUUID()}.png").toUri())
            val newFile4 = File(Path.of(basePath, playerother, "${board.WIDTH - index - 1}", "${UUID.randomUUID()}.png").toUri())
            moves[currentPlayer.side]!!.add(Pair(images[0], newFile1))
            moves[currentPlayer.side]!!.add(Pair(images[1], newFile2))
            moves[currentPlayer.side]!!.add(Pair(images[2], newFile3))
            moves[currentPlayer.side]!!.add(Pair(images[3], newFile4))
            throwInColumn(index)
            println(board)
            winner = board.winner
        }
        for (pair in moves[winner] ?: moves[Token.PLAYER_1]!!.apply{ this.addAll(moves[Token.PLAYER_2]!!)}) {
            pair.second.parentFile.mkdirs()
            ImageIO.write(pair.first, "png", pair.second)
            evaluatedPositions++
            if (evaluatedPositions >= minimumSize) finished = true
        }
        return winner
    }

    fun create() {
        while (!finished) {
            play()
        }
    }

    companion object {
        fun savePosition(board: Board, column: Int, player: Token, basePath: String) {
            fun player(player: Token) = when (player) {
                Token.PLAYER_1 -> "black"
                Token.PLAYER_2 -> "white"
                else -> throw IllegalStateException("Must be someones turn")
            }
            val images = boardToImages(board)
            val files = listOf(
                File(Path.of(basePath, player(player), "$column", "${UUID.randomUUID()}.png").toUri()),
                File(Path.of(basePath, player(player), "${board.WIDTH - column - 1}", "${UUID.randomUUID()}.png").toUri()),
                File(Path.of(basePath, player(player.other()), "$column", "${UUID.randomUUID()}.png").toUri()),
                File(Path.of(basePath, player(player.other()), "${board.WIDTH - column - 1}", "${UUID.randomUUID()}.png").toUri())
            )
            for (i in 0..< 4) {
                files[i].parentFile.mkdirs()
                ImageIO.write(images[i], "png", files[i])
            }
        }

        fun boardToImages(board: Board): List<BufferedImage> {
            val image1 = BufferedImage(board.WIDTH, board.HEIGHT, BufferedImage.TYPE_BYTE_GRAY)
            val image2 = BufferedImage(board.WIDTH, board.HEIGHT, BufferedImage.TYPE_BYTE_GRAY)
            val image3 = BufferedImage(board.WIDTH, board.HEIGHT, BufferedImage.TYPE_BYTE_GRAY)
            val image4 = BufferedImage(board.WIDTH, board.HEIGHT, BufferedImage.TYPE_BYTE_GRAY)
            // Define color mappings for player 1, player 2, and empty cells
            val colors = mapOf(
                Token.EMPTY to 127,
                Token.PLAYER_1 to 0,
                Token.PLAYER_2 to 255
            )
            for (x in 0 until board.WIDTH) {
                for (y in 0 until board.HEIGHT) {
                    val token = board[x, y]
                    val grayscaleValue12 = colors[token]!!
                    val grayscaleValue34 = colors[token.other()]!!
                    image1.setRGB(x, board.HEIGHT - y - 1, grayscaleValue12 shl 16 or (grayscaleValue12 shl 8) or grayscaleValue12)
                    image2.setRGB(board.WIDTH - x - 1, board.HEIGHT - y - 1, grayscaleValue12 shl 16 or (grayscaleValue12 shl 8) or grayscaleValue12)
                    image3.setRGB(x, board.HEIGHT - y - 1, grayscaleValue34 shl 16 or (grayscaleValue34 shl 8) or grayscaleValue34)
                    image4.setRGB(board.WIDTH - x - 1, board.HEIGHT - y - 1, grayscaleValue34 shl 16 or (grayscaleValue34 shl 8) or grayscaleValue34)
                }
            }
            return listOf(image1, image2, image3, image4)
        }
    }
}


fun main() {
    //val samples = 120000
    //val startTime = System.currentTimeMillis()
    //ConsoleOutput.setAll(false, false, false, false, false, false, false, false, false)
    //DatasetCreation(7, 6, basePath = "src/main/python/adrian", minimumSize = samples).create()
    //val endTime = System.currentTimeMillis()
    //val elapsedTimeMillis = endTime - startTime
    //println("Created $samples samples elapsed: ${elapsedTimeMillis / 1000} seconds")
    //exitProcess(0)
    DatasetCreation.savePosition(Board().apply { throwInColumn(2, Token.PLAYER_1) }, 4, Token.PLAYER_2, "/home/adrian/Schreibtisch/test")
}