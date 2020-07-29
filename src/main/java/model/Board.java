package model;

public class Board {
    public final int HEIGHT;
    public final int WIDTH;
    private Field[][] fields;

    public Board(int width, int height) {
        HEIGHT = height;
        WIDTH = width;
        fields = new Field[WIDTH][HEIGHT];
        for (int j = 0; j < HEIGHT; j++) for (int i = 0; i < WIDTH; i++) fields[i][j] = new Field();
    }

    public boolean throwInColumn(int x, Identifier player) {
        for (int y = 0; y < HEIGHT; y++){
            if (fields[x][y].isEmpty()) {
                fields[x][y].setPlayer(player);
                return true;
            }
        }
        return false;
    }

    public void removeOfColumn(int x) {
        for (int y = HEIGHT - 1; y >= 0; y--){
            if (!fields[x][y].isEmpty()) {
                fields[x][y].setPlayer(Identifier.EMPTY);
                return;
            }
        }
    }

    public Identifier getWinner() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Identifier res = checkFor4RowOnField(x, y);
                if (res != Identifier.EMPTY) return res;
            }
        }
        return Identifier.EMPTY;
    }

    private Identifier checkFor4RowOnField(int x, int y) {
        Identifier playerToCheck = fields[x][y].getPlayer();
        if (playerToCheck == Identifier.EMPTY) return Identifier.EMPTY;
        int amountInRow = 1;
        for (int dx = 1; true; dx++) {
            if (x + dx >= WIDTH) break;
            if (fields[x + dx][y].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int dx = 1; true; dx++) {
            if (x - dx < 0) break;
            if (fields[x - dx][y].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        amountInRow = 1;
        for (int dy = 1; true; dy++) {
            if (y + dy >= HEIGHT) break;
            if (fields[x][y + dy].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int dy = 1; true; dy++) {
            if (y - dy < 0) break;
            if (fields[x][y - dy].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        amountInRow = 1;
        for (int d = 1; true; d++) {
            if (x + d >= WIDTH || y + d >= HEIGHT) break;
            if (fields[x + d][y + d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int d = 1; true; d++) {
            if (x - d < 0 || y - d < 0) break;
            if (fields[x - d][y - d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        amountInRow = 1;
        for (int d = 1; true; d++) {
            if (x + d >= WIDTH || y - d < 0) break;
            if (fields[x + d][y - d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        for (int d = 1; true; d++) {
            if (x - d < 0 || y + d >= HEIGHT) break;
            if (fields[x - d][y + d].getPlayer() != playerToCheck) break;
            amountInRow++;
        }
        if (amountInRow >= 4) return playerToCheck;

        return Identifier.EMPTY;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder().append("|");
        for (int y = HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                if (fields[x][y].getPlayer() == Identifier.PLAYER_1) str.append("X|");
                else if (fields[x][y].getPlayer() == Identifier.PLAYER_2) str.append("O|");
                else str.append(" |");
            }
            if (y != 0) str.append("\n|");
        }
        str.append("\n");
        for (int x = 0; x < WIDTH; x++) str.append("--");
        return str.append("-").toString();
    }

	public boolean stillSpace() {
        int y = HEIGHT - 1;
        for (int x = 0; x < WIDTH; x++) if (fields[x][y].getPlayer() == Identifier.EMPTY) return true;
        return false;
    }

    public Field get(int x, int y) {
        return fields[x][y];
    }
}