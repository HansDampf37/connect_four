package model.procedure;

import bot.Ruediger.RuedigerDerBot;
import model.HumanPlayer;
import model.Token;
import model.Player;

public class DefaultGame extends Game {

    @Override
    protected void definePlayers() {
        players = new Player[2];
        players[0] = new HumanPlayer(Token.PLAYER_1, board);
        players[1] = new RuedigerDerBot(Token.PLAYER_2, board, 2);
    }
}