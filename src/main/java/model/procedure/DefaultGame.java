package model.procedure;

import model.HumanPlayer;
import model.Identifier;
import model.Player;

public class DefaultGame extends Game {

    @Override
    protected void definePlayers() {
        players = new Player[2];
        players[0] = new HumanPlayer(Identifier.PLAYER_1, board);
        players[1] = new HumanPlayer(Identifier.PLAYER_2, board);
    }
}