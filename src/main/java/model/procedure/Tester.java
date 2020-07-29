package model.procedure;

import bot.RuedigerDerBot;
import model.Identifier;
import model.procedure.ConsoleOutput;
import model.procedure.Game;
import model.HumanPlayer;
import model.Player;

public class Tester extends Game {

    @Override
    protected void definePlayers() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true);
        players = new Player[2];
        players[1] = new RuedigerDerBot(Identifier.PLAYER_1, board, 4);
        players[0] = new RuedigerDerBot(Identifier.PLAYER_2, board, 6);
    }

}