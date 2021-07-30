package bot.Ruediger;

import bot.PonderingBot;
import model.Board;
import model.Token;

public abstract class IRuediger extends PonderingBot {
    /**
     * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
     * in order to win the game and that are already occupied by this player
     */
    int[][] ownThreatMap;
    /**
     * For each field this matrix contains the maximum amount of fields in a row, column or diagonal that can use this field
     * in order to win the game and that are already occupied by the opponent
     */
    int[][] opponentThreatMap;
    /**
     * A predicament is a state that always wins the game for one player if played correctly. Assuming the forecast is great enough
     * to see those predicaments the bot always plays perfectly and wins the game. But there are situations where
     * predicaments aren't recognized by the tree-approach because their usages are too far in the future
     * for the forecast to see them. The only predicaments not seen by the forecast are the ones where there are two fields
     * that are threatened 3 times and that are directly on top of each other.
     * Additionally, there mustn't be another field underneath such a constellation that is threatened 3 times by the opponent.
     * These kinds of patterns are easy to find and if there is a column as described this variable contains its index.
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

    IRuediger(Token side, Board board, int forecast) {
        super(side, board, forecast);
        ownThreatMap = new int[board.WIDTH][board.HEIGHT];
        opponentThreatMap = new int[board.WIDTH][board.HEIGHT];
    }

    @Override
    protected int rateState() {
        int rating;
        buildPressureMatrix(side);
        buildPressureMatrix(side == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
        searchForPredicaments();
        //enhanceMaps();
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
        //if both players created a predicament the player with the fewer moves to use his predicament wins
        else if (ownPredicamentInLine != -1) rating = ownPredicamentHeight <= oppPredicamentHeight ? Integer.MAX_VALUE - ownPredicamentHeight: Integer.MIN_VALUE + oppPredicamentHeight;
        else rating = evaluate(ownThreatMap) - evaluate(opponentThreatMap);
        return rating;
    }

    protected abstract void enhanceMaps();

    private void buildPressureMatrix(Token player) {
        for (int y = 0; y < board.HEIGHT; y++) {
            for (int x = 0; x < board.WIDTH; x++) {
                int maximumForThisField = 0;
                if (board.get(x, y).getPlayer() == Token.EMPTY) {
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
                            Token currentPlayer = board.get(curX, y).getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Token.EMPTY) {
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
                            Token currentPlayer = board.get(curX, curY).getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Token.EMPTY) {
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
                            Token currentPlayer = board.get(curX, curY).getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Token.EMPTY) {
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
                            Token currentPlayer = board.get(x, curY).getPlayer();
                            if (currentPlayer == player) {
                                currentValueForField++;
                            }
                            else if (currentPlayer != Token.EMPTY) {
                                currentValueForField = 0;
                                break;
                            }
                        }
                        maximumForThisField = Math.max(currentValueForField, maximumForThisField);
                    }
                }
                if (player == side) ownThreatMap[x][y] = maximumForThisField;
                else opponentThreatMap[x][y] = maximumForThisField;
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

    private int evaluate(int[][] threatMap) {
        int result = 0;
        for (int x = 0; x < threatMap.length; x++) {
            for (int y = 0; y < threatMap[x].length; y++) {
                result += Math.pow(threatMap[x][y], 3);
            }
        }
        return result;
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
            for (int y = 0; y < board.HEIGHT; y++) {
                if (board.get(x, y).isEmpty()) {
                    // if the opponent has a threat underneath a predicament it doesn't count since one can't use it
                    if (opponentThreatMap[x][y] == 3 && board.get(x, y).isEmpty()) ownPredicamentPossible = false;
                    if (ownThreatMap[x][y] == 3 && board.get(x, y).isEmpty()) oppPredicamentPossible = false;
                    if (!ownPredicamentPossible && !oppPredicamentPossible) break;
                    if (ownPredicamentPossible && ownThreatMap[x][y] == 3 && !outOfBoardY(y + 1) && ownThreatMap[x][y + 1] == 3) {
                        //Predicament found
                        ownPredicamentInLine = x;
                        ownPredicamentHeight = y - base;
                        //System.out.println("Found own predicament at " +  x + ", " + y);
                        //System.out.println(mapToString());
                        break;
                    }
                    if (oppPredicamentPossible && opponentThreatMap[x][y] == 3 && !outOfBoardY(y + 1) && opponentThreatMap[x][y + 1] == 3) {
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
                str.append(ownThreatMap[x][y]).append(",").append(opponentThreatMap[x][y]).append("|");
            }
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < board.WIDTH; x++) str.append("-").append(x + 1);
        return str.append("-").toString();
    }

    @Override
    public String getName() {
        return "Ruediger without enhancer (" + getForecast() + ")";
    }
}
