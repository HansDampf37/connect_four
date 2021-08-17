package bot;

import bot.Torben.TorbenDerBot;
import model.Board;
import model.Token;
import model.procedure.ConsoleOutput;
import model.procedure.Game;

public class Trainer {
    public void train(int intensity) {
        ConsoleOutput.setAll(false, false, false, false, false, false, true, false);
        for (int i = 0; i < intensity; i++) {
            System.out.println(i);
            Board b = new Board(7, 6);
            new Game(new TorbenDerBot(4, Token.PLAYER_1, b), new TorbenDerBot(4, Token.PLAYER_2, b)).play();
        }
    }
}