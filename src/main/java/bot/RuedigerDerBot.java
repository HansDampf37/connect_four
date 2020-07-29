package bot;

import  model.*;

/**
 * Ruediger is analyzing the board. He wants to maximize the amount of rows columns and diagonals that he can use to finish
 * and minimize the amount of rows columns and diagonals that the opponent can use to finish.
 */
public class RuedigerDerBot extends PonderingBot {
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
        ownPressureMap = new int[board.WIDTH][board.HEIGHT];
        opponentPressureMap = new int[board.WIDTH][board.HEIGHT];
    }

    public RuedigerDerBot(Identifier player, Board board, int forecast) {
        this(player, board);
        this.forecast = forecast;
	}

    private void buildPressureMatrix(Identifier player) {
        for (int y = 0; y < board.HEIGHT; y++) {
            for (int x = 0; x < board.WIDTH; x++) {
                int maximumForThisField = 0;
                if (board.get(x, y).getPlayer() == Identifier.EMPTY) {
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
                            Identifier currentPlayer = board.get(curX, y).getPlayer();
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
                            Identifier currentPlayer = board.get(curX, curY).getPlayer();
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
                            Identifier currentPlayer = board.get(curX, curY).getPlayer();
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
                            Identifier currentPlayer = board.get(x, curY).getPlayer();
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
        return x < 0 || x >= board.WIDTH;
    }

    private boolean outOfBoardY(int y) {
        return y < 0 || y >= board.HEIGHT;
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

    @Override
    protected int rateState() {
        int rating;
        buildPressureMatrix(side);
        buildPressureMatrix(side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        searchForPredicaments();
        //if this bot created a predicament and the opponent didn't, he won
        if (ownPredicamentInLine != -1 && opponentPredicamentInLine == -1) {
            rating = Integer.MAX_VALUE - ownPredicamentHeight;
            //System.out.println("Own predicament\n" + board + "\n" + mapToString());
        }
        //if the opponent created a predicament and this bot didn't, he lost
        else if (ownPredicamentInLine == -1 && opponentPredicamentInLine != -1) {
            rating = Integer.MIN_VALUE + oppPredicamentHeight;
            //System.out.println("Opponent predicament\n" + board + "\n" + mapToString());
        }
        //if both players created a predicament the player with the less moves to use his predicament wins
        else if (ownPredicamentInLine != -1) rating = ownPredicamentHeight <= oppPredicamentHeight ? Integer.MAX_VALUE - ownPredicamentHeight: Integer.MIN_VALUE + oppPredicamentHeight;
        else rating = sum(ownPressureMap) - sum(opponentPressureMap);
        return rating;
    }

    /**
     * Searches for a column with predicaments. Read {@link #ownPredicamentInLine}
     */
    private void searchForPredicaments() {
        ownPredicamentHeight = -1;
        oppPredicamentHeight = -1;
        ownPredicamentInLine = -1;
        opponentPredicamentInLine = -1;
        for (int x = 0; x < board.WIDTH; x++) {
            boolean oppPredicamentPossible = true;
            boolean ownPredicamentPossible = true;
            int base = 0;
            for (int y = board.HEIGHT - 1; y >= 0; y--) {
                if (board.get(x, y).isEmpty()) {
                    // if the opponent has a threat underneath a predicament it doesn't count since one cant use it
                    if (opponentPressureMap[x][y] == 3) ownPredicamentPossible = false;
                    if (ownPressureMap[x][y] == 3) oppPredicamentPossible = false;
                    if (!ownPredicamentPossible && !oppPredicamentPossible) break;
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

    private String mapToString() {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = board.HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < board.WIDTH; x++) {
                str.append(ownPressureMap[x][y]).append(",").append(opponentPressureMap[x][y]).append("|");
            }
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < board.WIDTH; x++) str.append("-").append(x + 1);
        return str.append("-").toString();
    }

    @Override
    public String getName() {
        return "Ruediger (" + forecast + ")";
    }
}