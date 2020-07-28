package model.procedure;

import model.Identifier;

public class DefaultGame extends Game {

    @Override
    protected void definePlayers() {
        players = new PlayerType[2];
        players[0] = new HumanPlayer(Identifier.PLAYER_1, board);
        players[1] = new HumanPlayer(Identifier.PLAYER_2, board);
    }
}