import model.procedure.Tester;
import model.procedure.Game;

public class App {
    public static void main(String[] args) {
        // Trainer tr = new Trainer();
        // tr.train(Integer.MAX_VALUE);
        Game game = new Tester();
        game.play();     
    }
}