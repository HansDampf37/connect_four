package model.procedure;

import bot.Ruediger.RuedigerDerBot;
import bot.Ruediger.RuedigerOhneEnhancer;
import model.Board;
import model.Token;
import model.Player;

public class Tester {
    public Tester() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true);
        Board board = new Board(7, 6);
        new Game(new RuedigerDerBot(4, Token.PLAYER_1, board), new RuedigerDerBot(4, Token.PLAYER_2, board)).play();
    }
}