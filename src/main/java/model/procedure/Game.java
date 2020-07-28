package model.procedure;

import model.*;
import java.awt.image.BufferStrategy;

import java.awt.Graphics;

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
        Identifier winner = Identifier.EMPTY;
        for (; winner == Identifier.EMPTY; winner = board.getWinner()){
            if (ConsoleOutput.printBoard) System.out.println(board);
            throwInColumn(players[curPlayerInd].getColumnOfNextMove());
            if (!board.stillSpace()) break;
        }
        if (ConsoleOutput.printBoard) System.out.println(board);
        if (ConsoleOutput.gameResult) {
            if (winner == Identifier.PLAYER_1) System.out.println("P 1 (X) won");
            else if(winner == Identifier.PLAYER_2) System.out.println("P 2 (O) won");
            else System.out.println("Its a draw");
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