package model

import java.lang.StringBuilder
import model.Token.EMPTY as EMPTY
import model.Token.PLAYER_1 as PLAYER_1
import model.Token.PLAYER_2 as PLAYER_2

class Board private constructor(val WIDTH: Int = 7,
            val HEIGHT: Int = 6,
            private val fields: Array<Array<Token>> = Array(WIDTH) { Array(HEIGHT) { EMPTY } }) : Iterable<Token>, Cloneable {

    constructor(width: Int = 7, height: Int = 6): this(width, height, Array(width) { Array(height) { EMPTY } })

    constructor(fields: Array<Array<Token>>): this(fields.size, fields[0].size, fields)

    fun throwInColumn(x: Int, player: Token): Boolean {
        for (y in 0 until HEIGHT) {
            if (fields[x][y] == EMPTY) {
                fields[x][y] = player
                return true
            }
        }
        return false
    }

    fun removeOfColumn(x: Int) {
        for (y in HEIGHT - 1 downTo 0) {
            if (fields[x][y] != EMPTY) {
                fields[x][y] = EMPTY
                return
            }
        }
    }

    val isEmpty: Boolean get() = all { it == EMPTY }

    val winner: Token
        get() {
            for (y in 0 until HEIGHT) {
                for (x in 0 until WIDTH) {
                    val res = checkFor4RowOnField(x, y)
                    if (res != EMPTY) return res
                }
            }
            return EMPTY
        }

    private fun checkFor4RowOnField(x: Int, y: Int): Token {
        val playerToCheck = fields[x][y]
        if (playerToCheck == EMPTY) return EMPTY
        var amountInRow = 1
        run {
            var dx = 1
            while (true) {
                if (x + dx >= WIDTH) break
                if (fields[x + dx][y] != playerToCheck) break
                amountInRow++
                dx++
            }
        }
        var dx = 1
        while (true) {
            if (x - dx < 0) break
            if (fields[x - dx][y] != playerToCheck) break
            amountInRow++
            dx++
        }
        if (amountInRow >= 4) return playerToCheck
        amountInRow = 1
        run {
            var dy = 1
            while (true) {
                if (y + dy >= HEIGHT) break
                if (fields[x][y + dy] != playerToCheck) break
                amountInRow++
                dy++
            }
        }
        var dy = 1
        while (true) {
            if (y - dy < 0) break
            if (fields[x][y - dy] != playerToCheck) break
            amountInRow++
            dy++
        }
        if (amountInRow >= 4) return playerToCheck
        amountInRow = 1
        run {
            var d = 1
            while (true) {
                if (x + d >= WIDTH || y + d >= HEIGHT) break
                if (fields[x + d][y + d] != playerToCheck) break
                amountInRow++
                d++
            }
        }
        run {
            var d = 1
            while (true) {
                if (x - d < 0 || y - d < 0) break
                if (fields[x - d][y - d] != playerToCheck) break
                amountInRow++
                d++
            }
        }
        if (amountInRow >= 4) return playerToCheck
        amountInRow = 1
        run {
            var d = 1
            while (true) {
                if (x + d >= WIDTH || y - d < 0) break
                if (fields[x + d][y - d] != playerToCheck) break
                amountInRow++
                d++
            }
        }
        var d = 1
        while (true) {
            if (x - d < 0 || y + d >= HEIGHT) break
            if (fields[x - d][y + d] != playerToCheck) break
            amountInRow++
            d++
        }
        return if (amountInRow >= 4) playerToCheck else EMPTY
    }

    override fun toString(): String {
        val str = StringBuilder().append("|")
        for (y in HEIGHT - 1 downTo 0) {
            for (x in 0 until WIDTH) {
                when (fields[x][y]) {
                    PLAYER_1 -> str.append("X|")
                    PLAYER_2 -> str.append("O|")
                    else -> str.append(" |")
                }
            }
            if (y != 0) str.append("\n|")
        }
        str.append("\n")
        for (x in 0 until WIDTH) str.append("-").append(x + 1)
        return str.append("-").toString()
    }

    fun stillSpace(): Boolean {
        return IntRange(0, WIDTH - 1).any { stillSpaceIn(it) }
        // for (x in 0 until WIDTH) if (stillSpaceIn(x)) return true
        // return false
    }

    fun stillSpaceIn(row: Int): Boolean {
        return fields[row][HEIGHT - 1] == EMPTY
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Board) return false
        if (other.HEIGHT != HEIGHT || other.WIDTH != WIDTH) return false
        for (x in 0 until WIDTH) {
            for (y in 0 until HEIGHT) {
                if (fields[x][y] != other.fields[x][y]) return false
            }
        }
        return true
    }

    operator fun get(x: Int, y: Int): Token {
        return fields[x][y]
    }

    override fun iterator(): Iterator<Token> {
        return BoardIterator()
    }

    internal inner class BoardIterator : Iterator<Token> {
        private var pointer = 0
        override fun hasNext(): Boolean {
            return pointer < HEIGHT * WIDTH
        }

        override fun next(): Token {
            val res = fields[pointer % WIDTH][pointer / WIDTH]
            pointer++
            return res
        }
    }

    init {
        require(!(WIDTH <= 0 || HEIGHT <= 0)) { "height and with must be natural numbers" }
    }

    public override fun clone(): Board {
        return Board(fields = Array(fields.size) { i -> fields[i].clone() })
    }
}