package rl

import bot.bots.PonderingBot
import bot.bots.tree.TreeBuilder
import bot.ratingfunctions.ruediger.RuedigerDerBot
import model.Board
import model.Token
import model.procedure.ConsoleOutput
import model.procedure.Game
import java.io.File
import java.lang.Thread.sleep
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.lang.IllegalStateException
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
import kotlin.system.exitProcess

class DatasetCreation(boardWidth: Int, boardHeight: Int, val basePath: String, val minimumSize: Int) : Game(
    PonderingBot(Token.PLAYER_1, RuedigerDerBot(Token.PLAYER_1)),
    PonderingBot(Token.PLAYER_2, RuedigerDerBot(Token.PLAYER_2)),
    boardWidth, boardHeight, null
) {

    init {
        (players[0] as PonderingBot).treeBuilder.scheduler = TreeBuilder.SimpleScheduler(1000000)
        (players[1] as PonderingBot).treeBuilder.scheduler = TreeBuilder.SimpleScheduler(1000000)
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
            while (!(currentPlayer as PonderingBot).treeBuilder.isIdle) {
                sleep(100)
            }
            val index = players[curPlayerInd].getColumnOfNextMove()
            val image = boardToImage(board)
            val player = when (players[curPlayerInd].side) {
                Token.PLAYER_1 -> "black"
                Token.PLAYER_2 -> "white"
                else -> throw IllegalStateException("Must be someones turn")
            }
            val newFile = File(Path.of(basePath, player, "$index", "${UUID.randomUUID()}").toUri())
            moves[currentPlayer.side]!!.add(Pair(image, newFile))
            println(board)
            println("Label $index")
            throwInColumn(index)
            winner = board.winner
        }
        for (pair in moves[winner]!!) {
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

    fun boardToImage(board: Board): BufferedImage {
        val image = BufferedImage(board.WIDTH, board.HEIGHT, BufferedImage.TYPE_BYTE_GRAY)
        // Define color mappings for player 1, player 2, and empty cells
        val colors = mapOf(
            Token.EMPTY to 127,
            Token.PLAYER_1 to 0,
            Token.PLAYER_2 to 255
        )
        for (x in 0 until board.WIDTH) {
            for (y in 0 until board.HEIGHT) {
                val token = board[x, y]
                val grayscaleValue = colors[token]!!
                image.setRGB(x, board.HEIGHT - y - 1, grayscaleValue shl 16 or (grayscaleValue shl 8) or grayscaleValue)
            }
        }
        return image
    }
}

/**
 * Images can be inverted and mirrored to create more data
 */
class DataAugmentation(val basePath: String = "/home/adrian/Schreibtisch/connect4dataset") {
    companion object {
        fun mirror() {

        }

        fun invert() {

        }
    }
}


fun main() {
    val samples = 1200
    val startTime = System.currentTimeMillis()
    ConsoleOutput.setAll(false, false, false, false, false, false, false, false, false)
    DatasetCreation(7, 6, basePath = "/home/adrian/Schreibtisch/connect4dataset", minimumSize = samples).create()
    val endTime = System.currentTimeMillis()
    val elapsedTimeMillis = endTime - startTime
    println("Created $samples samples elapsed: ${elapsedTimeMillis / 1000} seconds")
    exitProcess(0)
}