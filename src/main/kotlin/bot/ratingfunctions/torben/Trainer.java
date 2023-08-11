package bot.ratingfunctions.torben;

import bot.bots.PonderingBot;
import model.Board;
import model.Token;
import model.procedure.ConsoleOutput;
import model.procedure.Game;

public class Trainer {
    public void train(int intensity) {
        ConsoleOutput.setAll(false, false, false, false, false, false, true, false, false);
        for (int i = 0; i < intensity; i++) {
            System.out.println(i);
            Board b = new Board(7, 6);
            new Game(new PonderingBot(Token.PLAYER_1, new TorbenDerBot(Token.PLAYER_1)), new PonderingBot(Token.PLAYER_2, new TorbenDerBot(Token.PLAYER_2)), 7, 6, null).play();
        }
    }
}