package us.jonathans.entity.rule;

import java.util.HashSet;
import java.util.Set;

public class JonathanMancalaRuleSet implements MancalaRuleSet {
    @Override
    public MoveResult makeMove(MancalaBoard board, MancalaSide player, MancalaHole hole){
        int stones = board.getStones(hole);
        if (!getLegalMoves(board, player).contains(hole)) {
            return new MoveResult(0, false, false);
        }
        int pastCaptured;

        if (player == MancalaSide.PLAYER1) {
            pastCaptured = board.getStones(MancalaHole.G);
        }
        else {
            pastCaptured = board.getStones(MancalaHole.g);
        }

        if (player == MancalaSide.PLAYER1) {
            board.setStones(hole, 0);
            while (stones > 0) {
                hole = board.getNextHole(hole);
                if (hole == MancalaHole.g) {
                    continue;
                }
                else {
                    board.setStones(hole, board.getStones(hole) + 1);
                    stones--;
                }
            }
        } else if (player == MancalaSide.PlAYER2) {
            board.setStones(hole, 0);
            while (stones > 0) {
                hole = board.getNextHole(hole);
                if (hole == MancalaHole.G) {
                }
                else {
                    board.setStones(hole, board.getStones(hole) + 1);
                    stones--;
                }
            }
        }
        return endingMove(board, player, hole, pastCaptured);
    }

    private MoveResult endingMove(MancalaBoard board, MancalaSide player, MancalaHole hole, int pastCaptured) {
        int currCaptured;
        MancalaHole goalPosition = player.getGoal();

        if (board.getStones(hole) == 1
                && getLegalMoves(board, player).contains(hole)
                && board.getStones(board.getOppositeHole(hole)) > 0
        ) {
            board.setStones(hole, 0);
            board.setStones(
                    goalPosition,
                    board.getStones(goalPosition) +
                            1 +
                            board.getStones(board.getOppositeHole(hole)));
            board.setStones(board.getOppositeHole(hole), 0);
        }

        if (isGameOver(board)) {
            for (MancalaSide mancalaSide: new MancalaSide[]{MancalaSide.PLAYER1, MancalaSide.PlAYER2}) {
                for (MancalaHole mancalaHole: getHoles(mancalaSide)) {
                    board.setStones(mancalaSide.getGoal(), board.getStones(mancalaSide.getGoal()) + board.getStones(mancalaHole));
                    board.setStones(mancalaHole, 0);
                }
            }
            currCaptured = board.getStones(goalPosition);
            return new MoveResult(currCaptured - pastCaptured, false, true);
        }
        currCaptured = board.getStones(goalPosition);

        if (hole == MancalaHole.g || hole == MancalaHole.G) {
            return new MoveResult(currCaptured - pastCaptured, true, true);
        }
        return new MoveResult(currCaptured - pastCaptured, false, true);
    }

    @Override
    public Set<MancalaHole> getLegalMoves(MancalaBoard board, MancalaSide player) {
        HashSet<MancalaHole> legalMoves = new HashSet<>();
        if (player == MancalaSide.PLAYER1) {
            for (int i = 0; i < 6; i++) {
                assert getHoles(player) != null;
                if (board.getStones(getHoles(player)[i]) > 0) {
                    legalMoves.add(getHoles(player)[i]);
                }
            }
        } else if (player == MancalaSide.PlAYER2) {
            for (int i = 0; i < 6; i++) {
                if (board.getStones(getHoles(player)[i]) > 0) {
                    legalMoves.add(getHoles(player)[i]);
                }
            }
        }
        return legalMoves;
    }

    private MancalaHole[] getHoles(MancalaSide player) {
        if (player == MancalaSide.PLAYER1) {
            MancalaHole[] holes = {
                    MancalaHole.A,
                    MancalaHole.B,
                    MancalaHole.C,
                    MancalaHole.D,
                    MancalaHole.E,
                    MancalaHole.F,
            };
            return holes;
        } else if (player == MancalaSide.PlAYER2) {
            MancalaHole[] holes = {
                    MancalaHole.a,
                    MancalaHole.b,
                    MancalaHole.c,
                    MancalaHole.d,
                    MancalaHole.e,
                    MancalaHole.f,
            };
            return holes;
        }
        return new MancalaHole[0];
    }

    @Override
    public MancalaSide checkWin(MancalaBoard board) {

        if (isGameOver(board)) {
            if (board.getStones(MancalaHole.g) < board.getStones(MancalaHole.G)) {
                return MancalaSide.PLAYER1;
            } else if (board.getStones(MancalaHole.G) < board.getStones(MancalaHole.g)) {
                return MancalaSide.PlAYER2;
            }
            return null;
        }
        return null;
    }

    @Override
    public Boolean isGameOver(MancalaBoard board) {
        if (getLegalMoves(board, MancalaSide.PLAYER1).isEmpty() || getLegalMoves(board, MancalaSide.PlAYER2).isEmpty()) {
            return true;
        }
        return false;
    }
}


