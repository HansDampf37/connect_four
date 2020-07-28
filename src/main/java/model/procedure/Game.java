package model.procedure;

import model.*;
import java.awt.image.BufferStrategy;

import bot.ConsoleOutput;

import java.awt.Graphics;

public abstract class Game  {
    private Display display;
    private BufferStrategy bs;
    private Graphics g;
    protected Board board = new Board();
    protected PlayerType[] players;
    protected int curPlayerInd;
    
    public Game() {
        definePlayers();
        // if (ConsoleOutput.printBoard) display = new Display();
        curPlayerInd = (int)(Math.random() * players.length);
    }

    protected abstract void definePlayers();

    protected void throwInColumn(int x) {
        if (board.throwInColumn(x, players[curPlayerInd].side)) {
            curPlayerInd = curPlayerInd == players.length - 1 ? 0 : curPlayerInd + 1;
        }
    }
    
    public void play() {
        Identifier winner = Identifier.EMPTY;
        // if (ConsoleOutput.printBoard)render();
        // if (ConsoleOutput.printBoard)render();
        // if (ConsoleOutput.printBoard)render();
        for (; winner == Identifier.EMPTY; winner = board.getWinner()){
            if (ConsoleOutput.printBoard) System.out.println(board);
            // if (ConsoleOutput.printBoard)render();
            throwInColumn(players[curPlayerInd].getColumnOfNextMove());
            if (!board.stillSpace()) break;
        }
        if (ConsoleOutput.printBoard) System.out.println(board);
        // if (ConsoleOutput.printBoard)render();
        if (ConsoleOutput.gameResult) {
            if (winner == Identifier.PLAYER_1) System.out.println("P 1 (X) won");
            else if(winner == Identifier.PLAYER_2) System.out.println("P 2 (O) won");
            else System.out.println("Its a draw");
            System.out.println(board);
        }
        for (PlayerType player : players) player.goodbye(winner);
        reset();
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if(bs == null) {
            display.getCanvas().createBufferStrategy(1);
            return;
        }
        g = bs.getDrawGraphics();
        board.render(g);
        bs.show();
        g.dispose();
    }

    protected void reset() {
        board = new Board();
        curPlayerInd = (int)(Math.random() * players.length);
        definePlayers();
    }
}