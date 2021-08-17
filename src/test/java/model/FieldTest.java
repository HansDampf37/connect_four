package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {
    private Field f;

    @Before
    public void setUp() {
        f = new Field();
    }

    @Test
    public void testSet() {
        f = new Field();
        assertEquals(f.getPlayer(), Token.EMPTY);
        assertTrue(f.isEmpty());
        f.setPlayer(Token.PLAYER_1);
        assertEquals(f.getPlayer(), Token.PLAYER_1);
        assertFalse(f.isEmpty());
        f.setPlayer(Token.PLAYER_2);
        assertEquals(f.getPlayer(), Token.PLAYER_2);
        assertFalse(f.isEmpty());
        f.setPlayer(Token.EMPTY);
        assertEquals(f.getPlayer(), Token.EMPTY);
        assertTrue(f.isEmpty());
    }

}
