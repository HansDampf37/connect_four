package model.procedure;

import model.Board;
import model.Identifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends PlayerType {

    public HumanPlayer(Identifier side, Board board) {
        super(side, board);
    }

    @Override
    public int getColumnOfNextMove() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String s = br.readLine();
                if (s.matches("[1-7]")) return Integer.valueOf(s) - 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void goodbye(Identifier winner) {
        System.out.println(winner == side ? "Die Krone der Schöpfung zieht sich zurück." : "Nicht schlecht");
    }
}