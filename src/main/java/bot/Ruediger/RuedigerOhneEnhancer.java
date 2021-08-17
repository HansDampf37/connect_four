package bot.Ruediger;

import model.Board;
import model.Token;

public class RuedigerOhneEnhancer extends IRuediger {

    public RuedigerOhneEnhancer(int forecast, Token side, Board board) {
        super(forecast, side, board);
    }

    @Override
    protected void enhanceMaps() {
        //DO NOTHING
    }
}
