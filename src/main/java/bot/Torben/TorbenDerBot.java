package bot.Torben;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import bot.ConsoleOutput;
import bot.tree.Tree;
import bot.tree.Node;
import model.*;
import model.procedure.*;

/**
 * This Bot analyzes the board and recognizes given patterns (for example 3 in a
 * row with a free space at the end). Every pattern is associated with a rating.
 * Out of all possible moves the bot picks the one with the highest accumulated
 * rating. Upon finishing a game the ratings of all patterns that were used by
 * the loser become decreased, and the ones of the patterns used by the winner
 * get increased. The In/Decrease itself is anti-proportional to the amount of
 * times theses patterns were used.
 */
public class TorbenDerBot extends PlayerType {
    /**
     * Array containing ratings for patterns
     */
    public int[] ratings;
    /**
     * Contains a set of hardcoded patterns
     */
    private final PatternChecker checker = new PatternChecker();
    /**
     * Element i represents the amount of times that pattern i has been used in
     * this game
     */
    private final int[] ownOverallPatternUsage;
    /**
     * Element i represents the amount of times that pattern i is currently used on
     * the board
     */
    private final int[] ownCurrentPatternUsage;
    /**
     * Element i represents the amount of times that pattern i has been used in
     * this game
     */
    private int[] opOverallPatternUsage;
    /**
     * Element i represents the amount of times that pattern i is currently used on
     * the board
     */
    private int[] opCurrentPatternUsage;
    /**
     * how many moves should the bot calculate in advance
     */
    private final int forecast;

    /**
     * Path to data
     */
    private final String PATH = "game/src/main/java/bot/data.txt";

    /**
     * Constructor
     * 
     * @param side  Player_1 or Player_2
     * @param board the board
     */
    public TorbenDerBot(final Identifier side, final Board board, int forecast) {
        super(side, board);
        log();
        readRatings();
        this.forecast = forecast;
        ownOverallPatternUsage = new int[checker.getPatternAmount()];
        ownCurrentPatternUsage = new int[checker.getPatternAmount()];
        opOverallPatternUsage = new int[checker.getPatternAmount()];
        opCurrentPatternUsage = new int[checker.getPatternAmount()];
    }

    /**
     * prints out overall useless information
     */
    private void log() {
        if (ConsoleOutput.playerGreetings) {
            System.out.println(
                    "--------------------------------------------------------------------------------------------------");
            System.out.println(
                    "------------------------------------------TORBEN DER BOT------------------------------------------");
            System.out.println(
                    "--------------------------------------------------------------------------------------------------");
            System.out.println("Actively checking for " + checker.getPatternAmount() + " patterns.");
        }
    }

    @Override
    public int getColumnOfNextMove() {
        // checks which patterns have been used in the opponents last move
        updateOpponentsOverallPatternUsage();
        // checks which patterns he is currently using in order to compare them to later
        // TODO not accurate
        checkOwnCurrentPatternUsage();

        // builds a tree
        Tree states = new Tree(forecast);
        // makes the tree represent the games states
        traverse(forecast, 0, states.getRoot(), side);
        // gets the best following state
        int bestColumn = states.getRoot().indexOfNodeWithBestExpectationOfHighValue();
        // checks which patterns are used in this move
        // checks which patterns the opponent is currently using in order to compare
        // them to later TODO not accurate
        board.throwInColumn(bestColumn, side);
        updateOwnOverallPatternUsage();
        checkOpponentsCurrentPatternUsage();
        board.removeOfColumn(bestColumn);
        return bestColumn;
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

    /**
     * checks which patterns are used by the opponent at the moment
     */
    private void checkOpponentsCurrentPatternUsage() {
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            ownCurrentPatternUsage[i] = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board.getFields(),
                    side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        }
    }

    /**
     * checks which patterns are used by itself at the moment
     */
    private void checkOwnCurrentPatternUsage() {
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            ownCurrentPatternUsage[i] = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board.getFields(),
                    side);
        }
    }

    /**
     * checks if there were new patterns used since the last invocation of
     * {@link #checkOpponentsCurrentPatternUsage()} and updates
     * {@link #opOverallPatternUsage}
     */
    private void updateOpponentsOverallPatternUsage() {
        if (ConsoleOutput.patternRecognition)
            System.out.println("Opponent");
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            int occurence = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board.getFields(),
                    side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
            if (ConsoleOutput.patternRecognition)
                if (occurence > 0)
                    System.out.println("Pattern: \n" + checker.getPattern(i) + "    " + occurence
                            + "\n---------------\n---------------");
            if (occurence > opCurrentPatternUsage[i]) {
                opOverallPatternUsage[i] += occurence - opCurrentPatternUsage[i];
            }
            ownCurrentPatternUsage[i] = occurence;
        }
    }

    /**
     * checks if there were new patterns used since the last invocation of
     * {@link #checkOwnCurrentPatternUsage()} and updates
     * {@link #ownOverallPatternUsage}
     */
    private void updateOwnOverallPatternUsage() {
        if (ConsoleOutput.patternRecognition)
            System.out.println("Torben");
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            int occurence = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board.getFields(), side);
            if (ConsoleOutput.patternRecognition)
                if (occurence > 0)
                    System.out.println("Pattern: \n" + checker.getPattern(i) + "    " + occurence
                            + "\n---------------\n---------------");
            if (occurence > ownCurrentPatternUsage[i]) {
                ownOverallPatternUsage[i] += occurence - ownCurrentPatternUsage[i];
            }
        }
    }

    /**
     * Rates the current board's state
     * 
     * @return rating
     */
    private int rateState() {
        int rating = 0;
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            rating += ratings[i] * checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board.getFields(), side);
        }
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            rating -= ratings[i] * checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board.getFields(),
                    side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        }
        return rating;
    }

    /**
     * adapts ratings for patterns relative to winning or losing the game
     */
    @Override
    public void goodbye(final Identifier winner) {
        if (winner == side) {
            for (int i = 0; i < checker.getPatternAmount(); i++) {
                if (ownOverallPatternUsage[i] != 0)
                    ratings[i] += (ownOverallPatternUsage[i] * ownOverallPatternUsage[i]);
                if (opOverallPatternUsage[i] != 0)
                    ratings[i] -= (opOverallPatternUsage[i] * opOverallPatternUsage[i]);
            }
            if (ConsoleOutput.playerGreetings)
                System.out.println("Torben: GG eZ");
        } else if (winner == (side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1)) {
            for (int i = 0; i < checker.getPatternAmount(); i++) {
                if (ownOverallPatternUsage[i] != 0)
                    ratings[i] -= (ownOverallPatternUsage[i] * ownOverallPatternUsage[i]);
                if (opOverallPatternUsage[i] != 0)
                    ratings[i] += (opOverallPatternUsage[i] * opOverallPatternUsage[i]);
            }
            if (ConsoleOutput.playerGreetings)
                System.out.println("Torben: that won't happen again");
        }
        writeRatings();
    }

    /**
     * get data input
     */
    private void readRatings() {
        InputStream fis;
        byte[] data = new byte[0];
        try {
            fis = new FileInputStream(new File(PATH));
            data = new byte[fis.available()];
            fis.read(data);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String input = new String(data);
        if (ConsoleOutput.botInitiation) System.out.println(input);
        final String[] ratingsAr = input.split(",");
        ratings = new int[checker.getPatternAmount()];
        ratings[0] = ratingsAr[0].isEmpty() ? 0 : Integer.valueOf(ratingsAr[0]);
        for (int i = 1; i < ratingsAr.length; i++) {
            ratings[i] = Integer.valueOf(ratingsAr[i]);
        }
    }

    /**
     * get data output
     */
    public void writeRatings() {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < ratings.length - 1; i++) str.append(ratings[i]).append(",");
        str.append(ratings[ratings.length - 1]);
        try {
            FileOutputStream out = new FileOutputStream(new File(PATH));
            out.write(str.toString().getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}