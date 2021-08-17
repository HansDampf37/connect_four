package model.procedure;

import bot.Ruediger.RuedigerDerBot;
import model.Board;
import model.HumanPlayer;
import model.Token;
import model.Player;

public class DefaultGame {

    public DefaultGame() {
        Board b = new Board(7, 6);
        new Game(new HumanPlayer(Token.PLAYER_1, b), new RuedigerDerBot(4, Token.PLAYER_2, b)).play();
    }
}