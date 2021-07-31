package bot.Torben

import model.Board
import bot.PonderingBot
import bot.tree.Tree
import model.Token
import model.procedure.ConsoleOutput
import java.io.*
import java.lang.StringBuilder

/**
 * This Bot analyzes the board and recognizes given patterns (for example 3 in a
 * row with a free space at the end). Every pattern is associated with a rating.
 * Out of all possible moves the bot picks the one with the highest accumulated
 * rating. Upon finishing a game the ratings of all patterns that were used by
 * the loser become decreased, and the ones of the patterns used by the winner
 * get increased. The In/Decrease itself is anti-proportional to the amount of
 * times theses patterns were used.
 */
class TorbenDerBot(side: Token?, board: Board?, forecast: Int) : PonderingBot(side, board, forecast) {
    /**
     * Array containing ratings for patterns
     */
    private var ratings: IntArray = IntArray(0)

    /**
     * Contains a set of hardcoded patterns
     */
    private val checker = PatternSet()

    /**
     * Element i represents the amount of times that pattern i has been used in
     * this game
     */
    private val ownOverallPatternUsage: IntArray

    /**
     * Element i represents the amount of times that pattern i is currently used on
     * the board
     */
    private val ownCurrentPatternUsage: IntArray

    /**
     * Element i represents the amount of times that pattern i has been used in
     * this game
     */
    private val opOverallPatternUsage: IntArray

    /**
     * Element i represents the amount of times that pattern i is currently used on
     * the board
     */
    private val oppCurrentPatternUsage: IntArray

    /**
     * Path to data
     */
    private val PATH = "game/src/main/java/bot/data.txt"

    /**
     * prints out overall useless information
     */
    private fun log() {
        if (ConsoleOutput.playerGreetings) {
            println(
                "--------------------------------------------------------------------------------------------------"
            )
            println(
                "------------------------------------------TORBEN DER BOT------------------------------------------"
            )
            println(
                "--------------------------------------------------------------------------------------------------"
            )
            println("Actively checking for " + checker.patternAmount + " patterns.")
        }
    }

    override fun getColumnOfNextMove(): Int {
        // checks which patterns have been used in the opponents last move
        updateOpponentsOverallPatternUsage()
        // checks which patterns he is currently using in order to compare them to later
        // TODO not accurate
        checkOwnCurrentPatternUsage()
        // builds a tree
        val states = Tree(forecast, board.WIDTH)
        // makes the tree represent the games states
        traverse(forecast, 0, states.root, side)
        // gets the best following state
        val bestColumn = states.root.indexOfNodeWithBestExpectationOfHighValue()
        // checks which patterns are used in this move
        // checks which patterns the opponent is currently using in order to compare
        // them to later TODO not accurate
        board.throwInColumn(bestColumn, side)
        updateOwnOverallPatternUsage()
        checkOpponentsCurrentPatternUsage()
        board.removeOfColumn(bestColumn)
        val rating = states.root.value
        if (rating > Int.MAX_VALUE - board.HEIGHT) {
            adapt(true)
        } else if (rating < Int.MIN_VALUE + board.HEIGHT) {
            adapt(false)
        }
        return bestColumn
    }

    private fun adapt(won: Boolean) {
        if (won) {
            for (i in 0 until checker.patternAmount) {
                if (ownOverallPatternUsage[i] != 0) ratings[i] += ownOverallPatternUsage[i] * ownOverallPatternUsage[i]
                if (opOverallPatternUsage[i] != 0) ratings[i] -= opOverallPatternUsage[i] * opOverallPatternUsage[i]
            }
        } else {
            for (i in 0 until checker.patternAmount) {
                if (ownOverallPatternUsage[i] != 0) ratings[i] -= ownOverallPatternUsage[i] * ownOverallPatternUsage[i]
                if (opOverallPatternUsage[i] != 0) ratings[i] += opOverallPatternUsage[i] * opOverallPatternUsage[i]
            }
        }
        writeRatings()
    }

    /**
     * checks which patterns are used by the opponent at the moment
     */
    private fun checkOpponentsCurrentPatternUsage() {
        for (i in 0 until checker.patternAmount) {
            oppCurrentPatternUsage[i] = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(
                board,
                if (side == Token.PLAYER_1) Token.PLAYER_2 else Token.PLAYER_1
            )
        }
    }

    /**
     * checks which patterns are used by itself at the moment
     */
    private fun checkOwnCurrentPatternUsage() {
        for (i in 0 until checker.patternAmount) {
            ownCurrentPatternUsage[i] = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(
                board,
                side
            )
        }
    }

    /**
     * checks if there were new patterns used since the last invocation of
     * [.checkOpponentsCurrentPatternUsage] and updates
     * [.opOverallPatternUsage]
     */
    private fun updateOpponentsOverallPatternUsage() {
        if (ConsoleOutput.patternRecognition) println("Opponent")
        for (i in 0 until checker.patternAmount) {
            val occurrence = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(
                board,
                if (side == Token.PLAYER_1) Token.PLAYER_2 else Token.PLAYER_1
            )
            if (ConsoleOutput.patternRecognition) if (occurrence > 0) println(
                """Pattern: 
${checker.getPattern(i)}    $occurrence
---------------
---------------"""
            )
            if (occurrence > oppCurrentPatternUsage[i]) {
                opOverallPatternUsage[i] += occurrence - oppCurrentPatternUsage[i]
            }
            ownCurrentPatternUsage[i] = occurrence
        }
    }

    /**
     * checks if there were new patterns used since the last invocation of
     * [.checkOwnCurrentPatternUsage] and updates
     * [.ownOverallPatternUsage]
     */
    private fun updateOwnOverallPatternUsage() {
        if (ConsoleOutput.patternRecognition) println("Torben")
        for (i in 0 until checker.patternAmount) {
            val occurence = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board, side)
            if (ConsoleOutput.patternRecognition) if (occurence > 0) println(
                """Pattern: 
${checker.getPattern(i)}    $occurence
---------------
---------------"""
            )
            if (occurence > ownCurrentPatternUsage[i]) {
                ownOverallPatternUsage[i] += occurence - ownCurrentPatternUsage[i]
            }
        }
    }

    /**
     * Rates the current board's state
     *
     * @return rating
     */
    override fun rateState(): Int {
        var rating = 0
        for (i in 0 until checker.patternAmount) {
            rating += ratings[i] * checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board, side)
        }
        for (i in 0 until checker.patternAmount) {
            rating -= ratings[i] * checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(
                board,
                if (side == Token.PLAYER_1) Token.PLAYER_2 else Token.PLAYER_1
            )
        }
        return rating
    }

    override fun getName(): String {
        return "Torben"
    }

    /**
     * get data input
     */
    private fun readRatings() {
        val fis: InputStream
        var data = ByteArray(0)
        try {
            fis = FileInputStream(File(PATH))
            data = ByteArray(fis.available())
            fis.read(data)
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val input = String(data)
        if (ConsoleOutput.botInitiation) println(input)
        val ratingsAr = input.split(",".toRegex()).toTypedArray()
        ratings = IntArray(checker.patternAmount)
        ratings[0] = if (ratingsAr[0].isEmpty()) 0 else Integer.valueOf(ratingsAr[0])
        for (i in 1 until ratingsAr.size) {
            ratings[i] = Integer.valueOf(ratingsAr[i])
        }
    }

    /**
     * get data output
     */
    private fun writeRatings() {
        val str = StringBuilder()
        for (i in 0 until ratings.size - 1) str.append(ratings[i]).append(",")
        str.append(ratings[ratings.size - 1])
        try {
            val out = FileOutputStream(File(PATH))
            out.write(str.toString().toByteArray())
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Constructor
     *
     * @param side  Player_1 or Player_2
     * @param board the board
     */
    init {
        log()
        readRatings()
        ownOverallPatternUsage = IntArray(checker.patternAmount)
        ownCurrentPatternUsage = IntArray(checker.patternAmount)
        opOverallPatternUsage = IntArray(checker.patternAmount)
        oppCurrentPatternUsage = IntArray(checker.patternAmount)
    }
}