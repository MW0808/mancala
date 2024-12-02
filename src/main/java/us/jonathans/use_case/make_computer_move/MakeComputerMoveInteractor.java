package us.jonathans.use_case.make_computer_move;

import us.jonathans.data_access.match.MatchDataAccessInterface;
import us.jonathans.entity.engine.Engine;
import us.jonathans.data_access.engine.EngineManager;
import us.jonathans.entity.match.EngineMatch;
import us.jonathans.entity.rule.Game;
import us.jonathans.entity.rule.MancalaHole;
import us.jonathans.observable.publisher.MatchEndPublisher;

import java.util.logging.Logger;

public class MakeComputerMoveInteractor implements MakeComputerMoveInputBoundary{
    private final MatchDataAccessInterface matchDataAccessInterface;
    private final MakeComputerMoveOutputBoundary makeComputerMovePresenter;

    public MakeComputerMoveInteractor (MatchDataAccessInterface matchDataAccessInterface,
                                       MakeComputerMoveOutputBoundary makeComputerMoveOutputBoundary) {
        this.matchDataAccessInterface = matchDataAccessInterface;
        this.makeComputerMovePresenter = makeComputerMoveOutputBoundary;

    }

    // Executes the logic for making a computer move: determines the best move using the engine,
    // applies it, and updates the presenter with the new game state.
    @Override
    public void execute(MakeComputerMoveInputData inputData) {
        Logger.getLogger("computer move use case").info("Making an engine move");
        EngineMatch match = matchDataAccessInterface.getCurrentMatch();
        Game game = match.getGame();
        Engine engine = new EngineManager().getEngine(match.getEngineId(), game.getRuleSet());
        MancalaHole bestMove = engine.findBestMove(game.getBoard(), game.getCurrentSide());
        if (bestMove == null) {
            throw new IllegalStateException("No valid move was determined by the AI.");
        }
        game.makeMove(bestMove);
        makeComputerMovePresenter.prepareSuccessView(
                new MakeComputerMoveOutputData(game.getBoard().asArray())
        );
        if (game.isGameOver()) {
            MatchEndPublisher.getInstance().publish(match);
        }
    }
}
