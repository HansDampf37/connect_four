package model.procedure;

import bot.Ruediger.RuedigerDerBot;
import bot.Ruediger.RuedigerOhneEnhancer;
import model.Token;
import model.Player;

public class Tester extends Game {

    @Override
    protected void definePlayers() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true);
        players = new Player[2];
        int index = (int)Math.round(Math.random());
        players[index] = new RuedigerDerBot(Token.PLAYER_1, board, 4);
        players[index == 0 ? 1 : 0] = new RuedigerOhneEnhancer(Token.PLAYER_2, board, 4);
    }

}