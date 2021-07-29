package model.procedure;

public class ConsoleOutput {
    public static boolean botInitiation = false;
    public static boolean patternInitiation = false;
    public static boolean patternRecognition = false;
    public static boolean treeInitiation = false;
    public static boolean treeTraversal = false;
    public static boolean gameResult = true;
    public static boolean printBoard = true;
    public static boolean playerGreetings = false;

    public static void setAll(boolean printBoard, boolean botInit, boolean patternInit, boolean treeInit, boolean patternRecog, boolean treeTraversal, boolean gameResult, boolean playerGreetings) {
        ConsoleOutput.printBoard = printBoard;
        ConsoleOutput.botInitiation = botInit;
        ConsoleOutput.patternInitiation = patternInit;
        ConsoleOutput.gameResult = gameResult;
        ConsoleOutput.treeInitiation = treeInit;
        ConsoleOutput.patternRecognition = patternRecog;
        ConsoleOutput.treeTraversal = treeTraversal;
        ConsoleOutput.playerGreetings = playerGreetings;
    }
}