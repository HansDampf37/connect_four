package bot.Torben;

import model.*;

/**
 * a pattern represents a special situation on the board
 */
public class Pattern {
    public static int ME = 1;
    public static int NULL = 2;
    public static int EMPTY = 0;
    public static int NOT_EMPTY = -1;
    private int[][] pattern;
    private int width;
    private int height;

    public Pattern(int[][] pattern) {
        init(pattern);
    }

    private void init(int[][] pattern) {
        int maxY = pattern[0].length;
        for (int x = 0; x < pattern.length; x++) if (pattern[x].length != maxY) throw new IllegalArgumentException("this Pattern is not squared");
        this.pattern = pattern;
        width = pattern.length;
        height = pattern[0].length;
        addFloor();
    }

    public Pattern(String[] lines) {
        if (lines == null) throw new IllegalArgumentException("Empty input in pattern constructor");
        int[][] res = new int[lines[0].length()][lines.length];
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                switch (c) {
                    case 'x':
                        res[x][y] = ME;
                        break;
                    case ' ':
                        res[x][y] = EMPTY;
                        break;
                    case '-':
                        res[x][y] = NOT_EMPTY;
                        break;
                    case '0':
                        res[x][y] = NULL;
                        break;
                    default:
                        break;
                }
            }
        }
        init(res);
    }

    private void addFloor() {
        int[][] copy;
        for (int x = 0; x < width; x++) {
            if (pattern[x][0] == ME || pattern[x][0] == EMPTY) {
                copy = new int[width][height + 1];
                for(int xVal = 0; xVal < width; xVal++) {
                    for (int yVal = 0; yVal < height; yVal++) {
                        copy[xVal][yVal + 1] = pattern[xVal][yVal];
                    }
                }
                for(int xVal = 0; xVal < width; xVal++) copy[xVal][0] = NOT_EMPTY;
                height++;
                pattern = copy;
            }
        }
    }

    public int amountOfTimesThisPatternIsOnBoard(Field[][] fields, Identifier player) {
        int amounts = 0;
        for (int xOffs = 0; xOffs <= Board.WIDTH - width; xOffs++) {
            for (int yOffs = -1; yOffs <= Board.HEIGHT - height; yOffs++) {
                if (searchWithOffset(xOffs, yOffs, fields, player)) amounts++;
            }
        }

        return amounts;
    }

    private boolean searchWithOffset(int xOffset, int yOffset, Field[][] fields, Identifier player) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y + yOffset < 0) {
                    if (pattern[x][y] != NOT_EMPTY) return false;
                } else {
                    if (pattern[x][y] != NULL) {
                        Identifier boardValue = fields[x + xOffset][y + yOffset].getPlayer();
                        int patternValue = pattern[x][y];
                        if (patternValue == ME) {
                            if (boardValue != player) return false;
                        } else if (patternValue == EMPTY) {
                            if (boardValue != Identifier.EMPTY) return false;
                        } else if (patternValue == NOT_EMPTY) {
                            if (boardValue == Identifier.EMPTY) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean canContain(Pattern other) {
        if (width < other.width) return false;
        if (height < other.height) return false;

        for (int xOffs = 0; xOffs <= width - other.width; xOffs++) {
            for (int yOffs = 0; yOffs <= height - other.height; yOffs++) {
                if (searchWithOffset(xOffs, yOffs, other)) return true;
            }
        }
        return false;
    }

    private boolean searchWithOffset(int xOffset, int yOffset, Pattern otherpattern) {
        for (int x = 0; x < otherpattern.width; x++) {
            for (int y = 0; y < otherpattern.height; y++) {
                int currentVal = pattern[x + xOffset][y + yOffset];
                if (currentVal == ME) if (!(otherpattern.pattern[x][y] == EMPTY || otherpattern.pattern[x][y] == ME || otherpattern.pattern[x][y] == NULL)) return false;
                else if (currentVal == EMPTY) if (otherpattern.pattern[x][y] != EMPTY) return false;
                else if (currentVal == NOT_EMPTY) {}
                else if (currentVal == NULL) {}
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (pattern[x][y] == ME) str.append("X|");
                else if (pattern[x][y] == NOT_EMPTY) str.append("-|");
                else if (pattern[x][y] == NULL) str.append("N|");
                else str.append(" |");
            }
            if (y != 0) str.append("\n|");
        }
        return str.toString();
    }

    // public static void main(String[] args) {
    //     Board b = new Board();
    //     b.throwInColumn(0, Player.PLAYER_1);
    //     b.throwInColumn(1, Player.PLAYER_1);
    //     b.throwInColumn(2, Player.PLAYER_1);
    //     b.throwInColumn(3, Player.PLAYER_1);
    //     b.throwInColumn(4, Player.PLAYER_1);
    //     b.throwInColumn(1, Player.PLAYER_1);
    //     b.throwInColumn(2, Player.PLAYER_1);
    //     b.throwInColumn(3, Player.PLAYER_1);
    //     b.throwInColumn(4, Player.PLAYER_1);
    //     System.out.println(b);
    //     int[][] init = {{ME}, {ME}, {ME}};
    //     int[][] init2 = {{ME}, {ME}, {ME}, {ME}};
    //     Pattern p = new Pattern(init);
    //     Pattern p2 = new Pattern(init2);
    //     System.out.println(p.canContain(p2));
    //     System.out.println(p.amountOfTimesThisPatternOccursOnBoard(b, Player.PLAYER_1));
    // }
}