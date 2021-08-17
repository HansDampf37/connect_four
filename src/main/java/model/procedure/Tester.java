package model.procedure;

import bot.PonderingBot;
import bot.ratingfunctions.ruediger.IRuediger;
import bot.ratingfunctions.ruediger.RuedigerDerBot;
import model.Board;
import model.Token;

public class Tester {
    public Tester() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true);
        Board board = new Board(7, 6);
        new Game(new PonderingBot(Token.PLAYER_1, board, new IRuediger(Token.PLAYER_1)), new PonderingBot(Token.PLAYER_2, board, new RuedigerDerBot(Token.PLAYER_2))).play();
    }
}