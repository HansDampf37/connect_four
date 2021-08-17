package bot.Ruediger;

import model.Board;
import model.HumanPlayer;
import model.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class IRuedigerTest {
    private Board b = new Board(7, 6);
    private IRuediger r = new RuedigerDerBot(Token.PLAYER_2, b, 3);
    private HumanPlayer h = new HumanPlayer(Token.PLAYER_1, b);

    @Before
    public void setup() {
        b.throwInColumn(0, r.getSide());
        b.throwInColumn(1, r.getSide());
        b.throwInColumn(3, r.getSide());
        b.throwInColumn(6, r.getSide());
        b.throwInColumn(2, h.getSide());
        b.throwInColumn(5, h.getSide());
        //
        b.throwInColumn(0, h.getSide());
        b.throwInColumn(2, h.getSide());
        b.throwInColumn(3, h.getSide());
        b.throwInColumn(5, h.getSide());
        b.throwInColumn(6, h.getSide());
        //
        b.throwInColumn(0, r.getSide());
        b.throwInColumn(2, r.getSide());
        b.throwInColumn(3, r.getSide());
        b.throwInColumn(5, h.getSide());
        b.throwInColumn(6, r.getSide());
        //
        b.throwInColumn(0, h.getSide());
        b.throwInColumn(2, r.getSide());
        b.throwInColumn(3, r.getSide());
        b.throwInColumn(5, r.getSide());
        b.throwInColumn(6, h.getSide());
        //
        b.throwInColumn(0, r.getSide());
        b.throwInColumn(2, r.getSide());
        b.throwInColumn(3, h.getSide());
        //
        b.throwInColumn(0, h.getSide());
        b.throwInColumn(2, h.getSide());
        b.throwInColumn(3, r.getSide());
    }

    @Test
    public void testEval() {
        System.out.println(b.toString());
        System.out.println(r.rateState());
        System.out.println(print(r.ownThreatMap));
        System.out.println(print(r.opponentThreatMap));
    }

    private String print(int[][] map) {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = map[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < map.length; x++) str.append(map[x][y]).append("|");
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < map.length; x++) str.append("-").append(x + 1);
        return str.append("-").toString();
    }
}
