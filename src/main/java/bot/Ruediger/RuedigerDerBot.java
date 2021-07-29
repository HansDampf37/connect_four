package bot.Ruediger;

import model.Board;
import model.Token;

public class RuedigerDerBot extends IRuediger {

    public RuedigerDerBot(Token side, Board board, int forecast) {
        super(side, board, forecast);
    }

    @Override
    protected void enhanceMaps() {
        // only works when board has even height
        if (board.HEIGHT % 2 == 1) return;
        for (int x = 0; x < board.WIDTH; x++) {
            for (int y = 0; y < board.HEIGHT; y++) {
                if (beginner == side) {
                    //this bot should aim for lines 0, 2, 4, 6
                    if (ownThreatMap[x][y] == 3 && y % 2 == 0) ownThreatMap[x][y] = 6;
                    if (opponentThreatMap[x][y] == 3 && y % 2 == 1) opponentThreatMap[x][y] = 6;
                } else {
                    //this bot should aim for lines 1, 3, 5
                    if (opponentThreatMap[x][y] == 3 && y % 2 == 0) opponentThreatMap[x][y] = 6;
                    if (ownThreatMap[x][y] == 3 && y % 2 == 1) ownThreatMap[x][y] = 6;
                }
            }
        }
    }

}
