package bot;

import bot.tree.Node;
import bot.tree.Tree;
import model.Board;
import model.Field;
import model.Token;
import model.Player;

public abstract class PonderingBot extends Player {
    /**
     * The amount of moves this bot plans ahead
     */
    protected int forecast;
    /**
     * The player that did the first move
     */
    protected Token beginner = null;

    protected PonderingBot(Token side, Board board, int forecast) {
        super(side, board);
        this.forecast = forecast * 2;
    }

    @Override
    public int getColumnOfNextMove() {
        if (beginner == null) {
            for (Field f : board) if (!f.isEmpty()) beginner = Token.PLAYER_1 == side ? Token.PLAYER_2 : Token.PLAYER_1;
        }
        beginner = side;
        Tree states = new Tree(forecast, board.WIDTH);
        traverse(forecast, 0, states.getRoot(), side);
        int result = states.getRoot().indexOfNodeWithBestExpectationOfHighValue();
        int rating = states.getRoot().getValue();
        if (rating > Integer.MAX_VALUE - 20) System.out.println("Win inevitable");
        if (rating < Integer.MIN_VALUE + 20) System.out.println("Loss inevitable");
        System.out.println("Rating after own move: " + rating);
        return result;
    }

    /**
     * Recursive method that lets a {@link Tree} represent the game's states. Every
     * leave's value is set to the according state's rating
     *
     * @param forecast    amount of moves the algorithm is planning ahead
     * @param lvl         the current level
     * @param currentNode the current node
     */
    protected void traverse(int forecast, int lvl, Node currentNode, Token player) {
        Token winner = board.getWinner();
        if (winner != Token.EMPTY) {
            currentNode.makeLeave();
            if (winner == side) {
                currentNode.setValue(Integer.MAX_VALUE);
            } else {
                currentNode.setValue(Integer.MIN_VALUE);
            }
        } else if (lvl == forecast) {
            currentNode.makeLeave();
            currentNode.setValue(rateState());
        } else {
            for (int i = 0; i < board.WIDTH; i++) {
                if (board.throwInColumn(i, player)) {
                    traverse(forecast, lvl + 1, currentNode.getChild(i),
                            player == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
                    board.removeOfColumn(i);
                } else {
                    currentNode.getChild(i).setInvisible();
                }
            }
        }
    }

    public int getForecast() {
        return forecast;
    }

    /**
     * evaluates the state of the current board
     * @return high return values are associated with a good rating
     */
    protected abstract int rateState();
}
