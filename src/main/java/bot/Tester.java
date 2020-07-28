package bot;

import model.Identifier;
import model.procedure.Game;
import model.procedure.HumanPlayer;
import model.procedure.PlayerType;

public class Tester extends Game {

    @Override
    protected void definePlayers() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true);
        players = new PlayerType[2];
        players[0] = new HumanPlayer(Identifier.PLAYER_1, board);
        players[1] = new RuedigerDerBot(Identifier.PLAYER_2, board, 5);
    }

}