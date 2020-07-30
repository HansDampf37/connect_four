package bot.tree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TreeTest {
    private Tree tree;
    private int spread = 3;
    private int depth = 3;

    @Before
    public void setup() {
        tree = new Tree(spread, depth);
    }

    @Test
    public void testDFS() {
        int actualAmount = 0;
        for (Node n : tree) {
            actualAmount++;
        }
        int expectedAmount = 0;
        for (int j = 0; j <= spread; j++) expectedAmount += (int)Math.pow(spread, j);
        assertEquals(actualAmount, expectedAmount);
    }
}
