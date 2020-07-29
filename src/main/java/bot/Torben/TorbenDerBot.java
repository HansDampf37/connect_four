package bot.Torben;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import model.procedure.ConsoleOutput;
import bot.PonderingBot;
import bot.tree.Tree;
import model.*;

/**
 * This Bot analyzes the board and recognizes given patterns (for example 3 in a
 * row with a free space at the end). Every pattern is associated with a rating.
 * Out of all possible moves the bot picks the one with the highest accumulated
 * rating. Upon finishing a game the ratings of all patterns that were used by
 * the loser become decreased, and the ones of the patterns used by the winner
 * get increased. The In/Decrease itself is anti-proportional to the amount of
 * times theses patterns were used.
 */
public class TorbenDerBot extends PonderingBot {
    /**
     * Array containing ratings for patterns
     */
    private int[] ratings;
    /**
     * Contains a set of hardcoded patterns
     */
    private final PatternSet checker = new PatternSet();
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
    private int[] oppCurrentPatternUsage;
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
        oppCurrentPatternUsage = new int[checker.getPatternAmount()];
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

    public int getColumnOfNextMove() {
        // checks which patterns have been used in the opponents last move
        updateOpponentsOverallPatternUsage();
        // checks which patterns he is currently using in order to compare them to later
        // TODO not accurate
        checkOwnCurrentPatternUsage();

        // builds a tree
        Tree states = new Tree(forecast, board.WIDTH);
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
        int rating = states.getRoot().getValue();
        if (rating > Integer.MAX_VALUE - board.HEIGHT) {
            adapt(true);
        } else if (rating < Integer.MIN_VALUE + board.HEIGHT) {
            adapt(false);
        }
        return bestColumn;
    }

    private void adapt(boolean won) {
        if (won) {
            for (int i = 0; i < checker.getPatternAmount(); i++) {
                if (ownOverallPatternUsage[i] != 0)
                    ratings[i] += (ownOverallPatternUsage[i] * ownOverallPatternUsage[i]);
                if (opOverallPatternUsage[i] != 0)
                    ratings[i] -= (opOverallPatternUsage[i] * opOverallPatternUsage[i]);
            }
        } else {
            for (int i = 0; i < checker.getPatternAmount(); i++) {
                if (ownOverallPatternUsage[i] != 0)
                    ratings[i] -= (ownOverallPatternUsage[i] * ownOverallPatternUsage[i]);
                if (opOverallPatternUsage[i] != 0)
                    ratings[i] += (opOverallPatternUsage[i] * opOverallPatternUsage[i]);
            }
        }
        writeRatings();
    }

    /**
     * checks which patterns are used by the opponent at the moment
     */
    private void checkOpponentsCurrentPatternUsage() {
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            oppCurrentPatternUsage[i] = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board,
                    side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        }
    }

    /**
     * checks which patterns are used by itself at the moment
     */
    private void checkOwnCurrentPatternUsage() {
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            ownCurrentPatternUsage[i] = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board,
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
            int occurrence = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board,
                    side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
            if (ConsoleOutput.patternRecognition)
                if (occurrence > 0)
                    System.out.println("Pattern: \n" + checker.getPattern(i) + "    " + occurrence
                            + "\n---------------\n---------------");
            if (occurrence > oppCurrentPatternUsage[i]) {
                opOverallPatternUsage[i] += occurrence - oppCurrentPatternUsage[i];
            }
            ownCurrentPatternUsage[i] = occurrence;
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
            int occurence = checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board, side);
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
    protected int rateState() {
        int rating = 0;
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            rating += ratings[i] * checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board, side);
        }
        for (int i = 0; i < checker.getPatternAmount(); i++) {
            rating -= ratings[i] * checker.getPattern(i).amountOfTimesThisPatternIsOnBoard(board,
                    side == Identifier.PLAYER_1 ? Identifier.PLAYER_2 : Identifier.PLAYER_1);
        }
        return rating;
    }

    @Override
    public String getName() {
        return "Torben";
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
    private void writeRatings() {
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