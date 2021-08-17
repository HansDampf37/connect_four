package model.procedure;

import bot.Ruediger.RuedigerDerBot;
import model.HumanPlayer;
import model.Token;
import model.Player;

public class DefaultGame {

    public DefaultGame() {
        new Game(new HumanPlayer(), new RuedigerDerBot(4)).play();
    }
}