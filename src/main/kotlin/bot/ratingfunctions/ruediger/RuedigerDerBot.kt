package bot.ratingfunctions.ruediger

import bot.bots.RatingFunction
import model.Board
import model.Token
import model.Token.EMPTY
import model.procedure.ConsoleOutput
import kotlin.math.max
import kotlin.math.pow

open class RuedigerDerBot(private val side: Token) : RatingFunction {
    override fun name() = "Rüdiger"

    lateinit var cachedLastOwnThreadMap: Array<IntArray>
    lateinit var cachedLastOppThreadMap: Array<IntArray>

    override fun invoke(board: Board): Int {
        if (board.winner != EMPTY) return if (board.winner == side) Integer.MAX_VALUE else Integer.MIN_VALUE
        /**
         * The width of the Board that is currently evaluated
         */
        val width = board.WIDTH

        /**
         * The height of the Board that is currently evaluated
         */
        val height = board.HEIGHT

        /**
         * the height of an own predicament if there is one (@see [.ownPredicamentInLine])
         */
        val ownPredicamentHeight: Int

        /**
         * the height of an opponent's predicament if there is one (@see [.ownPredicamentInLine])
         */
        val oppPredicamentHeight: Int

        /**
         * A predicament is a state that always wins the game for one player if played correctly. Assuming the forecast is great enough
         * to see those predicaments the bot always plays perfectly and wins the game. But there are situations where
         * predicaments aren't recognized by the tree-approach because their usages are too far in the future
         * for the forecast to see them. The only predicaments not seen by the forecast are the ones where there are two fields
         * that are threatened 3 times and that are directly on top of each other.
         * Additionally, there mustn't be another field underneath such a constellation that is threatened 3 times by the opponent.
         * These kinds of patterns are easy to find and if there is a column as described this variable contains its index.
         */
        val ownPredicamentInLine: Int

        /**
         * See [.ownPredicamentInLine]
         */
        val opponentPredicamentInLine: Int

        /**
         * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
         * in order to win the game and that are already occupied by this player
         */
        val ownThreatMap: Array<IntArray> = Array(width) { IntArray(height) }

        /**
         * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
         * in order to win the game and that are already occupied by the opponent
         */
        val opponentThreatMap: Array<IntArray> = Array(width) { IntArray(height) }

        buildPressureMatrix(side, board, ownThreatMap, opponentThreatMap, width, height)
        buildPressureMatrix(side.other(), board, ownThreatMap, opponentThreatMap, width, height)
        val result = searchForPredicaments(board, ownThreatMap, opponentThreatMap, width, height)
        val own = result.first
        val opp = result.second
        ownPredicamentInLine = own.first
        ownPredicamentHeight = own.second
        opponentPredicamentInLine = opp.first
        oppPredicamentHeight = opp.second
        //if this bot created a predicament and the opponent didn't, he won
        val rating: Int = if (ownPredicamentInLine != -1 && opponentPredicamentInLine == -1) {
            Int.MAX_VALUE - ownPredicamentHeight - 1
        } else if (ownPredicamentInLine == -1 && opponentPredicamentInLine != -1) {
            Int.MIN_VALUE + oppPredicamentHeight + 1
        } else if (ownPredicamentInLine != -1) {
            if (ownPredicamentHeight <= oppPredicamentHeight) Int.MAX_VALUE - ownPredicamentHeight - 1 else Int.MIN_VALUE + oppPredicamentHeight + 1
        } else {
            evaluate(ownThreatMap) - evaluate(opponentThreatMap)
        }
        cachedLastOwnThreadMap = ownThreatMap
        cachedLastOppThreadMap = opponentThreatMap
        return rating
    }

    private fun buildPressureMatrix(
        player: Token,
        board: Board,
        ownThreatMap: Array<IntArray>,
        opponentThreatMap: Array<IntArray>,
        width: Int,
        height: Int
    ) {
        for (y in 0 until board.HEIGHT) {
            for (x in 0 until board.WIDTH) {
                var maximumForThisField = 0
                if (board[x, y] == EMPTY) {
                    var currentValueForField: Int
                    //horizontally
                    for (offset in -3..0) {
                        currentValueForField = 0
                        for (delta in 0..3) {
                            val curX = x + offset + delta
                            if (outOfBoardX(curX, width)) {
                                currentValueForField = 0
                                break
                            }
                            val currentPlayer = board[curX, y]
                            if (currentPlayer == player) {
                                currentValueForField++
                            } else if (currentPlayer != EMPTY) {
                                currentValueForField = 0
                                break
                            }
                        }
                        maximumForThisField = max(currentValueForField, maximumForThisField)
                    }
                    //diagonally
                    for (offset in -3..0) {
                        currentValueForField = 0
                        for (delta in 0..3) {
                            val curX = x + offset + delta
                            val curY = y + offset + delta
                            if (outOfBoard(curX, curY, width, height)) {
                                currentValueForField = 0
                                break
                            }
                            val currentPlayer = board[curX, curY]
                            if (currentPlayer == player) {
                                currentValueForField++
                            } else if (currentPlayer != EMPTY) {
                                currentValueForField = 0
                                break
                            }
                        }
                        maximumForThisField = max(currentValueForField, maximumForThisField)
                    }
                    //diagonally other direction
                    for (offset in -3..0) {
                        currentValueForField = 0
                        for (delta in 0..3) {
                            val curX = x - offset - delta
                            val curY = y + offset + delta
                            if (outOfBoard(curX, curY, width, height)) {
                                currentValueForField = 0
                                break
                            }
                            val currentPlayer = board[curX, curY]
                            if (currentPlayer == player) {
                                currentValueForField++
                            } else if (currentPlayer != EMPTY) {
                                currentValueForField = 0
                                break
                            }
                        }
                        maximumForThisField = max(currentValueForField, maximumForThisField)
                    }

                    //vertically
                    //for (offset in -3..0) {
                    //    currentValueForField = 0
                    //    for (delta in 0..3) {
                    //        val curY = y - offset - delta
                    //        if (outOfBoardY(curY)) {
                    //            currentValueForField = 0
                    //            break
                    //        }
                    //      val currentPlayer = board[x, curY]
                    //        if (currentPlayer == player) {
                    //            currentValueForField++
                    //        } else if (currentPlayer != EMPTY) {
                    //            currentValueForField = 0
                    //            break
                    //        }
                    //    }
                    //    maximumForThisField = max(currentValueForField, maximumForThisField)
                    //}
                }
                if (player == side) ownThreatMap[x][y] = maximumForThisField else opponentThreatMap[x][y] =
                    maximumForThisField
            }
        }
    }

    private fun outOfBoard(x: Int, y: Int, width: Int, height: Int): Boolean {
        return outOfBoardY(y, height) || outOfBoardX(x, width)
    }

    private fun outOfBoardX(x: Int, width: Int): Boolean {
        return x < 0 || x >= width
    }

    private fun outOfBoardY(y: Int, height: Int): Boolean {
        return y < 0 || y >= height
    }

    private fun evaluate(threatMap: Array<IntArray>): Int {
        var result = 0
        for (x in threatMap.indices) {
            for (y in 0 until threatMap[x].size) {
                result += threatMap[x][y].toDouble().pow(10.0).toInt()
            }
        }
        return result
    }

    /**
     * Searches for a column with predicaments. Read [.ownPredicamentInLine]
     */
    private fun searchForPredicaments(board: Board, ownThreatMap: Array<IntArray>, opponentThreatMap: Array<IntArray>, width: Int, height: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var ownPredicamentHeight = Integer.MAX_VALUE
        var oppPredicamentHeight = Integer.MAX_VALUE
        var ownPredicamentInLine = -1
        var opponentPredicamentInLine = -1
        for (x in 0 until board.WIDTH) {
            var oppPredicamentPossible = true
            var ownPredicamentPossible = true
            var base = 0
            for (y in 0 until board.HEIGHT) {
                if (board[x, y] == EMPTY) {
                    // if the opponent has a threat underneath a predicament it doesn't count since one can't use it
                    if (opponentThreatMap[x][y] == 3 && board[x, y] == EMPTY) ownPredicamentPossible = false
                    if (ownThreatMap[x][y] == 3 && board[x, y] == EMPTY) oppPredicamentPossible = false
                    if (!ownPredicamentPossible && !oppPredicamentPossible) break
                    if (ownPredicamentPossible && ownThreatMap[x][y] == 3 && !outOfBoardY(y + 1, height) && ownThreatMap[x][y + 1] == 3) {
                        //Predicament found
                        if (y - base <= ownPredicamentHeight) {
                            ownPredicamentInLine = x
                            ownPredicamentHeight = y - base
                            if (ConsoleOutput.predicamentSearch) {
                                println("Found own predicament at $x, $y")
                                println(board)
                                println(mapToString(width, height, ownThreatMap, opponentThreatMap))
                            }
                            break
                        }
                    }
                    if (oppPredicamentPossible && opponentThreatMap[x][y] == 3 && !outOfBoardY(y + 1, height) && opponentThreatMap[x][y + 1] == 3) {
                        //Predicament found
                        if (y - base < oppPredicamentHeight) {
                            opponentPredicamentInLine = x
                            oppPredicamentHeight = y - base
                            if (ConsoleOutput.predicamentSearch) {
                                println("Found opponent predicament at $x, $y")
                                println(board)
                                println(mapToString(width, height, ownThreatMap, opponentThreatMap))
                            }
                            break
                        }
                    }
                } else {
                    base++
                }
            }
        }

        if (ConsoleOutput.predicamentSearch) println("No further predicaments were found")
        return Pair(Pair(ownPredicamentInLine, ownPredicamentHeight), Pair(opponentPredicamentInLine, oppPredicamentHeight))
    }

    private fun mapToString(width: Int, height: Int, ownThreatMap: Array<IntArray>, opponentThreatMap: Array<IntArray>): String {
        val str = StringBuilder().append("|")
        for (y in (0 until height).reversed()) {
            for (x in 0 until width) {
                str.append(ownThreatMap[x][y]).append(",").append(opponentThreatMap[x][y]).append("|")
            }
            if (y != 0) str.append("\n|")
        }
        str.append("\n")
        for (x in 0 until width) str.append("--").append(x + 1).append("-")
        return str.append("--").toString()
    }
}