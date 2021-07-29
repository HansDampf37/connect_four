package bot.Ruediger;

import model.Board;
import model.Token;

public class RuedigerOhneEnhancer extends IRuediger {

    public RuedigerOhneEnhancer(Token side, Board board, int forecast) {
        super(side, board, forecast);
    }

    @Override
    protected void enhanceMaps() {
        //DO NOTHING
    }
}
