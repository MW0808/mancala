package us.jonathans.entity.rule;

public class JonathanMancalaBoard implements MancalaBoard{
    public final Object[][] board;

    public JonathanMancalaBoard(int startStones) {
        board = getBoardPosition(startStones);
    }

    private static Object[][] getBoardPosition(int startNum) {
        return new Object[][]{
                {startNum, MancalaHole.A},
                {startNum, MancalaHole.B},
                {startNum, MancalaHole.C},
                {startNum, MancalaHole.D},
                {startNum, MancalaHole.E},
                {startNum, MancalaHole.F},
                {0, MancalaHole.G},
                {startNum, MancalaHole.a},
                {startNum, MancalaHole.b},
                {startNum, MancalaHole.c},
                {startNum, MancalaHole.d},
                {startNum, MancalaHole.e},
                {startNum, MancalaHole.f},
                {0, MancalaHole.g}
        };
    }

    @Override
    public int getStones(MancalaHole hole) {
        for (Object[] objects : board) {
            if (objects[1] == hole) {
                return (int) objects[0];
            }
        }
        return 0;
    }

    @Override
    public void setStones(MancalaHole hole, int stones) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][1] == hole) {
                board[i][0] = stones;
            }
        }
    }

    @Override
    public MancalaHole getNextHole(MancalaHole hole) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][1] == hole) {
                return (MancalaHole) board[(i + 1) % 14][1];
            }
        }
        return null;
    }

    @Override
    public int[] asArray() {
        final int[] array = new int[14];
        for (int i = 0; i < 14; i++) {
            array[i] = (int) board[i][0];
        }
        return array;
    }

  public MancalaHole getOppositeHole(MancalaHole hole) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][1] == hole) {
                return (MancalaHole) board[12-i][1];
            }
        }
        return null;
    }

    @Override
    public MancalaBoard clone() {
        JonathanMancalaBoard clone = new JonathanMancalaBoard(4);
        for (int i = 0; i < board.length; i++) {
            clone.board[i] = board[i].clone();
        }
        return clone;
    }

    public Object[][] getBoard() {
        return board;
    }
}
