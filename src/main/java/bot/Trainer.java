package bot;

import bot.Torben.TorbenDerBot;
import model.Identifier;
import model.procedure.Game;
import model.procedure.PlayerType;

public class Trainer extends Game {

	@Override
	protected void definePlayers() {
        players = new PlayerType[2];
        players[0] = new TorbenDerBot(Identifier.PLAYER_1, board, 4);
        players[1] = new TorbenDerBot(Identifier.PLAYER_2, board, 4);
    }
    
    public void train(int intensity) {
        ConsoleOutput.setAll(false, false, false, false, false, false, true, false);
        for (int i = 0; i < intensity; i++) {
            System.out.println(i);
            play();
        }
    }
}