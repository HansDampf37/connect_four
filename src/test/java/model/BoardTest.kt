package model

import TestUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
        Assert.assertEquals(3, board.HEIGHT)
        Assert.assertEquals(2, board.WIDTH)
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
        assertFalse(TestUtils.gameDraw.stillSpace())
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
        Assert.assertEquals(Token.PLAYER_1, TestUtils.wonP1.winner)
        Assert.assertEquals(Token.PLAYER_1, TestUtils.wonP1_second.winner)
        Assert.assertEquals(Token.PLAYER_2, TestUtils.wonP2.winner)
        Assert.assertEquals(Token.PLAYER_2, TestUtils.wonP2_lastMove.winner)
    }

    @Test
    fun testNoWinner() {
        Assert.assertSame(b.winner, Token.EMPTY)
        Assert.assertEquals(Token.EMPTY, TestUtils.gameAlmostDraw.winner)
        Assert.assertEquals(Token.EMPTY, TestUtils.gameDraw.winner)
        Assert.assertEquals(Token.EMPTY, TestUtils.gameProgressed.winner)
        Assert.assertEquals(Token.EMPTY, TestUtils.noPredicament.winner)
        Assert.assertEquals(Token.EMPTY, TestUtils.predicament1.winner)
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

    @Test
    fun testEquals() {
        val b1 = Board()
        val b2 = Board()
        for (x in 0 until b.WIDTH) {
            for (y in 0 until b.HEIGHT) {
                val token = listOf(Token.EMPTY, Token.PLAYER_1, Token.PLAYER_2).random()
                b1.throwInColumn(x, token)
                b2.throwInColumn(x, token)
            }
        }
        Assert.assertEquals(b2, b1)
    }

    @Test
    fun testNotEquals() {
        val b1 = Board()
        val b2 = Board()
        for (x in 0 until b.WIDTH) {
            for (y in 0 until b.HEIGHT) {
                val token = listOf(Token.EMPTY, Token.PLAYER_1, Token.PLAYER_2).random()
                b1.throwInColumn(x, token)
                b2.throwInColumn(x, token)
            }
        }
        if (b1[0, 0] == Token.EMPTY) b1.throwInColumn(0, Token.PLAYER_1) else b1.removeOfColumn(0)
        Assert.assertNotEquals(b2, b1)
        Assert.assertNotEquals(Board(7, 6), Board(7, 7))
    }
}