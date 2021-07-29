import model.procedure.DefaultGame;
import model.procedure.Tester;
import model.procedure.Game;

public class App {
    public static void main(String[] args) {
        Game game = new DefaultGame();
        game.play();     
    }
}