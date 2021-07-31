package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends Player {

    public HumanPlayer(Token side, Board board) {
        super(side, board);
    }

    @Override
    public int getColumnOfNextMove() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String s = br.readLine();
                if (s != null && s.matches("[1-" + board.WIDTH + "]")) return Integer.valueOf(s) - 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "Human";
    }
}