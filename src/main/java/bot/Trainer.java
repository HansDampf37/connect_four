package bot;

import bot.Torben.TorbenDerBot;
import model.Token;
import model.procedure.ConsoleOutput;
import model.procedure.Game;
import model.Player;

public class Trainer {
    public void train(int intensity) {
        ConsoleOutput.setAll(false, false, false, false, false, false, true, false);
        for (int i = 0; i < intensity; i++) {
            System.out.println(i);
            new Game(new TorbenDerBot(4), new TorbenDerBot(4)).play();
        }
    }
}