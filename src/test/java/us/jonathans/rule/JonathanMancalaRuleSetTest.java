package us.jonathans.rule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import us.jonathans.entity.rule.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class JonathanMancalaRuleSetTest {
    private JonathanMancalaRuleSet ruleSet;
    private JonathanMancalaBoard board;
    @BeforeEach
    void setUp() {
        board = new JonathanMancalaBoard(4);
        ruleSet = new JonathanMancalaRuleSet();
    }

    @Test
    void makeLegalMovePlayer1() {
        ruleSet.makeMove(board, MancalaSide.PLAYER1, MancalaHole.A);
        int[] correct = new int[]{
                0,
                5,
                5,
                5,
                5,
                4,
                0,
                4,
                4,
                4,
                4,
                4,
                4,
                0
        };
        assertEquals(board.getBoard()[0][0], correct[0]);
        for (int i = 0; i < board.getBoard().length; i++) {
            assertEquals(board.getBoard()[i][0], correct[i]);
        }
    }

    @Test
    void makeLegalMovePlayer2() {
        ruleSet.makeMove(board, MancalaSide.PlAYER2, MancalaHole.a);
        int[] correct = new int[]{
                4,
                4,
                4,
                4,
                4,
                4,
                0,
                0,
                5,
                5,
                5,
                5,
                4,
                0
        };
        assertEquals(board.getBoard()[0][0], correct[0]);
        for (int i = 0; i < board.getBoard().length; i++) {
            assertEquals(board.getBoard()[i][0], correct[i]);
        }
    }

    @Test
    void noLegalMoves() {
        board = new JonathanMancalaBoard(0);
        HashSet<MancalaHole> moves = new HashSet<>();
        assertEquals(moves, ruleSet.getLegalMoves(board, MancalaSide.PLAYER1));
        assertEquals(moves, ruleSet.getLegalMoves(board, MancalaSide.PlAYER2));
    }

    @Test
    void checkWin() {
        MancalaSide winner = null;
        while (!ruleSet.isGameOver(board)) {
            for (MancalaHole hole: ruleSet.getLegalMoves(board, MancalaSide.PLAYER1)) {
                ruleSet.makeMove(board, MancalaSide.PLAYER1, hole);
            }
            for (MancalaHole hole: ruleSet.getLegalMoves(board, MancalaSide.PlAYER2)) {
                ruleSet.makeMove(board, MancalaSide.PlAYER2, hole);
            }
            if (board.getStones(MancalaHole.g) < board.getStones(MancalaHole.G)) {
                winner = MancalaSide.PLAYER1;
            } else if (board.getStones(MancalaHole.G) < board.getStones(MancalaHole.g)) {
                winner = MancalaSide.PlAYER2;
            }
        }
        assertEquals(ruleSet.checkWin(board), winner);
    }

    @Test
    void checkWinFalse() {
        assertEquals(ruleSet.checkWin(board), null);
    }

    @Test
    void isGameOverFalse() {
        ruleSet.isGameOver(board);
        assertEquals(ruleSet.isGameOver(board), false);
    }

    @Test
    void isGameOverTrue() {
        while (!ruleSet.isGameOver(board)) {
            for (MancalaHole hole: ruleSet.getLegalMoves(board, MancalaSide.PLAYER1)) {
                ruleSet.makeMove(board, MancalaSide.PLAYER1, hole);
            }
            for (MancalaHole hole: ruleSet.getLegalMoves(board, MancalaSide.PlAYER2)) {
                ruleSet.makeMove(board, MancalaSide.PlAYER2, hole);
            }
        }
        assertEquals(ruleSet.isGameOver(board), true);
    }

    @Test
    void testInvalidMakeMove() {
        assertEquals(
                new MoveResult(0, false, false),
                ruleSet.makeMove(board, MancalaSide.PLAYER1, MancalaHole.a));
    }

    @Test
    void testMakeMoveLoopOverGoalPlayer1() {
        board.setStones(MancalaHole.F, 8);
        ruleSet.makeMove(board, MancalaSide.PLAYER1, MancalaHole.F);
        int[] correct = new int[]{
                5,
                4,
                4,
                4,
                4,
                0,
                1,
                5,
                5,
                5,
                5,
                5,
                5,
                0
        };
        assertEquals(board.getBoard()[13][0], correct[13]);
        for (int i = 0; i < board.getBoard().length; i++) {
            assertEquals(board.getBoard()[i][0], correct[i]);
        }
    }

    @Test
    void testMakeMoveLoopOverGoalPlayer2() {
        board.setStones(MancalaHole.f, 8);
        ruleSet.makeMove(board, MancalaSide.PlAYER2, MancalaHole.f);
        int[] correct = new int[]{
                5,
                5,
                5,
                5,
                5,
                5,
                0,
                5,
                4,
                4,
                4,
                4,
                0,
                1
        };
        assertEquals(board.getBoard()[13][0], correct[13]);
        for (int i = 0; i < board.getBoard().length; i++) {
            assertEquals(board.getBoard()[i][0], correct[i]);
        }
    }

    @Test
    void testMoveAgain() {
        assertEquals(
                new MoveResult(1, true, true),
        ruleSet.makeMove(board, MancalaSide.PLAYER1, MancalaHole.C));
    }

//    @Test
//    void testCaptureOpponentStones() {
//        int[] newBoard = new int[]{
//                0,
//                4,
//                4,
//                4,
//                5,
//                5,
//                6,
//                4,
//                4,
//                4,
//                0,
//                4,
//                4,
//                0
//        };
//        for (int i = 0; i < board.getBoard().length; i++) {
//            assertEquals(board.getBoard()[i][0], correct[i]);
//        }
//    }
}