package bot.ratingfunctions.ruediger

import bot.bots.RatingFunction
import model.Board
import model.Token
import model.Token.EMPTY
import kotlin.math.max
import kotlin.math.pow

open class IRuediger(val side: Token) : RatingFunction {

    override fun name() = "Rüdiger"

    /**
     * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
     * in order to win the game and that are already occupied by this player
     */
    lateinit var ownThreatMap: Array<IntArray>

    /**
     * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
     * in order to win the game and that are already occupied by the opponent
     */
    lateinit var opponentThreatMap: Array<IntArray>

    /**
     * A predicament is a state that always wins the game for one player if played correctly. Assuming the forecast is great enough
     * to see those predicaments the bot always plays perfectly and wins the game. But there are situations where
     * predicaments aren't recognized by the tree-approach because their usages are too far in the future
     * for the forecast to see them. The only predicaments not seen by the forecast are the ones where there are two fields
     * that are threatened 3 times and that are directly on top of each other.
     * Additionally, there mustn't be another field underneath such a constellation that is threatened 3 times by the opponent.
     * These kinds of patterns are easy to find and if there is a column as described this variable contains its index.
     */
    private var ownPredicamentInLine = -1

    /**
     * See [.ownPredicamentInLine]
     */
    private var opponentPredicamentInLine = -1

    /**
     * The width of the Board that is currently evaluated
     */
    protected var width: Int = 0

    /**
     * The height of the Board that is currently evaluated
     */
    protected var height: Int = 0

    /**
     * the height of an own predicament if there is one (@see [.ownPredicamentInLine])
     */
    private var ownPredicamentHeight = -1

    /**
     * the height of an opponent's predicament if there is one (@see [.ownPredicamentInLine])
     */
    private var oppPredicamentHeight = -1


    override fun invoke(board: Board): Int {
        if (board.winner != EMPTY) return if (board.winner == side) Integer.MAX_VALUE else Integer.MIN_VALUE
        width = board.WIDTH
        height = board.HEIGHT
        ownThreatMap = Array(width) { IntArray(height) }
        opponentThreatMap = Array(width) { IntArray(height) }

        buildPressureMatrix(side, board)
        buildPressureMatrix(side.other(), board)
        searchForPredicaments(board)
        //enhanceMaps();
        //if this bot created a predicament and the opponent didn't, he won
        val rating: Int = if (ownPredicamentInLine != -1 && opponentPredicamentInLine == -1) {
            Int.MAX_VALUE - ownPredicamentHeight - 1
            //System.out.println("Own predicament\n" + board + "\n" + mapToString());
        } else if (ownPredicamentInLine == -1 && opponentPredicamentInLine != -1) {
            Int.MIN_VALUE + oppPredicamentHeight + 1
            //System.out.println("Opponent predicament\n" + board + "\n" + mapToString());
        } else if (ownPredicamentInLine != -1) {
            if (ownPredicamentHeight <= oppPredicamentHeight) Int.MAX_VALUE - ownPredicamentHeight - 1 else Int.MIN_VALUE + oppPredicamentHeight + 1
        } else {
            evaluate(ownThreatMap) - evaluate(opponentThreatMap)
        }
        return rating
    }

    protected open fun enhanceMaps() = Unit

    private fun buildPressureMatrix(player: Token, board: Board) {
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
                            if (outOfBoardX(curX)) {
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
                            if (outOfBoard(curX, curY)) {
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
                            if (outOfBoard(curX, curY)) {
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
                    for (offset in -3..0) {
                        currentValueForField = 0
                        for (delta in 0..3) {
                            val curY = y - offset - delta
                            if (outOfBoardY(curY)) {
                                currentValueForField = 0
                                break
                            }
                            val currentPlayer = board[x, curY]
                            if (currentPlayer == player) {
                                currentValueForField++
                            } else if (currentPlayer != EMPTY) {
                                currentValueForField = 0
                                break
                            }
                        }
                        maximumForThisField = max(currentValueForField, maximumForThisField)
                    }
                }
                if (player == side) ownThreatMap[x][y] = maximumForThisField else opponentThreatMap[x][y] =
                    maximumForThisField
            }
        }
    }

    private fun outOfBoard(x: Int, y: Int): Boolean {
        return outOfBoardY(y) || outOfBoardX(x)
    }

    private fun outOfBoardX(x: Int): Boolean {
        return x < 0 || x >= width
    }

    private fun outOfBoardY(y: Int): Boolean {
        return y < 0 || y >= height
    }

    private fun evaluate(threatMap: Array<IntArray>): Int {
        var result = 0
        for (x in threatMap.indices) {
            for (y in 0 until threatMap[x].size) {
                result += threatMap[x][y].toDouble().pow(3.0).toInt()
            }
        }
        return result
    }

    /**
     * Searches for a column with predicaments. Read [.ownPredicamentInLine]
     */
    private fun searchForPredicaments(board: Board) {
        ownPredicamentHeight = -1
        oppPredicamentHeight = -1
        ownPredicamentInLine = -1
        opponentPredicamentInLine = -1
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
                    if (ownPredicamentPossible && ownThreatMap[x][y] == 3 && !outOfBoardY(y + 1) && ownThreatMap[x][y + 1] == 3) {
                        //Predicament found
                        ownPredicamentInLine = x
                        ownPredicamentHeight = y - base
                        //System.out.println("Found own predicament at " +  x + ", " + y);
                        //System.out.println(mapToString());
                        break
                    }
                    if (oppPredicamentPossible && opponentThreatMap[x][y] == 3 && !outOfBoardY(y + 1) && opponentThreatMap[x][y + 1] == 3) {
                        //Predicament found
                        opponentPredicamentInLine = x
                        oppPredicamentHeight = y - base
                        //System.out.println("Found opponent predicament at " + x + ", " + y);
                        //System.out.println(mapToString());
                        break
                    }
                } else {
                    base++
                }
            }
        }
    }

    private fun mapToString(): String {
        val str = StringBuilder().append("|")
        for (y in 0 until height) {
            for (x in 0 until width) {
                str.append(ownThreatMap[x][y]).append(",").append(opponentThreatMap[x][y]).append("|")
            }
            if (y != 0) str.append("\n|")
        }
        str.append("\n")
        for (x in 0 until width) str.append("-").append(x + 1)
        return str.append("-").toString()
    }
}