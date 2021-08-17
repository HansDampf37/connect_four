package model

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class BoardTest {
    private lateinit var b: Board
    private val height = 2
    private val width = 4

    @Before
    fun setUp() {
        b = Board(width, height)
    }

    @Test
    fun testSize() {
        Assert.assertEquals(height, b.HEIGHT)
        Assert.assertEquals(width, b.WIDTH)

        val board = Board(Array(2) {Array(3) {Token.EMPTY} })
        assertEquals(3, board.HEIGHT)
        assertEquals(2, board.WIDTH)
    }

    @Test
    fun testInit() {
        b = Board(width, height)
        for (f in b) {
            Assert.assertEquals(f, Token.EMPTY)
        }
    }

    @Test
    fun testFull() {
        Assert.assertTrue(b.stillSpace())
        for (x in 0 until b.WIDTH) {
            for (count in 0 until b.HEIGHT) {
                b.throwInColumn(x, Token.PLAYER_1)
            }
        }
        Assert.assertFalse(b.stillSpace())
    }

    @Test
    fun testWinner() {
        assertEquals(b.winner, Token.EMPTY)
        for (x in 0 until b.WIDTH) {
            for (count in 0 until b.HEIGHT) {
                b.throwInColumn(x, Token.PLAYER_1)
            }
        }
        Assert.assertSame(b.winner, Token.PLAYER_1)
    }

    @Test
    fun testNoWinner() {
        Assert.assertSame(b.winner, Token.EMPTY)
    }

    @Test
    fun testThrowInColumn() {
        for (y in 0 until b.HEIGHT) Assert.assertTrue(b.throwInColumn(0, Token.PLAYER_1))
        Assert.assertFalse(b.throwInColumn(0, Token.PLAYER_1))
    }

    @Test
    fun testRemoveOfColumn() {
        for (y in 0 until b.HEIGHT) Assert.assertTrue(b.throwInColumn(0, Token.PLAYER_1))
        for (y in 0 until b.HEIGHT) b.removeOfColumn(0)
        Assert.assertTrue(b[0, 0] == Token.EMPTY)
    }
}