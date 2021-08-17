package model

import java.lang.StringBuilder

class Board private constructor(val WIDTH: Int = 7,
            val HEIGHT: Int = 6,
            private val fields: Array<Array<Field>> = Array(WIDTH) { Array(HEIGHT) { Field() } }) : Iterable<Field>, Cloneable {

    constructor(width: Int = 7, height: Int = 6): this(width, height, Array(width) { Array(height) { Field() } })

    constructor(fields: Array<Array<Field>>): this(fields.size, fields[0].size, fields)

    fun throwInColumn(x: Int, player: Token): Boolean {
        for (y in 0 until HEIGHT) {
            if (fields[x][y].isEmpty) {
                fields[x][y].player = player
                return true
            }
        }
        return false
    }

    fun removeOfColumn(x: Int) {
        for (y in HEIGHT - 1 downTo 0) {
            if (!fields[x][y].isEmpty) {
                fields[x][y].player = Token.EMPTY
                return
            }
        }
    }

    val winner: Token
        get() {
            for (y in 0 until HEIGHT) {
                for (x in 0 until WIDTH) {
                    val res = checkFor4RowOnField(x, y)
                    if (res != Token.EMPTY) return res
                }
            }
            return Token.EMPTY
        }

    private fun checkFor4RowOnField(x: Int, y: Int): Token {
        val playerToCheck = fields[x][y].player
        if (playerToCheck == Token.EMPTY) return Token.EMPTY
        var amountInRow = 1
        run {
            var dx = 1
            while (true) {
                if (x + dx >= WIDTH) break
                if (fields[x + dx][y].player != playerToCheck) break
                amountInRow++
                dx++
            }
        }
        var dx = 1
        while (true) {
            if (x - dx < 0) break
            if (fields[x - dx][y].player != playerToCheck) break
            amountInRow++
            dx++
        }
        if (amountInRow >= 4) return playerToCheck
        amountInRow = 1
        run {
            var dy = 1
            while (true) {
                if (y + dy >= HEIGHT) break
                if (fields[x][y + dy].player != playerToCheck) break
                amountInRow++
                dy++
            }
        }
        var dy = 1
        while (true) {
            if (y - dy < 0) break
            if (fields[x][y - dy].player != playerToCheck) break
            amountInRow++
            dy++
        }
        if (amountInRow >= 4) return playerToCheck
        amountInRow = 1
        run {
            var d = 1
            while (true) {
                if (x + d >= WIDTH || y + d >= HEIGHT) break
                if (fields[x + d][y + d].player != playerToCheck) break
                amountInRow++
                d++
            }
        }
        run {
            var d = 1
            while (true) {
                if (x - d < 0 || y - d < 0) break
                if (fields[x - d][y - d].player != playerToCheck) break
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
                if (fields[x + d][y - d].player != playerToCheck) break
                amountInRow++
                d++
            }
        }
        var d = 1
        while (true) {
            if (x - d < 0 || y + d >= HEIGHT) break
            if (fields[x - d][y + d].player != playerToCheck) break
            amountInRow++
            d++
        }
        return if (amountInRow >= 4) playerToCheck else Token.EMPTY
    }

    override fun toString(): String {
        val str = StringBuilder().append("|")
        for (y in HEIGHT - 1 downTo 0) {
            for (x in 0 until WIDTH) {
                when (fields[x][y].player) {
                    Token.PLAYER_1 -> str.append("X|")
                    Token.PLAYER_2 -> str.append("O|")
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
        return fields[row][HEIGHT - 1].player == Token.EMPTY
    }

    operator fun get(x: Int, y: Int): Field {
        return fields[x][y]
    }

    override fun iterator(): Iterator<Field> {
        return BoardIterator()
    }

    internal inner class BoardIterator : Iterator<Field> {
        private var pointer = 0
        override fun hasNext(): Boolean {
            return pointer < HEIGHT * WIDTH
        }

        override fun next(): Field {
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