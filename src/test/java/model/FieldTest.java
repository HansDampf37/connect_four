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

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testSet() {
        f = new Field();
        assertEquals(f.getPlayer(), Identifier.EMPTY);
        assertTrue(f.isEmpty());
        f.setPlayer(Identifier.PLAYER_1);
        assertEquals(f.getPlayer(), Identifier.PLAYER_1);
        assertFalse(f.isEmpty());
        f.setPlayer(Identifier.PLAYER_2);
        assertEquals(f.getPlayer(), Identifier.PLAYER_2);
        assertFalse(f.isEmpty());
        f.setPlayer(Identifier.EMPTY);
        assertEquals(f.getPlayer(), Identifier.EMPTY);
        assertTrue(f.isEmpty());
    }

}
