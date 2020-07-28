package bot;

import bot.tree.Node;
import bot.tree.Tree;
import  model.*;

import model.procedure.PlayerType;

/**
 * Ruediger is analyzing the board. He wants to maximize the amount of rows columns and diagonals that he can use to finish
 * and minimize the amount of rows columns and diagonals that the opponent can use to finish.
 */
public class RuedigerDerBot extends PlayerType {
    /**
     * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
     * in order to win the game and that are already occupied by this player
     */
    private int[][] ownPressureMap;
    /**
     * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
     * in order to win the game and that are already occupied by the opponent
     */
    private int[][] opponentPressureMap;
    /**
     * The amount of moves this bot plans ahead
     */
    private int forecast;
    /**
     * A predicament is a state that always wins the game for one player if played correctly. Assuming the forecast is great enough
     * to see those predicaments the bot always plays perfectly and wins the game. But there are situations where
     * predicaments aren't recognized by the tree-approach because their usages are too far in the future
     * for the forecast to see them. The only predicaments not seen by te forecast are the ones where there are two fields
     * that are pressured 3 times and that are directly on top of each other.
     * Additionally there mustn't be another field underneath such a constellation that is threatened 3 times by the opponent.
     * These kind of patterns are easy to find and if there is a column as described this variable contains its index.
     */
    private int ownPredicamentInLine = -1;
    /**
     * See {@link #ownPredicamentInLine}
     */
    private int opponentPredicamentInLine = -1;
    /**
     * the height of an own predicament if there is one (read {@link #ownPredicamentInLine})
     */
    private int ownPredicamentHeight = -1;
    /**
     * the height of an opponent's predicament if there is one (read {@link #ownPredicamentInLine})
     */
    private int oppPredicamentHeight = -1;

    private RuedigerDerBot(Identifier side, Board board) {
        super(side, board);
        ownPressureMap = new int[Board.WIDTH][Board.HEIGHT];
        opponentPressureMap = new int[Board.WIDTH][Board.HEIGHT];
    }

    RuedigerDerBot(Identifier player2, Board board, int forecast) {
        this(player2, board);
        this.forecast = forecast;
	}

	@Override
    public int getColumnOfNextMove() {
        // builds a tree
        Tree states = new Tree(forecast);
        // makes the tree represent the games states
        traverse(forecast, 0, states.getRoot(), side);
        // gets the best following state
        int i = states.getRoot().indexOfNodeWithBestExpectationOfHighValue();
        board.throwInColumn(i, side);
        buildPressureMatrix(side);
        buildPressureMatrix(side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        System.out.println(mapToString());
        board.removeOfColumn(i);
        return i;
    }

    private void buildPressureMatrix(Identifier player) {
        Field[][] fields = board.getFields();
        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                int maximumForThisField = 0;
                if (fields[x][y].getPlayer() == Identifier.EMPTY) {
                    int currentValueForField;
                    //horizontally
                    for (int offset = -3; offset <= 0; offset++) {
                        currentValueForField = 0;
                        for (int delta = 0; delta < 4; delta++) {
                            int curX = x + offset + delta;
                            if (outOfBoardX(curX)) {
                                currentValueForField = 0;
                                break;
                            }
                            Identifier currentPlayer = fields[curX][y].getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Identifier.EMPTY) {
                                currentValueForField = 0;
                                break;
                            }
                        }
                        maximumForThisField = Math.max(currentValueForField, maximumForThisField);
                    }
                    //diagonally
                    for (int offset = -3; offset <= 0; offset++) {
                        currentValueForField = 0;
                        for (int delta = 0; delta < 4; delta++) {
                            int curX = x + offset + delta;
                            int curY = y + offset + delta;
                            if (outOfBoard(curX, curY)) {
                                currentValueForField = 0;
                                break;
                            }
                            Identifier currentPlayer = fields[curX][curY].getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Identifier.EMPTY) {
                                currentValueForField = 0;
                                break;
                            }
                        }
                        maximumForThisField = Math.max(currentValueForField, maximumForThisField);
                    }
                    //diagonally other direction
                    for (int offset = -3; offset <= 0; offset++) {
                        currentValueForField = 0;
                        for (int delta = 0; delta < 4; delta++) {
                            int curX = x - offset - delta;
                            int curY = y + offset + delta;
                            if (outOfBoard(curX, curY)) {
                                currentValueForField = 0;
                                break;
                            }
                            Identifier currentPlayer = fields[curX][curY].getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Identifier.EMPTY) {
                                currentValueForField = 0;
                                break;
                            }
                        }
                        maximumForThisField = Math.max(currentValueForField, maximumForThisField);
                    }

                    //vertically
                    for (int offset = -3; offset <= 0; offset++) {
                        currentValueForField = 0;
                        for (int delta = 0; delta < 4; delta++) {
                            int curY = y - offset - delta;
                            if (outOfBoardY(curY)) {
                                currentValueForField = 0;
                                break;
                            }
                            Identifier currentPlayer = fields[x][curY].getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Identifier.EMPTY) {
                                currentValueForField = 0;
                                break;
                            }
                        }
                        maximumForThisField = Math.max(currentValueForField, maximumForThisField);
                    }
                }
                if (player == side) ownPressureMap[x][y] = maximumForThisField;
                else opponentPressureMap[x][y] = maximumForThisField;
            }
        }
    }

    private boolean outOfBoard(int x, int y) {
        return outOfBoardY(y) || outOfBoardX(x);
    }

    private boolean outOfBoardX(int x) {
        return x < 0 || x >= Board.WIDTH;
    }

    private boolean outOfBoardY(int y) {
        return y < 0 || y >= Board.HEIGHT;
    }

    private int sum(int[][] mtx) {
        int result = 0;
        for (int x = 0; x < mtx.length; x++) {
            for (int y = 0; y < mtx[x].length; y++) {
                result += mtx[x][y];
            }
        }
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
    private void traverse(int forecast, int lvl, Node currentNode, Identifier player) {
        Identifier winner = board.getWinner();
        if (winner != Identifier.EMPTY) {
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
            for (int i = 0; i < Board.WIDTH; i++) {
                if (board.throwInColumn(i, player)) {
                    traverse(forecast, lvl + 1, currentNode.getChild(i),
                            player == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
                    board.removeOfColumn(i);
                } else {
                    currentNode.getChild(i).setInvisible();
                }
            }
        }
    }

    private int rateState() {
        int rating;
        buildPressureMatrix(side);
        buildPressureMatrix(side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        searchForPredicaments();
        //if this bot created a predicament and the opponent didn't, he won
        if (ownPredicamentInLine != -1 && opponentPredicamentInLine == -1) rating = Integer.MAX_VALUE - ownPredicamentHeight;
        //if the opponent created a predicament and this bot didn't, he lost
        else if (ownPredicamentInLine == -1 && opponentPredicamentInLine != -1) rating = Integer.MIN_VALUE + oppPredicamentHeight;
        //if both players created a predicament the player with the less moves to use his predicament wins
        else if (ownPredicamentInLine != -1) rating = ownPredicamentHeight <= oppPredicamentHeight ? Integer.MAX_VALUE - ownPredicamentHeight: Integer.MIN_VALUE + oppPredicamentHeight;
        else rating = sum(ownPressureMap) - sum(opponentPressureMap);
        return rating;
    }

    private void searchForPredicaments() {
        Field[][] fields = board.getFields();
        for (int x = 0; x < Board.WIDTH; x++) {
            boolean oppPredicamentPossible = true;
            boolean ownPredicamentPossible = true;
            int base = 0;
            for (int y = Board.HEIGHT - 1; y >= 0; y--) {
                if (fields[x][y].isEmpty()) {
                    // if the opponent has a threat underneath a predicament it doesn't count since one cant use it
                    if (opponentPressureMap[x][y] == 3) ownPredicamentPossible = false;
                    if (ownPressureMap[x][y] == 3) oppPredicamentPossible = false;
                    if (ownPredicamentPossible && ownPressureMap[x][y] == 3 && !outOfBoardY(y + 1) && ownPressureMap[x][y + 1] == 3) {
                        //Predicament found
                        ownPredicamentInLine = x;
                        ownPredicamentHeight = y - base;
                        //System.out.println("Found own predicament at " +  x + ", " + y);
                        //System.out.println(mapToString());
                        break;
                    }
                    if (oppPredicamentPossible && opponentPressureMap[x][y] == 3 && !outOfBoardY(y + 1) && opponentPressureMap[x][y + 1] == 3) {
                        //Predicament found
                        opponentPredicamentInLine = x;
                        oppPredicamentHeight = y - base;
                        //System.out.println("Found opponent predicament at " + x + ", " + y);
                        //System.out.println(mapToString());
                        break;
                    }
                } else {
                    base++;
                }
            }
        }
    }

    public String mapToString() {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = Board.HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < Board.WIDTH; x++) {
                str.append(ownPressureMap[x][y]).append(",").append(opponentPressureMap[x][y]).append("|");
            }
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < Board.WIDTH; x++) str.append("--");
        return str.append("-").toString();
    }

    @Override
    public void goodbye(Identifier winner) {
        if (winner == side) System.out.println("Rüdiger: Reckt by Rüdiger");
    }
}