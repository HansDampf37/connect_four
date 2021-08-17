import bot.Ruediger.RuedigerDerBot;
import model.HumanPlayer;
import model.procedure.Game;

public class App {
    public static void main(String[] args) {
        Game game = new Game(new HumanPlayer(), new RuedigerDerBot(4));
        game.play();     
    }
}