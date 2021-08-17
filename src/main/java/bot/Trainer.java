package bot;

import bot.ratingfunctions.torben.TorbenDerBot;
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
            new Game(new PonderingBot(Token.PLAYER_1, b, new TorbenDerBot(Token.PLAYER_1)), new PonderingBot(Token.PLAYER_2, b, new TorbenDerBot(Token.PLAYER_2))).play();
        }
    }
}