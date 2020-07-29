package model.procedure;

import model.*;
import java.awt.image.BufferStrategy;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Game  {
    protected Board board = new Board(7, 6);
    protected Player[] players;
    private int curPlayerInd;
    
    public Game() {
        definePlayers();
        curPlayerInd = (int)(Math.random() * players.length);
    }

    protected abstract void definePlayers();

    private void throwInColumn(int x) {
        if (board.throwInColumn(x, players[curPlayerInd].getSide())) {
            curPlayerInd = curPlayerInd == players.length - 1 ? 0 : curPlayerInd + 1;
        }
    }
    
    public void play() {
        System.out.println(players[0].getName() + " vs " + players[1].getName());
        Identifier winner;
        for (winner = Identifier.EMPTY; winner == Identifier.EMPTY; winner = board.getWinner()){
            if (ConsoleOutput.printBoard) System.out.println(board);
            throwInColumn(players[curPlayerInd].getColumnOfNextMove());
            if (!board.stillSpace()) break;
        }
        //if (ConsoleOutput.printBoard) System.out.println(board);
        if (ConsoleOutput.gameResult) {
            Identifier finalWinner = winner;
            Arrays.stream(players).filter(p -> p.getSide() == finalWinner).forEach(p -> System.out.println(p.getName() + " won"));
            if (Arrays.stream(players).noneMatch(p -> p.getSide() == finalWinner)) System.out.println("Its a draw");
            System.out.println(board);
        }
        for (Player player : players) player.goodbye(winner);
        reset();
    }

    private void reset() {
        board = new Board(7, 6);
        curPlayerInd = (int)(Math.random() * players.length);
        definePlayers();
    }
}