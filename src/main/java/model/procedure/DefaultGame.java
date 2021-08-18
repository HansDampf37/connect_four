package model.procedure;

import bot.bots.PonderingBot;
import bot.ratingfunctions.ruediger.RuedigerDerBot;
import model.Board;
import model.HumanPlayer;
import model.Token;

public class DefaultGame {

    public DefaultGame() {
        Board b = new Board(7, 6);
        new Game(new HumanPlayer(Token.PLAYER_1, b), new PonderingBot(Token.PLAYER_2, b, new RuedigerDerBot(Token.PLAYER_2))).play();
    }
}