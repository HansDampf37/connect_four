package model.procedure;

import bot.Ruediger.RuedigerDerBot;
import bot.Ruediger.RuedigerOhneEnhancer;
import model.Token;
import model.Player;

public class Tester {
    public Tester() {
        ConsoleOutput.setAll(true, true, false, false, false, false, true, true);
        new Game(new RuedigerDerBot(4), new RuedigerDerBot(4)).play();
    }
}