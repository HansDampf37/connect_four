package bot.ratingfunctions.torben

import model.Board
import model.Token
import java.lang.StringBuilder

/**
 * a pattern represents a special situation on the board
 */
class Pattern internal constructor(lines: Array<String>?) {
    private lateinit var pattern: Array<IntArray>
    private var width = 0
    private var height = 0
    private fun init(pattern: Array<IntArray>) {
        val maxY: Int = pattern[0].size
        for (x in pattern.indices) require(pattern[x].size == maxY) { "this Pattern is not squared" }
        this.pattern = pattern
        width = pattern.size
        height = pattern[0].size
        addFloor()
    }

    private fun addFloor() {
        var copy: Array<IntArray>
        for (x in 0 until width) {
            if (pattern[x][0] == ME || pattern[x][0] == EMPTY) {
                copy = Array(width) { IntArray(height + 1) }
                for (xVal in 0 until width) {
                    if (height >= 0) System.arraycopy(pattern[xVal], 0, copy[xVal], 1, height)
                }
                for (xVal in 0 until width) copy[xVal][0] = NOT_EMPTY
                height++
                pattern = copy
            }
        }
    }

    fun amountOfTimesThisPatternIsOnBoard(board: Board, player: Token): Int {
        var amounts = 0
        for (xOffs in 0..board.WIDTH - width) {
            for (yOffs in -1..board.HEIGHT - height) {
                if (searchWithOffset(xOffs, yOffs, board, player)) amounts++
            }
        }
        return amounts
    }

    private fun searchWithOffset(xOffset: Int, yOffset: Int, board: Board, player: Token): Boolean {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (y + yOffset < 0) {
                    if (pattern[x][y] != NOT_EMPTY) return false
                } else {
                    if (pattern[x][y] != NULL) {
                        val boardValue = board[x + xOffset, y + yOffset]
                        val patternValue = pattern[x][y]
                        if (patternValue == ME) {
                            if (boardValue != player) return false
                        } else if (patternValue == EMPTY) {
                            if (boardValue != Token.EMPTY) return false
                        } else if (patternValue == NOT_EMPTY) {
                            if (boardValue == Token.EMPTY) return false
                        }
                    }
                }
            }
        }
        return true
    }

    fun canContain(other: Pattern): Boolean {
        if (width < other.width) return false
        if (height < other.height) return false
        for (xOffs in 0..width - other.width) {
            for (yOffs in 0..height - other.height) {
                if (searchWithOffset(xOffs, yOffs, other)) return true
            }
        }
        return false
    }

    private fun searchWithOffset(xOffset: Int, yOffset: Int, otherpattern: Pattern): Boolean {
        for (x in 0 until otherpattern.width) {
            for (y in 0 until otherpattern.height) {
                val currentVal = pattern[x + xOffset][y + yOffset]
                if (currentVal == ME) if (!(otherpattern.pattern[x][y] == EMPTY || otherpattern.pattern[x][y] == ME || otherpattern.pattern[x][y] == NULL)) return false else if (currentVal == EMPTY) if (otherpattern.pattern[x][y] != EMPTY) return false else if (currentVal == NOT_EMPTY) {
                } else if (currentVal == NULL) {
                }
            }
        }
        return true
    }

    override fun toString(): String {
        val str = StringBuilder().append("|")
        for (y in height - 1 downTo 0) {
            for (x in 0 until width) {
                if (pattern[x][y] == ME) str.append("X|") else if (pattern[x][y] == NOT_EMPTY) str.append("-|") else if (pattern[x][y] == NULL) str.append(
                    "N|"
                ) else str.append(" |")
            }
            if (y != 0) str.append("\n|")
        }
        return str.toString()
    } // public static void main(String[] args) {

    //     Board b = new Board();
    //     b.throwInColumn(0, Player.PLAYER_1);
    //     b.throwInColumn(1, Player.PLAYER_1);
    //     b.throwInColumn(2, Player.PLAYER_1);
    //     b.throwInColumn(3, Player.PLAYER_1);
    //     b.throwInColumn(4, Player.PLAYER_1);
    //     b.throwInColumn(1, Player.PLAYER_1);
    //     b.throwInColumn(2, Player.PLAYER_1);
    //     b.throwInColumn(3, Player.PLAYER_1);
    //     b.throwInColumn(4, Player.PLAYER_1);
    //     System.out.println(b);
    //     int[][] init = {{ME}, {ME}, {ME}};
    //     int[][] init2 = {{ME}, {ME}, {ME}, {ME}};
    //     Pattern p = new Pattern(init);
    //     Pattern p2 = new Pattern(init2);
    //     System.out.println(p.canContain(p2));
    //     System.out.println(p.amountOfTimesThisPatternOccursOnBoard(b, Player.PLAYER_1));
    // }
    companion object {
        private const val ME = 1
        private const val NULL = 2
        private const val EMPTY = 0
        private const val NOT_EMPTY = -1
    }

    init {
        requireNotNull(lines) { "Empty input in pattern constructor" }
        val res = Array(lines[0].length) { IntArray(lines.size) }
        for (y in lines.indices) {
            val line = lines[y]
            for (x in line.indices) {
                when (line[x]) {
                    'x' -> res[x][y] = ME
                    ' ' -> res[x][y] = EMPTY
                    '-' -> res[x][y] = NOT_EMPTY
                    '0' -> res[x][y] = NULL
                    else -> {
                    }
                }
            }
        }
        init(res)
    }
}