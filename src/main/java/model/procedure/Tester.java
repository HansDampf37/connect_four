package model.procedure;

import bot.bots.PonderingBot;
import bot.ratingfunctions.ruediger.RuedigerDerBot;
import model.Board;
import model.Token;

public class Tester {
    public Tester() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true, true);
        Board board = new Board(7, 6);
        new Game(new PonderingBot(Token.PLAYER_1, new RuedigerDerBot(Token.PLAYER_1)), new PonderingBot(Token.PLAYER_2, new RuedigerDerBot(Token.PLAYER_2)), 7, 6).play();
    }
}