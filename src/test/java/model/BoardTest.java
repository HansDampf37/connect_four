package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    private Board b;
    private final int height = 2;
    private final int width = 4;

    @Before
    public void setUp() {
        b = new Board(width, height);
    }

    @Test
    public void testSize() {
        assertEquals(height, b.HEIGHT);
        assertEquals(width, b.WIDTH);
    }

    @Test
    public void testInit() {
        b = new Board(width, height);
        for (Field f : b) {
            assertEquals(f.getPlayer(), Token.EMPTY);
        }
    }

    @Test
    public void testFull() {
        assertTrue(b.stillSpace());
        for (Field f : b) f.setPlayer(Token.PLAYER_1);
        assertFalse(b.stillSpace());
    }

    @Test
    public void testWinner() {
        for (Field f : b) f.setPlayer(Token.PLAYER_1);
        assertSame(b.getWinner(), Token.PLAYER_1);
    }

    @Test
    public void testNoWinner() {
        assertSame(b.getWinner(), Token.EMPTY);
    }

    @Test
    public void testThrowInColumn() {
        for (int y = 0; y < b.HEIGHT; y++) assertTrue(b.throwInColumn(0, Token.PLAYER_1));
        assertFalse(b.throwInColumn(0, Token.PLAYER_1));
    }

    @Test
    public void testRemoveOfColumn() {
        for (int y = 0; y < b.HEIGHT; y++) assertTrue(b.throwInColumn(0, Token.PLAYER_1));
        for (int y = 0; y < b.HEIGHT; y++) b.removeOfColumn(0);
        assertTrue(b.get(0, 0).isEmpty());
    }
}
