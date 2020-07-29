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
        int index = (int)Math.round(Math.random());
        players[index] = new HumanPlayer(Identifier.PLAYER_1, board);
        players[index == 0 ? 1 : 0] = new RuedigerDerBot(Identifier.PLAYER_2, board, 4);
    }

}